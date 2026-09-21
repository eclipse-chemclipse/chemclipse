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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

	private static final String CHROMATOGRAM_DATA_QUERY = """
			    SELECT
			        struct_extract(point, 'chromatogram_index') AS chromatogram_index,
			        struct_extract(point, 'time') AS time,
			        struct_extract(point, 'intensity') AS intensity,
			        struct_extract(point, 'ms_level') AS ms_level
			    FROM read_parquet(?)
			    ORDER BY chromatogram_index, time
			""";

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

		try {
			Class.forName("org.duckdb.DuckDBDriver");
		} catch(ClassNotFoundException e) {
			logger.error(e);
			return;
		}

		try (Connection connection = DriverManager.getConnection("jdbc:duckdb:");
				PreparedStatement preparedStatement = connection.prepareStatement(CHROMATOGRAM_DATA_QUERY)) {

			preparedStatement.setString(1, chromatogramDataParquet.toString());
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while(resultSet.next()) {
					IVendorScan scan = new VendorScan();
					scan.setRetentionTime((int)Math.round(resultSet.getDouble("time") * IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
					IIon tic = new Ion(IIon.TIC_ION, resultSet.getFloat("intensity"));
					scan.addIon(tic);
					scan.setMassSpectrometer(resultSet.getShort("ms_level"));
					chromatogram.addScan(scan);
				}
			}
		} catch(SQLException e) {
			logger.error(e);
		}
	}

	private void addScanProxies(Path chromatogramDataParquet, IVendorChromatogram chromatogram, IReaderProxy readerProxy, IProgressMonitor monitor) {

		try {
			Class.forName("org.duckdb.DuckDBDriver");
		} catch(ClassNotFoundException e) {
			logger.error(e);
			return;
		}

		try (Connection connection = DriverManager.getConnection("jdbc:duckdb:");
				PreparedStatement preparedStatement = connection.prepareStatement(CHROMATOGRAM_DATA_QUERY)) {

			int cycleNumber = isMultiStageMassSpectrum ? 1 : 0;

			preparedStatement.setString(1, chromatogramDataParquet.toString());
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while(resultSet.next()) {
					IVendorScanProxy scanProxy = new VendorScanProxy(readerProxy, monitor);
					scanProxy.setRetentionTime((int)Math.round(resultSet.getDouble("time") * IChromatogramOverview.MINUTE_CORRELATION_FACTOR));
					scanProxy.setTotalSignal(resultSet.getFloat("intensity"));
					scanProxy.setMassSpectrometer(resultSet.getShort("ms_level"));
					if(scanProxy.getMassSpectrometer() < 2) {
						cycleNumber++;
					}
					if(cycleNumber >= 1) {
						scanProxy.setCycleNumber(cycleNumber);
					}
					chromatogram.addScan(scanProxy);
				}
			}
		} catch(SQLException e) {
			logger.error(e);
		}
	}
}
