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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.column.statistics.Statistics;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.filter2.columnindex.RowRanges;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.metadata.BlockMetaData;
import org.apache.parquet.hadoop.metadata.ColumnChunkMetaData;
import org.apache.parquet.internal.column.columnindex.ColumnIndex;
import org.apache.parquet.internal.column.columnindex.OffsetIndex;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.IVendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.IVendorScanProxy;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.VendorIon;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class ReaderProxy implements IReaderProxy {

	private static final Logger logger = Logger.getLogger(ReaderProxy.class);

	private static final String SPECTRUM_INDEX = "spectrum_index";
	private static final String MZ = "mz";
	private static final String INTENSITY = "intensity";

	private Path spectraParquet;
	private List<SpectrumPage> spectrumPages;

	/**
	 * Rows of a row group whose spectrum indices lie between min and max.
	 */
	private record SpectrumPage(int rowGroup, long firstRow, long lastRow, long min, long max) {

		boolean contains(long spectrumIndex) {

			return min <= spectrumIndex && spectrumIndex <= max;
		}
	}

	public ReaderProxy(Path spectraParquet) {

		this.spectraParquet = spectraParquet;
	}

	@Override
	public void readMassSpectrum(IVendorScanProxy scanProxy, IProgressMonitor monitor) throws IOException {

		int spectrumIndex = scanProxy.getScanNumber() - 1;
		try {
			readSpectra(index -> index == spectrumIndex ? scanProxy : null, getRowRanges(spectrumIndex), monitor);
		} catch(IOException e) {
			logger.error(e);
		}
	}

	@Override
	public void readMassSpectra(Map<Integer, IVendorScanProxy> scanProxies, IProgressMonitor monitor) throws IOException {

		if(scanProxies.isEmpty()) {
			return;
		}
		readSpectra(index -> {
			IVendorScanProxy scanProxy = scanProxies.get(index);
			if(scanProxy != null && !scanProxy.isLoaded()) {
				/*
				 * Leave the proxy mode without reading the file again,
				 * the ions are added while passing through the file.
				 */
				scanProxy.loadWithoutImport();
			}
			return scanProxy;
		}, null, monitor);
	}

	/**
	 * Passes once through the spectra file and adds each ion to the scan proxy
	 * the resolver returns for its spectrum index, or skips it on null.
	 * Only the given rows of each row group are read, or all rows if they are null.
	 */
	private void readSpectra(IntFunction<IVendorScanProxy> resolver, Map<Integer, RowRanges> rowRanges, IProgressMonitor monitor) throws IOException {

		try (ParquetFileReader parquetFileReader = ParquetReaderSupport.open(spectraParquet)) {
			MessageType schema = parquetFileReader.getFooter().getFileMetaData().getSchema();
			long recordCount = rowRanges == null ? parquetFileReader.getRecordCount() : rowRanges.values().stream().mapToLong(RowRanges::rowCount).sum();
			SubMonitor subMonitor = SubMonitor.convert(monitor, ConverterMessages.importScan, (int)Math.min(Integer.MAX_VALUE, recordCount));
			int rowGroups = parquetFileReader.getRowGroups().size();
			for(int rowGroup = 0; rowGroup < rowGroups; rowGroup++) {
				PageReadStore pageReadStore = readRowGroup(parquetFileReader, rowGroup, rowRanges);
				if(pageReadStore == null) {
					continue;
				}
				RecordReader<Group> recordReader = ParquetReaderSupport.getRecordReader(schema, pageReadStore);
				long rows = pageReadStore.getRowCount();
				for(long row = 0; row < rows; row++) {
					Group record = recordReader.read();
					Group point = ParquetReaderSupport.getGroup(record, ParquetReaderSupport.POINT);
					if(point != null) {
						IVendorScanProxy scanProxy = resolver.apply((int)ParquetReaderSupport.getNumber(point, SPECTRUM_INDEX, -1));
						if(scanProxy != null) {
							addIon(scanProxy, ParquetReaderSupport.getNumber(point, MZ, 0), (float)ParquetReaderSupport.getNumber(point, INTENSITY, 0));
						}
					} else {
						Group chunk = ParquetReaderSupport.getGroup(record, ParquetReaderSupport.CHUNK);
						if(chunk != null) {
							IVendorScanProxy scanProxy = resolver.apply((int)ParquetReaderSupport.getNumber(chunk, SPECTRUM_INDEX, -1));
							if(scanProxy != null) {
								readChunk(scanProxy, chunk);
							}
						}
					}
					subMonitor.worked(1);
				}
			}
		} finally {
			if(monitor != null) {
				monitor.done();
			}
		}
	}

	private static PageReadStore readRowGroup(ParquetFileReader parquetFileReader, int rowGroup, Map<Integer, RowRanges> rowRanges) throws IOException {

		if(parquetFileReader.getRowGroups().get(rowGroup).getRowCount() == 0) {
			return null;
		}
		if(rowRanges == null) {
			return parquetFileReader.readRowGroup(rowGroup);
		}
		RowRanges rows = rowRanges.get(rowGroup);
		return rows == null ? null : parquetFileReader.readFilteredRowGroup(rowGroup, rows);
	}

	/**
	 * Reading a single spectrum must not decode the whole file.
	 * Returns the rows of the pages that may hold the spectrum, per row group.
	 */
	private Map<Integer, RowRanges> getRowRanges(long spectrumIndex) throws IOException {

		Map<Integer, RowRanges> rowRanges = new HashMap<>();
		for(SpectrumPage spectrumPage : getSpectrumPages()) {
			if(spectrumPage.contains(spectrumIndex)) {
				RowRanges rows = RowRanges.builder().addSelectedRange(spectrumPage.firstRow(), spectrumPage.lastRow()).build();
				rowRanges.merge(spectrumPage.rowGroup(), rows, RowRanges::union);
			}
		}
		return rowRanges;
	}

	/**
	 * The spectrum index range of each page is read once per file from the column index.
	 */
	private synchronized List<SpectrumPage> getSpectrumPages() throws IOException {

		if(spectrumPages == null) {
			List<SpectrumPage> pages = new ArrayList<>();
			try (ParquetFileReader parquetFileReader = ParquetReaderSupport.open(spectraParquet)) {
				List<BlockMetaData> rowGroups = parquetFileReader.getRowGroups();
				for(int rowGroup = 0; rowGroup < rowGroups.size(); rowGroup++) {
					BlockMetaData block = rowGroups.get(rowGroup);
					if(block.getRowCount() == 0) {
						continue;
					}
					/*
					 * Reading single pages needs the offset index of every column.
					 */
					boolean offsetIndexes = block.getColumns().stream().allMatch(column -> column.getOffsetIndexReference() != null);
					for(ColumnChunkMetaData column : block.getColumns()) {
						String[] path = column.getPath().toArray();
						if(path.length == 2 && SPECTRUM_INDEX.equals(path[1])) {
							addSpectrumPages(parquetFileReader, rowGroup, block.getRowCount(), column, offsetIndexes, pages);
						}
					}
				}
			}
			spectrumPages = List.copyOf(pages);
		}
		return spectrumPages;
	}

	private static void addSpectrumPages(ParquetFileReader parquetFileReader, int rowGroup, long rowCount, ColumnChunkMetaData column, boolean offsetIndexes, List<SpectrumPage> pages) throws IOException {

		PrimitiveTypeName type = column.getPrimitiveType().getPrimitiveTypeName();
		ColumnIndex columnIndex = offsetIndexes ? parquetFileReader.readColumnIndex(column) : null;
		OffsetIndex offsetIndex = offsetIndexes ? parquetFileReader.readOffsetIndex(column) : null;
		if((type == PrimitiveTypeName.INT64 || type == PrimitiveTypeName.INT32) && columnIndex != null && offsetIndex != null) {
			for(int page = 0; page < offsetIndex.getPageCount(); page++) {
				if(!columnIndex.getNullPages().get(page)) {
					long min = getValue(columnIndex.getMinValues().get(page), type);
					long max = getValue(columnIndex.getMaxValues().get(page), type);
					pages.add(new SpectrumPage(rowGroup, offsetIndex.getFirstRowIndex(page), offsetIndex.getLastRowIndex(page, rowCount), min, max));
				}
			}
			return;
		}
		/*
		 * Without the indexes, only whole row groups can be skipped.
		 */
		Statistics<?> statistics = column.getStatistics();
		if(statistics != null && !statistics.isEmpty() && !statistics.hasNonNullValue()) {
			return;
		}
		if(statistics != null && statistics.hasNonNullValue() && statistics.genericGetMin() instanceof Number min && statistics.genericGetMax() instanceof Number max) {
			pages.add(new SpectrumPage(rowGroup, 0, rowCount - 1, min.longValue(), max.longValue()));
		} else {
			pages.add(new SpectrumPage(rowGroup, 0, rowCount - 1, Long.MIN_VALUE, Long.MAX_VALUE));
		}
	}

	private static long getValue(ByteBuffer value, PrimitiveTypeName type) {

		ByteBuffer buffer = value.duplicate().order(ByteOrder.LITTLE_ENDIAN);
		return type == PrimitiveTypeName.INT64 ? buffer.getLong(buffer.position()) : buffer.getInt(buffer.position());
	}

	private static void readChunk(IVendorScanProxy scanProxy, Group chunk) {

		double[] intensities = ChunkReaderSupport.readValues(chunk, INTENSITY);
		double[] masses = ChunkReaderSupport.readAxis(chunk, MZ, intensities.length);
		for(int i = 0; i < masses.length; i++) {
			addIon(scanProxy, masses[i], (float)intensities[i]);
		}
	}

	private static void addIon(IVendorScanProxy scanProxy, double mz, float intensity) {

		if(mz > 0 && !Float.isNaN(intensity)) {
			IVendorIon ion = new VendorIon(mz, intensity);
			scanProxy.addIon(ion, false);
		}
	}
}
