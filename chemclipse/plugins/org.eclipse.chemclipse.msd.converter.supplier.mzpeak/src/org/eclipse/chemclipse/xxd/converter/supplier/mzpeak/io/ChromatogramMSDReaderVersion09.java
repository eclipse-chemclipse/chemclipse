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

	private static final String CHROMATOGRAM_INDEX = "chromatogram_index";
	private static final String TIME = "time";
	private static final String INTENSITY = "intensity";
	private static final String MS_LEVEL = "ms_level";

	@FunctionalInterface
	private interface IScanBuilder {

		void addScan(int retentionTime, float intensity, short msLevel);
	}

	private boolean isMultiStageMassSpectrum = false;

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = new VendorChromatogram();
		readPackage(file, chromatogram);

		Path chromatogramMetadataParquet = extract(file, "chromatograms_metadata.parquet");
		int ticIndex = extractTicIndexFromMetadata(chromatogramMetadataParquet);
		Path chromatogramDataParquet = extract(file, "chromatograms_data.parquet");
		readTIC(chromatogramDataParquet, ticIndex, chromatogram);

		return chromatogram;
	}

	@Override
	public IChromatogramMSD read(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = new VendorChromatogram();
		readPackage(file, chromatogram);

		Path spectraPeaksParquet = extract(file, "spectra_peaks.parquet");
		Path spectraDataParquet = extract(file, "spectra_data.parquet");
		IReaderProxy readerProxy = new ReaderProxy(spectraPeaksParquet, spectraDataParquet);
		Path chromatogramMetadataParquet = extract(file, "chromatograms_metadata.parquet");
		int ticIndex = extractTicIndexFromMetadata(chromatogramMetadataParquet);
		Path chromatogramDataParquet = extract(file, "chromatograms_data.parquet");
		addScanProxies(chromatogramDataParquet, ticIndex, chromatogram, readerProxy, monitor);
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

	private int extractTicIndexFromMetadata(Path chromatogramMetadataParquet) {

		try (ParquetFileReader parquetFileReader = ParquetReaderSupport.open(chromatogramMetadataParquet)) {
			MessageType schema = parquetFileReader.getFooter().getFileMetaData().getSchema();
			PageReadStore pageReadStore;
			while((pageReadStore = parquetFileReader.readNextRowGroup()) != null) {
				RecordReader<Group> recordReader = ParquetReaderSupport.getRecordReader(schema, pageReadStore);
				long rows = pageReadStore.getRowCount();
				for(long row = 0; row < rows; row++) {
					Group point = ParquetReaderSupport.getGroup(recordReader.read(), ParquetReaderSupport.POINT);
					if(point == null) {
						continue;
					}
					if(point.getString("id", 0).equals("TIC") || point.getString("chromatogram_type", 0).equals("MS1000235")) {
						return point.getInteger("index", 0);
					}
				}
			}
		} catch(IOException e) {
			logger.error(e);
		}
		return 0;
	}

	private static void readTIC(Path chromatogramDataParquet, int ticIndex, IVendorChromatogram chromatogram) {

		readChromatogram(chromatogramDataParquet, ticIndex, (retentionTime, intensity, msLevel) -> {
			IVendorScan scan = new VendorScan();
			scan.setRetentionTime(retentionTime);
			IIon tic = new Ion(IIon.TIC_ION, intensity);
			scan.addIon(tic);
			scan.setMassSpectrometer(msLevel);
			chromatogram.addScan(scan);
		});
	}

	private void addScanProxies(Path chromatogramDataParquet, int ticIndex, IVendorChromatogram chromatogram, IReaderProxy readerProxy, IProgressMonitor monitor) {

		int[] cycleNumber = new int[]{isMultiStageMassSpectrum ? 1 : 0};

		readChromatogram(chromatogramDataParquet, ticIndex, (retentionTime, intensity, msLevel) -> {
			IVendorScanProxy scanProxy = new VendorScanProxy(readerProxy, monitor);
			scanProxy.setRetentionTime(retentionTime);
			scanProxy.setTotalSignal(intensity);
			scanProxy.setMassSpectrometer(msLevel);
			if(msLevel < 2) {
				cycleNumber[0]++;
			}
			if(cycleNumber[0] >= 1) {
				scanProxy.setCycleNumber(cycleNumber[0]);
			}
			chromatogram.addScan(scanProxy);
		});
	}

	private static void readChromatogram(Path chromatogramDataParquet, int ticIndex, IScanBuilder scanBuilder) {

		try (ParquetFileReader parquetFileReader = ParquetReaderSupport.open(chromatogramDataParquet)) {
			MessageType schema = parquetFileReader.getFooter().getFileMetaData().getSchema();
			PageReadStore pageReadStore;
			while((pageReadStore = parquetFileReader.readNextRowGroup()) != null) {
				RecordReader<Group> recordReader = ParquetReaderSupport.getRecordReader(schema, pageReadStore);
				long rows = pageReadStore.getRowCount();
				for(long row = 0; row < rows; row++) {
					Group group = recordReader.read();
					Group point = ParquetReaderSupport.getGroup(group, ParquetReaderSupport.POINT);
					if(point != null) {
						if(ParquetReaderSupport.getNumber(point, CHROMATOGRAM_INDEX, 0) == ticIndex) {
							readPoint(point, scanBuilder);
						}
						continue;
					}
					Group chunk = ParquetReaderSupport.getGroup(group, ParquetReaderSupport.CHUNK);
					if(chunk != null && ParquetReaderSupport.getNumber(chunk, CHROMATOGRAM_INDEX, 0) == ticIndex) {
						readChunk(chunk, scanBuilder);
					}
				}
			}
		} catch(IOException e) {
			logger.error(e);
		}
	}

	private static void readPoint(Group point, IScanBuilder scanBuilder) {

		int retentionTime = getRetentionTime(ParquetReaderSupport.getNumber(point, TIME, 0));
		float intensity = (float)ParquetReaderSupport.getNumber(point, INTENSITY, 0);
		short msLevel = (short)ParquetReaderSupport.getNumber(point, MS_LEVEL, 1);
		scanBuilder.addScan(retentionTime, intensity, msLevel);
	}

	private static void readChunk(Group chunk, IScanBuilder scanBuilder) {

		double[] intensities = ChunkReaderSupport.readValues(chunk, INTENSITY);
		double[] times = ChunkReaderSupport.readAxis(chunk, TIME, intensities.length);
		double[] msLevels = ParquetReaderSupport.getNumbers(chunk, MS_LEVEL);
		for(int i = 0; i < times.length; i++) {
			if(Double.isNaN(times[i]) || Double.isNaN(intensities[i])) {
				continue; // separates two runs of data points
			}
			short msLevel = i < msLevels.length && !Double.isNaN(msLevels[i]) ? (short)msLevels[i] : 1;
			scanBuilder.addScan(getRetentionTime(times[i]), (float)intensities[i], msLevel);
		}
	}

	private static int getRetentionTime(double minutes) {

		return (int)Math.round(minutes * IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
	}
}
