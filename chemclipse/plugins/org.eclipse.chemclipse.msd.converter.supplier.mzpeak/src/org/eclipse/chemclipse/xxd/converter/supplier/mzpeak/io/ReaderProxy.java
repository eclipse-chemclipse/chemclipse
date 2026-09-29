/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.column.statistics.IntStatistics;
import org.apache.parquet.column.statistics.LongStatistics;
import org.apache.parquet.column.statistics.Statistics;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.metadata.BlockMetaData;
import org.apache.parquet.hadoop.metadata.ColumnChunkMetaData;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.MessageType;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.IVendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.IVendorScanProxy;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.VendorIon;
import org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.preferences.PreferenceSupplier;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Reads the peaks of a single scan from the spectra tables of an mzPeak package.
 * <p>
 * The tables hold one row per data point, so the peaks of a scan are a contiguous run of rows.
 * Decoding is therefore done per Parquet row group and the result is kept until a scan outside that
 * row group is asked for, which is what keeps a chromatogram from decompressing the whole file once
 * per scan.
 */
public class ReaderProxy implements IReaderProxy {

	private static final Logger logger = Logger.getLogger(ReaderProxy.class);

	private static final String SPECTRUM_INDEX = "spectrum_index";
	private static final String MZ = "mz";
	private static final String INTENSITY = "intensity";

	private Path spectraPeaksParquet;
	private Path spectraDataParquet;

	private Path cachedFile = null;
	private long[][] blockRanges = null;
	private int cachedFirstBlock = -1;
	private int cachedLastBlock = -1;
	private long[] spectrumIndices = new long[0];
	private double[] masses = new double[0];
	private float[] abundances = new float[0];
	private int cachedSize = 0;

	public ReaderProxy(Path spectraPeaksParquet, Path spectraDataParquet) {

		this.spectraPeaksParquet = spectraPeaksParquet;
		this.spectraDataParquet = spectraDataParquet;
	}

	@Override
	public synchronized void readMassSpectrum(IVendorScanProxy scanProxy, IProgressMonitor monitor) throws IOException {

		monitor.beginTask(ConverterMessages.importScan, IProgressMonitor.UNKNOWN);

		Path spectraParquet = null;
		if(PreferenceSupplier.isImportCentroidedSpectra()) {
			spectraParquet = spectraPeaksParquet;
		} else if(PreferenceSupplier.isImportProfileSpectra()) {
			spectraParquet = spectraDataParquet;
		}

		if(spectraParquet == null) {
			monitor.done();
			return;
		}

		long spectrumIndex = scanProxy.getScanNumber() - 1L;

		try {
			load(spectraParquet, spectrumIndex);
			for(int i = 0; i < cachedSize; i++) {
				if(spectrumIndices[i] != spectrumIndex) {
					continue;
				}
				double mz = masses[i];
				if(mz > 0) {
					IVendorIon ion = new VendorIon(mz, abundances[i]);
					scanProxy.addIon(ion);
				}
			}
		} catch(IOException e) {
			logger.error(e);
		}

		monitor.done();
	}

	/**
	 * Makes sure the row groups that can hold the given scan are decoded and cached.
	 */
	private void load(Path spectraParquet, long spectrumIndex) throws IOException {

		if(!spectraParquet.equals(cachedFile)) {
			readBlockRanges(spectraParquet);
		}

		int firstBlock = -1;
		int lastBlock = -1;
		for(int i = 0; i < blockRanges.length; i++) {
			long[] range = blockRanges[i];
			if(range != null && (spectrumIndex < range[0] || spectrumIndex > range[1])) {
				continue;
			}
			if(firstBlock < 0) {
				firstBlock = i;
			}
			lastBlock = i;
		}

		if(firstBlock < 0) {
			cachedSize = 0;
			cachedFirstBlock = -1;
			cachedLastBlock = -1;
			return;
		}

		if(firstBlock == cachedFirstBlock && lastBlock == cachedLastBlock) {
			return; // already decoded
		}

		decode(spectraParquet, firstBlock, lastBlock);
	}

	private void readBlockRanges(Path spectraParquet) throws IOException {

		try (ParquetFileReader parquetFileReader = ParquetReaderSupport.open(spectraParquet)) {
			List<BlockMetaData> blocks = parquetFileReader.getRowGroups();
			blockRanges = new long[blocks.size()][];
			for(int i = 0; i < blocks.size(); i++) {
				blockRanges[i] = getSpectrumIndexRange(blocks.get(i));
			}
		}
		cachedFile = spectraParquet;
		cachedFirstBlock = -1;
		cachedLastBlock = -1;
		cachedSize = 0;
	}

	/**
	 * Returns the smallest and the largest scan of a row group, or null when the writer stored no
	 * usable statistics, in which case the row group has to be treated as if it held every scan.
	 */
	private static long[] getSpectrumIndexRange(BlockMetaData block) {

		for(ColumnChunkMetaData column : block.getColumns()) {
			String path = column.getPath().toDotString();
			if(!path.substring(path.lastIndexOf('.') + 1).equals(SPECTRUM_INDEX)) {
				continue;
			}
			Statistics<?> statistics = column.getStatistics();
			if(statistics == null || statistics.isEmpty() || !statistics.hasNonNullValue()) {
				return null;
			}
			if(statistics instanceof LongStatistics longStatistics) {
				return new long[]{longStatistics.getMin(), longStatistics.getMax()};
			}
			if(statistics instanceof IntStatistics intStatistics) {
				return new long[]{intStatistics.getMin(), intStatistics.getMax()};
			}
			return null;
		}
		return null;
	}

	private void decode(Path spectraParquet, int firstBlock, int lastBlock) throws IOException {

		int capacity = 0;
		try (ParquetFileReader parquetFileReader = ParquetReaderSupport.open(spectraParquet)) {
			List<BlockMetaData> blocks = parquetFileReader.getRowGroups();
			for(int i = firstBlock; i <= lastBlock; i++) {
				capacity += (int)blocks.get(i).getRowCount();
			}
			if(spectrumIndices.length < capacity) {
				spectrumIndices = new long[capacity];
				masses = new double[capacity];
				abundances = new float[capacity];
			}

			MessageType schema = parquetFileReader.getFooter().getFileMetaData().getSchema();
			for(int i = 0; i < firstBlock; i++) {
				parquetFileReader.skipNextRowGroup();
			}

			int size = 0;
			for(int i = firstBlock; i <= lastBlock; i++) {
				PageReadStore pageReadStore = parquetFileReader.readNextRowGroup();
				if(pageReadStore == null) {
					break;
				}
				RecordReader<Group> recordReader = ParquetReaderSupport.getRecordReader(schema, pageReadStore);
				for(long row = 0, rows = pageReadStore.getRowCount(); row < rows; row++) {
					Group point = ParquetReaderSupport.readPoint(recordReader);
					if(point == null) {
						continue;
					}
					spectrumIndices[size] = (long)ParquetReaderSupport.getNumber(point, SPECTRUM_INDEX, -1);
					masses[size] = ParquetReaderSupport.getNumber(point, MZ, 0);
					abundances[size] = (float)ParquetReaderSupport.getNumber(point, INTENSITY, 0);
					size++;
				}
			}
			cachedSize = size;
		}

		cachedFirstBlock = firstBlock;
		cachedLastBlock = lastBlock;
	}
}
