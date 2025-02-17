/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.ocx.internal.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IIntegrationEntry;
import org.eclipse.chemclipse.model.core.IPeakIntensityValues;
import org.eclipse.chemclipse.model.core.PeakType;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.exceptions.ReferenceMustNotBeNullException;
import org.eclipse.chemclipse.model.identifier.ChromatogramComparisonResult;
import org.eclipse.chemclipse.model.identifier.ChromatogramLibraryInformation;
import org.eclipse.chemclipse.model.identifier.IChromatogramComparisonResult;
import org.eclipse.chemclipse.model.identifier.IChromatogramLibraryInformation;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.IPeakComparisonResult;
import org.eclipse.chemclipse.model.identifier.IPeakLibraryInformation;
import org.eclipse.chemclipse.model.identifier.PeakComparisonResult;
import org.eclipse.chemclipse.model.identifier.PeakLibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.implementation.IntegrationEntry;
import org.eclipse.chemclipse.model.implementation.PeakIntensityValues;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.io.IChromatogramMSDZipReader;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram.IVendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram.IVendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram.IVendorScan;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram.VendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram.VendorScan;
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.implementation.PeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.PeakModelMSD;
import org.eclipse.chemclipse.support.history.EditInformation;
import org.eclipse.chemclipse.support.history.IEditHistory;
import org.eclipse.chemclipse.support.history.IEditInformation;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.support.MassSpectrometer;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.settings.Format;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Methods are copied to ensure that file formats are kept readable even if they contain errors.
 * This is suitable but I know, it's not the best way to achieve long term support for older formats.
 */
public class ChromatogramReader_0701 extends AbstractChromatogramReader implements IChromatogramMSDZipReader {

	private static final Logger logger = Logger.getLogger(ChromatogramReader_0701.class);

	@Override
	public IChromatogramMSD read(File file, IProgressMonitor monitor) throws IOException {

		IChromatogramMSD chromatogram = null;
		try (ZipFile zipFile = new ZipFile(file)) {
			if(isValidFileFormat(zipFile)) {
				chromatogram = readFromZipFile(zipFile, "", file, monitor);
			}
		}
		return chromatogram;
	}

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		IChromatogramOverview chromatogramOverview = null;
		try (ZipFile zipFile = new ZipFile(file)) {
			if(isValidFileFormat(zipFile)) {
				chromatogramOverview = readOverviewFromZipFile(zipFile, "", monitor);
			}
		}
		return chromatogramOverview;
	}

	@Override
	public IChromatogramMSD read(ZipInputStream zipInputStream, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		return readZipData(zipInputStream, directoryPrefix, null, monitor);
	}

	@Override
	public IChromatogramMSD read(ZipFile zipFile, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		return readFromZipFile(zipFile, directoryPrefix, null, monitor);
	}

	private IChromatogramMSD readFromZipFile(ZipFile zipFile, String directoryPrefix, File file, IProgressMonitor monitor) throws IOException {

		return readZipData(zipFile, directoryPrefix, file, monitor);
	}

	/*
	 * Object = ZipFile or ZipInputStream
	 * @param object
	 * @param file
	 * @return
	 */
	private IChromatogramMSD readZipData(Object object, String directoryPrefix, File file, IProgressMonitor monitor) throws IOException {

		boolean closeStream;
		//
		if(object instanceof ZipFile) {
			/*
			 * ZipFile
			 */
			closeStream = true;
		} else if(object instanceof ZipInputStream) {
			/*
			 * ZipInputStream
			 */
			closeStream = false;
		} else {
			return null;
		}
		//
		IVendorChromatogram chromatogram = new VendorChromatogram();
		//
		readScans(getDataInputStream(object, directoryPrefix + Format.FILE_SCANS), closeStream, chromatogram, monitor);
		readPeaks(getDataInputStream(object, directoryPrefix + Format.FILE_PEAKS), closeStream, chromatogram, monitor);
		readArea(getDataInputStream(object, directoryPrefix + Format.FILE_AREA), closeStream, chromatogram, monitor);
		readIdentification(getDataInputStream(object, directoryPrefix + Format.FILE_IDENTIFICATION), closeStream, chromatogram, monitor);
		readHistory(getDataInputStream(object, directoryPrefix + Format.FILE_HISTORY), closeStream, chromatogram, monitor);
		readMiscellaneous(getDataInputStream(object, directoryPrefix + Format.FILE_MISC), closeStream, chromatogram, monitor);
		setAdditionalInformation(file, chromatogram, monitor);
		//
		return chromatogram;
	}

	private IChromatogramOverview readOverviewFromZipFile(ZipFile zipFile, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		DataInputStream dataInputStream = getDataInputStream(zipFile, directoryPrefix + Format.FILE_TIC);
		//
		IVendorChromatogram chromatogram = new VendorChromatogram();
		readScansOverview(dataInputStream, chromatogram, monitor);
		//
		dataInputStream.close();
		return chromatogram;
	}

	private void readScansOverview(DataInputStream dataInputStream, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws IOException {

		IVendorScan massSpectrum;
		IVendorIon ion;
		/*
		 * Retention Times - Total Signals
		 */
		int scans = dataInputStream.readInt();
		for(int scan = 1; scan <= scans; scan++) {
			massSpectrum = new VendorScan();
			int retentionTime = dataInputStream.readInt();
			float abundance = dataInputStream.readFloat(); // Total Signal
			ion = new VendorIon(AbstractIon.TIC_ION, abundance);
			massSpectrum.setRetentionTime(retentionTime);
			massSpectrum.addIon(ion);
			chromatogram.addScan(massSpectrum);
		}
	}

	private void setAdditionalInformation(File file, IChromatogramMSD chromatogram, IProgressMonitor monitor) {

		chromatogram.setConverterId(Format.CONVERTER_ID_CHROMATOGRAM);
		chromatogram.setFile(file);
		// Delay
		int startRetentionTime = chromatogram.getStartRetentionTime();
		int scanDelay = startRetentionTime;
		chromatogram.setScanDelay(scanDelay);
		// Interval
		int endRetentionTime = chromatogram.getStopRetentionTime();
		int scanInterval = endRetentionTime / chromatogram.getNumberOfScans();
		chromatogram.setScanInterval(scanInterval);
	}

	private void readScans(DataInputStream dataInputStream, boolean closeStream, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws IOException {

		/*
		 * Scans
		 */
		int scans = dataInputStream.readInt();
		for(int scan = 1; scan <= scans; scan++) {
			IVendorScan massSpectrum = readMassSpectrum(dataInputStream);
			chromatogram.addScan(massSpectrum);
		}
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private void readPeaks(DataInputStream dataInputStream, boolean closeStream, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws IOException {

		int numberOfPeaks = dataInputStream.readInt();
		for(int i = 1; i <= numberOfPeaks; i++) {
			try {
				IChromatogramPeakMSD peak = readPeak(dataInputStream, chromatogram, monitor);
				chromatogram.getPeaks().add(peak);
			} catch(IllegalArgumentException e) {
				logger.warn(e);
			} catch(PeakException e) {
				logger.warn(e);
			}
		}
		//
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private IChromatogramPeakMSD readPeak(DataInputStream dataInputStream, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws IOException, IllegalArgumentException, PeakException {

		String detectorDescription = readString(dataInputStream); // Detector Description
		String integratorDescription = readString(dataInputStream); // Integrator Description
		String modelDescription = readString(dataInputStream); // Model Description
		PeakType peakType = PeakType.valueOf(readString(dataInputStream)); // Peak Type
		//
		float startBackgroundAbundance = dataInputStream.readFloat(); // Start Background Abundance
		float stopBackgroundAbundance = dataInputStream.readFloat(); // Stop Background Abundance
		//
		IPeakMassSpectrum peakMaximum = readPeakMassSpectrum(dataInputStream);
		//
		int numberOfRetentionTimes = dataInputStream.readInt(); // Number Retention Times
		IPeakIntensityValues intensityValues = new PeakIntensityValues(Float.MAX_VALUE);
		for(int i = 1; i <= numberOfRetentionTimes; i++) {
			int retentionTime = dataInputStream.readInt();
			float relativeIntensity = dataInputStream.readFloat(); // Intensity
			intensityValues.addIntensityValue(retentionTime, relativeIntensity);
		}
		intensityValues.normalize();
		//
		IPeakModelMSD peakModel = new PeakModelMSD(peakMaximum, intensityValues, startBackgroundAbundance, stopBackgroundAbundance);
		peakModel.setStrictModel(true); // Legacy
		IChromatogramPeakMSD peak = new ChromatogramPeakMSD(peakModel, chromatogram);
		peak.setDetectorDescription(detectorDescription);
		peak.setIntegratorDescription(integratorDescription);
		peak.setModelDescription(modelDescription);
		peak.setPeakType(peakType);
		//
		List<IIntegrationEntry> integrationEntries = readIntegrationEntries(dataInputStream);
		peak.setIntegratedArea(integrationEntries, integratorDescription);
		//
		readPeakIdentificationTargets(dataInputStream, peak, monitor);
		//
		return peak;
	}

	private List<IIntegrationEntry> readIntegrationEntries(DataInputStream dataInputStream) throws IOException {

		List<IIntegrationEntry> integrationEntries = new ArrayList<>();
		int numberOfIntegrationEntries = dataInputStream.readInt(); // Number Integration Entries
		for(int i = 1; i <= numberOfIntegrationEntries; i++) {
			double ion = dataInputStream.readDouble(); // m/z
			double integratedArea = dataInputStream.readDouble(); // Integrated Area
			IIntegrationEntry integrationEntry = new IntegrationEntry(ion, integratedArea);
			integrationEntries.add(integrationEntry);
		}
		return integrationEntries;
	}

	private void readPeakIdentificationTargets(DataInputStream dataInputStream, IPeakMSD peak, IProgressMonitor monitor) throws IOException {

		int numberOfPeakTargets = dataInputStream.readInt(); // Number Peak Targets
		for(int i = 1; i <= numberOfPeakTargets; i++) {
			//
			String identifier = readString(dataInputStream); // Identifier
			//
			String casNumber = readString(dataInputStream); // CAS-Number
			String comments = readString(dataInputStream); // Comments
			String miscellaneous = readString(dataInputStream); // Miscellaneous
			String name = readString(dataInputStream); // Name
			//
			float matchFactor = dataInputStream.readFloat(); // Match Factor
			float reverseMatchFactor = dataInputStream.readFloat(); // Reverse Match Factor
			float probability = dataInputStream.readFloat(); // Probability
			//
			IPeakLibraryInformation libraryInformation = new PeakLibraryInformation();
			libraryInformation.setCasNumber(casNumber);
			libraryInformation.setComments(comments);
			libraryInformation.setMiscellaneous(miscellaneous);
			libraryInformation.setName(name);
			IPeakComparisonResult comparisonResult = new PeakComparisonResult(matchFactor, reverseMatchFactor, 0.0f, 0.0f, probability);
			try {
				IIdentificationTarget identificationEntry = new IdentificationTarget(libraryInformation, comparisonResult);
				identificationEntry.setIdentifier(identifier);
				peak.getTargets().add(identificationEntry);
			} catch(ReferenceMustNotBeNullException e) {
				logger.warn(e);
			}
		}
	}

	private void readArea(DataInputStream dataInputStream, boolean closeStream, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws IOException {

		String integratorDescription = readString(dataInputStream); // Chromatogram Integrator Description
		List<IIntegrationEntry> chromatogramIntegrationEntries = readIntegrationEntries(dataInputStream);
		readString(dataInputStream); // Background Integrator Description
		List<IIntegrationEntry> backgroundIntegrationEntries = readIntegrationEntries(dataInputStream);
		//
		chromatogram.setIntegratedArea(chromatogramIntegrationEntries, backgroundIntegrationEntries, integratorDescription);
		//
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private void readIdentification(DataInputStream dataInputStream, boolean closeStream, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws IOException {

		int numberOfTargets = dataInputStream.readInt(); // Number of Targets
		for(int i = 1; i <= numberOfTargets; i++) {
			//
			String identifier = readString(dataInputStream); // Identifier
			//
			String casNumber = readString(dataInputStream); // CAS-Number
			String comments = readString(dataInputStream); // Comments
			String miscellaneous = readString(dataInputStream); // Miscellaneous
			String name = readString(dataInputStream); // Name
			//
			float matchFactor = dataInputStream.readFloat(); // Match Factor
			float reverseMatchFactor = dataInputStream.readFloat(); // Reverse Match Factor
			float probability = dataInputStream.readFloat(); // Probability
			//
			IChromatogramLibraryInformation libraryInformation = new ChromatogramLibraryInformation();
			libraryInformation.setCasNumber(casNumber);
			libraryInformation.setComments(comments);
			libraryInformation.setMiscellaneous(miscellaneous);
			libraryInformation.setName(name);
			IChromatogramComparisonResult comparisonResult = new ChromatogramComparisonResult(matchFactor, reverseMatchFactor, 0.0f, 0.0f, probability);
			try {
				IIdentificationTarget identificationEntry = new IdentificationTarget(libraryInformation, comparisonResult);
				identificationEntry.setIdentifier(identifier);
				chromatogram.getTargets().add(identificationEntry);
			} catch(ReferenceMustNotBeNullException e) {
				logger.warn(e);
			}
		}
		//
		//
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private void readHistory(DataInputStream dataInputStream, boolean closeStream, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws IOException {

		IEditHistory editHistory = chromatogram.getEditHistory();
		int numberOfEntries = dataInputStream.readInt(); // Number of entries
		for(int i = 1; i <= numberOfEntries; i++) {
			long time = dataInputStream.readLong(); // Date
			String description = readString(dataInputStream); // Description
			//
			Date date = new Date(time);
			IEditInformation editInformation = new EditInformation(date, description);
			editHistory.add(editInformation);
		}
		//
		//
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private void readMiscellaneous(DataInputStream dataInputStream, boolean closeStream, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws IOException {

		long time = dataInputStream.readLong(); // Date
		String miscInfo = readString(dataInputStream); // Miscellaneous Info
		String operator = readString(dataInputStream); // Operator
		//
		Date date = new Date(time);
		chromatogram.setDate(date);
		chromatogram.setMiscInfo(miscInfo);
		chromatogram.setOperator(operator);
		//
		//
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private IVendorScan readMassSpectrum(DataInputStream dataInputStream) throws IOException {

		String massSpectrometer = readString(dataInputStream); // Mass Spectrometer
		String massSpectrumType = readString(dataInputStream); // Mass Spectrum Type
		IVendorScan massSpectrum = new VendorScan();
		/*
		 * MassSpectrometer and MassSpectrumType
		 * Convert from the 0.7.0 enum format to short value.
		 */
		massSpectrum.setMassSpectrometer(getMassSpectrometerValue(massSpectrometer));
		massSpectrum.setMassSpectrumType(getMassSpectrumTypeValue(massSpectrumType));
		int retentionTime = dataInputStream.readInt();
		float retentionIndex = dataInputStream.readFloat(); // Retention Index
		massSpectrum.setRetentionTime(retentionTime);
		massSpectrum.setRetentionIndex(retentionIndex);
		int numberOfIons = dataInputStream.readInt(); // Number of ions
		for(int i = 1; i <= numberOfIons; i++) {
			/*
			 * Read Ions
			 */
			double mz = dataInputStream.readDouble(); // m/z
			float abundance = dataInputStream.readFloat(); // Abundance
			IVendorIon ion = new VendorIon(mz, abundance);
			massSpectrum.addIon(ion);
		}
		return massSpectrum;
	}

	private IPeakMassSpectrum readPeakMassSpectrum(DataInputStream dataInputStream) throws IOException {

		String massSpectrometer = readString(dataInputStream); // Mass Spectrometer
		String massSpectrumType = readString(dataInputStream); // Mass Spectrum Type
		IPeakMassSpectrum massSpectrum = new PeakMassSpectrum();
		/*
		 * MassSpectrometer and MassSpectrumType
		 * Convert from the 0.7.0 enum format to short value.
		 */
		massSpectrum.setMassSpectrometer(getMassSpectrometerValue(massSpectrometer));
		massSpectrum.setMassSpectrumType(getMassSpectrumTypeValue(massSpectrumType));
		int retentionTime = dataInputStream.readInt();
		float retentionIndex = dataInputStream.readFloat(); // Retention Index
		massSpectrum.setRetentionTime(retentionTime);
		massSpectrum.setRetentionIndex(retentionIndex);
		int numberOfIons = dataInputStream.readInt(); // Number of ions
		for(int i = 1; i <= numberOfIons; i++) {
			/*
			 * Read Ions
			 */
			double mz = dataInputStream.readDouble(); // m/z
			float abundance = dataInputStream.readFloat(); // Abundance
			IVendorIon ion = new VendorIon(mz, abundance);
			massSpectrum.addIon(ion);
		}
		return massSpectrum;
	}

	/**
	 * Legacy.
	 * Convert between Enum and short value.
	 * 
	 * @param massSpectrometer
	 * @return
	 */
	private short getMassSpectrometerValue(String massSpectrometer) {

		MassSpectrometer detector = MassSpectrometer.valueOf(massSpectrometer);
		short value = 1; // MS1
		if(detector == MassSpectrometer.MS2) {
			value = 2;
		} else if(detector == MassSpectrometer.MS3) {
			value = 3;
		}
		return value;
	}

	/**
	 * Legacy.
	 * Convert between Enum and short value.
	 * 
	 * @param massSpectrometer
	 * @return
	 */
	private MassSpectrumType getMassSpectrumTypeValue(String massSpectrumType) {

		MassSpectrumType type = MassSpectrumType.CENTROID;
		if(massSpectrumType.equals("PROFILE")) {
			type = MassSpectrumType.PROFILE;
		}
		return type;
	}

	private boolean isValidFileFormat(ZipFile zipFile) throws IOException {

		boolean isValid = false;
		DataInputStream dataInputStream = getDataInputStream(zipFile, Format.FILE_VERSION);
		String version = readString(dataInputStream);
		if(version.equals(Format.CHROMATOGRAM_VERSION_0701)) {
			isValid = true;
		}
		dataInputStream.close();
		return isValid;
	}
}
