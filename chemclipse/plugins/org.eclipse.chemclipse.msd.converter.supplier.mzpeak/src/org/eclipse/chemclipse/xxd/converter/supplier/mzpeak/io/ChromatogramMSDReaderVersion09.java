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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.MessageType;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramReader;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.IVendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.IVendorScan;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.IVendorScanProxy;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.VendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.VendorScan;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.VendorScanProxy;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.InstrumentConfiguration;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.Metadata;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.MzPeakIndex;
import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json.Param;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChromatogramMSDReaderVersion09 extends AbstractChromatogramReader implements IChromatogramMSDReader {

	private static final Logger logger = Logger.getLogger(ChromatogramMSDReaderVersion09.class);

	private static final String TIME = "time";
	private static final String INTENSITY = "intensity";
	private static final String MS_LEVEL = "ms_level";

	private boolean isMultiStageMassSpectrum = false;

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = new VendorChromatogram();
		readPackage(file, chromatogram);

		Path chromatogramDataParquet = extract(file, "chromatograms_data.parquet");
		readTIC(chromatogramDataParquet, chromatogram);

		return chromatogram;
	}

	@Override
	public IChromatogramMSD read(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = new VendorChromatogram();
		readPackage(file, chromatogram);

		Path chromatogramDataParquet = extract(file, "chromatograms_data.parquet");

		Path spectraPeaksParquet = extract(file, "spectra_peaks.parquet");
		Path spectraDataParquet = extract(file, "spectra_data.parquet");
		IReaderProxy readerProxy = new ReaderProxy(spectraPeaksParquet, spectraDataParquet);
		addScanProxies(chromatogramDataParquet, chromatogram, readerProxy, monitor);
		return chromatogram;
	}

	private void readPackage(File file, IVendorChromatogram chromatogram) {

		try (ZipFile zipFile = new ZipFile(file)) {
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			while(zipEntries.hasMoreElements()) {
				ZipEntry zipEntry = zipEntries.nextElement();
				if(zipEntry.getName().equals("mzpeak_index.json")) {
					try (InputStream zipInputStream = zipFile.getInputStream(zipEntry)) {
						MzPeakIndex mzPeakIndex = readIndex(zipInputStream);
						readMetadata(mzPeakIndex.getMetadata(), chromatogram);
					}
				}
			}
		} catch(IOException e) {
			logger.warn(e);
		}
	}

	private MzPeakIndex readIndex(InputStream inputStream) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // format is WIP
		return objectMapper.readValue(inputStream, MzPeakIndex.class);
	}

	private void readMetadata(Metadata metadata, IVendorChromatogram chromatogram) {

		for(InstrumentConfiguration instrumentConfiguration : metadata.getInstrumentConfigurationList()) {
			for(Param parameter : instrumentConfiguration.getParameters()) {
				if(parameter.getValue() == null) { // ?
					chromatogram.setInstrument(parameter.getName());
				}
			}
		}

		for(Param parameter : metadata.getFileDescription().getContents()) {
			if("MS:1000580".equals(parameter.getAccession()) && "MSn spectrum".equals(parameter.getName())) {
				isMultiStageMassSpectrum = true;
			}
		}
	}

	private static Path extract(File file, String entryName) throws IOException {

		try (ZipFile zipFile = new ZipFile(file)) {
			ZipEntry entry = zipFile.getEntry(entryName);
			if(entry == null) {
				throw new FileNotFoundException(entryName + " not found.");
			}

			Path tempFile = Files.createTempFile(file.getName(), "-" + Path.of(entry.getName()).getFileName());
			try (InputStream inputStream = zipFile.getInputStream(entry);
					OutputStream outputStream = Files.newOutputStream(tempFile)) {
				inputStream.transferTo(outputStream);
			}

			return tempFile;
		}
	}

	private static void readTIC(Path chromatogramDataParquet, IVendorChromatogram chromatogram) {

		try (ParquetFileReader parquetFileReader = ParquetReaderSupport.open(chromatogramDataParquet)) {
			MessageType schema = parquetFileReader.getFooter().getFileMetaData().getSchema();
			PageReadStore pageReadStore;
			while((pageReadStore = parquetFileReader.readNextRowGroup()) != null) {
				RecordReader<Group> recordReader = ParquetReaderSupport.getRecordReader(schema, pageReadStore);
				for(long row = 0, rows = pageReadStore.getRowCount(); row < rows; row++) {
					Group point = ParquetReaderSupport.readPoint(recordReader);
					if(point == null) {
						continue;
					}
					IVendorScan scan = new VendorScan();
					scan.setRetentionTime((int)Math.round(ParquetReaderSupport.getNumber(point, TIME, 0) * IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
					IIon tic = new Ion(IIon.TIC_ION, (float)ParquetReaderSupport.getNumber(point, INTENSITY, 0));
					scan.addIon(tic);
					scan.setMassSpectrometer((short)ParquetReaderSupport.getNumber(point, MS_LEVEL, 1));
					chromatogram.addScan(scan);
				}
			}
		} catch(IOException e) {
			logger.error(e);
		}
	}

	private void addScanProxies(Path chromatogramDataParquet, IVendorChromatogram chromatogram, IReaderProxy readerProxy, IProgressMonitor monitor) {

		int cycleNumber = isMultiStageMassSpectrum ? 1 : 0;

		try (ParquetFileReader parquetFileReader = ParquetReaderSupport.open(chromatogramDataParquet)) {
			MessageType schema = parquetFileReader.getFooter().getFileMetaData().getSchema();
			PageReadStore pageReadStore;
			while((pageReadStore = parquetFileReader.readNextRowGroup()) != null) {
				RecordReader<Group> recordReader = ParquetReaderSupport.getRecordReader(schema, pageReadStore);
				for(long row = 0, rows = pageReadStore.getRowCount(); row < rows; row++) {
					Group point = ParquetReaderSupport.readPoint(recordReader);
					if(point == null) {
						continue;
					}
					IVendorScanProxy scanProxy = new VendorScanProxy(readerProxy, monitor);
					scanProxy.setRetentionTime((int)Math.round(ParquetReaderSupport.getNumber(point, TIME, 0) * IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
					scanProxy.setTotalSignal((float)ParquetReaderSupport.getNumber(point, INTENSITY, 0));
					scanProxy.setMassSpectrometer((short)ParquetReaderSupport.getNumber(point, MS_LEVEL, 1));
					if(scanProxy.getMassSpectrometer() < 2) {
						cycleNumber++;
					}
					if(cycleNumber >= 1) {
						scanProxy.setCycleNumber(cycleNumber);
					}
					chromatogram.addScan(scanProxy);
				}
			}
		} catch(IOException e) {
			logger.error(e);
		}
	}
}
