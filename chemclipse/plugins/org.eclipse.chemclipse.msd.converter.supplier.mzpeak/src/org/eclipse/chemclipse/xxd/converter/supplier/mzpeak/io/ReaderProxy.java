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
import org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.preferences.PreferenceSupplier;
import org.eclipse.core.runtime.IProgressMonitor;

public class ReaderProxy implements IReaderProxy {

	private static final Logger logger = Logger.getLogger(ReaderProxy.class);

	private static final String SPECTRUM_INDEX = "spectrum_index";
	private static final String MZ = "mz";
	private static final String INTENSITY = "intensity";

	private Path spectraPeaksParquet;
	private Path spectraDataParquet;

	public ReaderProxy(Path spectraPeaksParquet, Path spectraDataParquet) {

		this.spectraPeaksParquet = spectraPeaksParquet;
		this.spectraDataParquet = spectraDataParquet;
	}

	@Override
	public void readMassSpectrum(IVendorScanProxy scanProxy, IProgressMonitor monitor) throws IOException {

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

		int spectrumIndex = scanProxy.getScanNumber() - 1;

		try (ParquetFileReader parquetFileReader = ParquetReaderSupport.open(spectraParquet)) {
			MessageType schema = parquetFileReader.getFooter().getFileMetaData().getSchema();
			PageReadStore pageReadStore;
			while((pageReadStore = parquetFileReader.readNextRowGroup()) != null) {
				RecordReader<Group> recordReader = ParquetReaderSupport.getRecordReader(schema, pageReadStore);
				for(long row = 0, rows = pageReadStore.getRowCount(); row < rows; row++) {
					Group point = ParquetReaderSupport.readPoint(recordReader);
					if(point == null) {
						continue;
					}
					if((long)ParquetReaderSupport.getNumber(point, SPECTRUM_INDEX, -1) != spectrumIndex) {
						continue;
					}
					double mz = ParquetReaderSupport.getNumber(point, MZ, 0);
					float intensity = (float)ParquetReaderSupport.getNumber(point, INTENSITY, 0);
					if(mz > 0) {
						IVendorIon ion = new VendorIon(mz, intensity);
						scanProxy.addIon(ion);
					}
					monitor.worked(1);
				}
			}
		} catch(IOException e) {
			logger.error(e);
		}

		monitor.done();
	}
}
