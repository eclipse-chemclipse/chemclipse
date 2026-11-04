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
import java.util.Map;
import java.util.function.IntFunction;

import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.MessageType;
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

	public ReaderProxy(Path spectraParquet) {

		this.spectraParquet = spectraParquet;
	}

	@Override
	public void readMassSpectrum(IVendorScanProxy scanProxy, IProgressMonitor monitor) throws IOException {

		int spectrumIndex = scanProxy.getScanNumber() - 1;
		try {
			readSpectra(index -> index == spectrumIndex ? scanProxy : null, monitor);
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
		}, monitor);
	}

	/**
	 * Passes once through the spectra file and adds each ion to the scan proxy
	 * the resolver returns for its spectrum index, or skips it on null.
	 */
	private void readSpectra(IntFunction<IVendorScanProxy> resolver, IProgressMonitor monitor) throws IOException {

		try (ParquetFileReader parquetFileReader = ParquetReaderSupport.open(spectraParquet)) {
			MessageType schema = parquetFileReader.getFooter().getFileMetaData().getSchema();
			SubMonitor subMonitor = SubMonitor.convert(monitor, ConverterMessages.importScan, (int)Math.min(Integer.MAX_VALUE, parquetFileReader.getRecordCount()));
			PageReadStore pageReadStore;
			while((pageReadStore = parquetFileReader.readNextRowGroup()) != null) {
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
