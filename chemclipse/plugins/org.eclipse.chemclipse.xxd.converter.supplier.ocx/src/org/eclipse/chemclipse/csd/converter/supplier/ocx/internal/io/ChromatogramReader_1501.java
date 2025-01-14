/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - adjust to API Changes
 *******************************************************************************/
package org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.eclipse.chemclipse.converter.io.IFileHelper;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.io.ChromatogramReaderCSD;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.io.IChromatogramCSDZipReader;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.model.chromatogram.IVendorChromatogram;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.model.chromatogram.IVendorScan;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.model.chromatogram.VendorChromatogram;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.model.chromatogram.VendorScan;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakModelCSD;
import org.eclipse.chemclipse.csd.model.implementation.ChromatogramPeakCSD;
import org.eclipse.chemclipse.csd.model.implementation.PeakModelCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.eclipse.chemclipse.model.columns.IRetentionIndexEntry;
import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.columns.ISeparationColumnIndices;
import org.eclipse.chemclipse.model.columns.RetentionIndexEntry;
import org.eclipse.chemclipse.model.columns.SeparationColumnFactory;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IIntegrationEntry;
import org.eclipse.chemclipse.model.core.IMethod;
import org.eclipse.chemclipse.model.core.IPeakIntensityValues;
import org.eclipse.chemclipse.model.core.PeakType;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.implementation.IntegrationEntry;
import org.eclipse.chemclipse.model.implementation.PeakIntensityValues;
import org.eclipse.chemclipse.model.implementation.QuantitationEntry;
import org.eclipse.chemclipse.model.quantitation.IInternalStandard;
import org.eclipse.chemclipse.model.quantitation.IQuantitationEntry;
import org.eclipse.chemclipse.model.quantitation.InternalStandard;
import org.eclipse.chemclipse.model.quantitation.QuantitationFlag;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.io.ChromatogramReaderMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.support.history.EditInformation;
import org.eclipse.chemclipse.support.history.IEditHistory;
import org.eclipse.chemclipse.support.history.IEditInformation;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.io.ChromatogramReaderWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.io.AbstractIO_1501;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.io.ReaderIO_1501;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.support.BaselineElement;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.support.Format;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.support.IBaselineElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

/**
 * Methods are copied to ensure that file formats are kept readable even if they contain errors.
 * This is suitable but I know, it's not the best way to achieve long term support for older formats.
 */
public class ChromatogramReader_1501 extends AbstractChromatogramReader implements IChromatogramCSDZipReader {

	private static final Logger logger = Logger.getLogger(ChromatogramReader_1501.class);
	private ReaderIO_1501 reader1501 = new ReaderIO_1501();

	@Override
	public IChromatogramCSD read(File file, IProgressMonitor monitor) throws IOException {

		IChromatogramCSD chromatogram = null;
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
				chromatogramOverview = readFromZipFile(zipFile, "", file, monitor);
			}
		}
		return chromatogramOverview;
	}

	@Override
	public IChromatogramCSD read(ZipInputStream zipInputStream, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		return readZipData(zipInputStream, directoryPrefix, null, monitor);
	}

	@Override
	public IChromatogramCSD read(ZipFile zipFile, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		return readFromZipFile(zipFile, directoryPrefix, null, monitor);
	}

	private IChromatogramCSD readFromZipFile(ZipFile zipFile, String directoryPrefix, File file, IProgressMonitor monitor) throws IOException {

		return readZipData(zipFile, directoryPrefix, file, monitor);
	}

	/*
	 * Object = ZipFile or ZipInputStream
	 * @param object
	 * @param file
	 * @return
	 */
	private IChromatogramCSD readZipData(Object object, String directoryPrefix, File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = null;
		SubMonitor subMonitor = SubMonitor.convert(monitor, ConverterMessages.readChromatogram, 100);
		//
		try {
			boolean closeStream;
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
			/*
			 * Read the chromatographic information.
			 */
			chromatogram = new VendorChromatogram();
			readMethod(getDataInputStream(object, directoryPrefix + Format.FILE_SYSTEM_SETTINGS_CSD), closeStream, chromatogram);
			readScans(getDataInputStream(object, directoryPrefix + Format.FILE_SCANS_CSD), closeStream, chromatogram);
			readBaseline(getDataInputStream(object, directoryPrefix + Format.FILE_BASELINE_CSD), closeStream, chromatogram);
			subMonitor.worked(20);
			readPeaks(getDataInputStream(object, directoryPrefix + Format.FILE_PEAKS_CSD), closeStream, chromatogram);
			readArea(getDataInputStream(object, directoryPrefix + Format.FILE_AREA_CSD), closeStream, chromatogram);
			subMonitor.worked(20);
			reader1501.readIdentificationTargets(getDataInputStream(object, directoryPrefix + Format.FILE_IDENTIFICATION_CSD), closeStream, chromatogram);
			readHistory(getDataInputStream(object, directoryPrefix + Format.FILE_HISTORY_CSD), closeStream, chromatogram);
			subMonitor.worked(20);
			readMiscellaneous(getDataInputStream(object, directoryPrefix + Format.FILE_MISC_CSD), closeStream, chromatogram);
			readSeparationColumn(getDataInputStream(object, directoryPrefix + Format.FILE_SEPARATION_COLUMN_CSD), closeStream, chromatogram);
			setAdditionalInformation(file, chromatogram);
			subMonitor.worked(20);
			//
			try {
				/*
				 * Read the referenced chromatograms.
				 * Get the size could lead to an exception if no reference info is stored.
				 */
				int size = readChromatogramReferenceInfo(getDataInputStream(object, directoryPrefix + Format.FILE_REFERENCE_INFO), closeStream);
				readReferencedChromatograms(object, directoryPrefix, chromatogram, size, closeStream, monitor);
				subMonitor.worked(20);
			} catch(IOException e) {
				logger.info(e);
			}
		} finally {
			SubMonitor.done(subMonitor);
		}
		//
		return chromatogram;
	}

	private void readMethod(DataInputStream dataInputStream, boolean closeStream, IChromatogramCSD chromatogram) throws IOException {

		IMethod method = chromatogram.getMethod();
		//
		method.setInstrumentName(readString(dataInputStream));
		method.setIonSource(readString(dataInputStream));
		method.setSamplingRate(dataInputStream.readDouble());
		method.setSolventDelay(dataInputStream.readInt());
		method.setSourceHeater(dataInputStream.readDouble());
		method.setStopMode(readString(dataInputStream));
		method.setStopTime(dataInputStream.readInt());
		method.setTimeFilterPeakWidth(dataInputStream.readInt());
		//
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private void readScans(DataInputStream dataInputStream, boolean closeStream, IChromatogramCSD chromatogram) throws IOException {

		/*
		 * Scans
		 */
		int scans = dataInputStream.readInt();
		for(int scan = 1; scan <= scans; scan++) {
			int retentionTime = dataInputStream.readInt();
			int relativeRetentionTime = dataInputStream.readInt();
			float totalSignal = dataInputStream.readFloat();
			IVendorScan scanCSD = new VendorScan(retentionTime, totalSignal);
			scanCSD.setRelativeRetentionTime(relativeRetentionTime);
			//
			int retentionTimeColumn1 = dataInputStream.readInt();
			int retentionTimeColumn2 = dataInputStream.readInt();
			float retentionIndex = dataInputStream.readFloat(); // Retention Index
			if(dataInputStream.readBoolean()) {
				int size = dataInputStream.readInt();
				for(int i = 0; i < size; i++) {
					SeparationColumnType separationColumnType = SeparationColumnFactory.getSeparationColumnType(readString(dataInputStream));
					float retentionIndexAdditional = dataInputStream.readFloat();
					scanCSD.setRetentionIndex(separationColumnType, retentionIndexAdditional);
				}
			}
			int timeSegmentId = dataInputStream.readInt();
			int cycleNumber = dataInputStream.readInt();
			//
			scanCSD.setRetentionTimeColumn1(retentionTimeColumn1);
			scanCSD.setRetentionTimeColumn2(retentionTimeColumn2);
			scanCSD.setRetentionIndex(retentionIndex);
			scanCSD.setTimeSegmentId(timeSegmentId);
			scanCSD.setCycleNumber(cycleNumber);
			/*
			 * Identification Results
			 */
			reader1501.readIdentificationTargets(dataInputStream, false, scanCSD);
			//
			chromatogram.addScan(scanCSD);
		}
		//
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private void readBaseline(DataInputStream dataInputStream, boolean closeStream, IChromatogramCSD chromatogram) throws IOException {

		/*
		 * Get the Baseline Models
		 */
		int scans = dataInputStream.readInt(); // Number of Scans
		int models = dataInputStream.readInt(); // Number of Models
		for(int i = 0; i < models; i++) {
			String baselineId = readString(dataInputStream);
			chromatogram.setActiveBaseline(baselineId); // Baseline Id
			List<IBaselineElement> baselineElements = new ArrayList<>();
			for(int scan = 1; scan <= scans; scan++) {
				int retentionTime = dataInputStream.readInt();
				float backgroundAbundance = dataInputStream.readFloat();
				IBaselineElement baselineElement = new BaselineElement(retentionTime, backgroundAbundance);
				baselineElements.add(baselineElement);
			}
			/*
			 * Set the Baseline
			 */
			IBaselineModel baselineModel = chromatogram.getBaselineModel();
			for(int index = 0; index < (scans - 1); index++) {
				/*
				 * Retention times and background abundances.
				 */
				IBaselineElement baselineElement = baselineElements.get(index);
				IBaselineElement baselineElementNext = baselineElements.get(index + 1);
				int startRetentionTime = baselineElement.getRetentionTime();
				float startBackgroundAbundance = baselineElement.getBackgroundAbundance();
				int stopRetentionTime = baselineElementNext.getRetentionTime();
				float stopBackgroundAbundance = baselineElementNext.getBackgroundAbundance();
				/*
				 * Set the baseline.
				 */
				baselineModel.addBaseline(startRetentionTime, stopRetentionTime, startBackgroundAbundance, stopBackgroundAbundance, false);
			}
		}
		chromatogram.setActiveBaselineDefault();
		//
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private void readPeaks(DataInputStream dataInputStream, boolean closeStream, IChromatogramCSD chromatogram) throws IOException {

		int numberOfPeaks = dataInputStream.readInt();
		for(int i = 1; i <= numberOfPeaks; i++) {
			try {
				IChromatogramPeakCSD peak = readPeak(dataInputStream, chromatogram);
				chromatogram.addPeak(peak);
			} catch(Exception e) {
				logger.warn(e);
			}
		}
		//
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private IChromatogramPeakCSD readPeak(DataInputStream dataInputStream, IChromatogramCSD chromatogram) throws IOException, IllegalArgumentException, PeakException {

		String detectorDescription = readString(dataInputStream); // Detector Description
		String quantifierDescription = readString(dataInputStream);
		boolean activeForAnalysis = dataInputStream.readBoolean();
		String integratorDescription = readString(dataInputStream); // Integrator Description
		String modelDescription = readString(dataInputStream); // Model Description
		PeakType peakType = PeakType.valueOf(readString(dataInputStream)); // Peak Type
		int suggestedNumberOfComponents = dataInputStream.readInt(); // Suggest Number Of Components
		readString(dataInputStream); // Keep this for backward compatibility 2020/09/11
		List<String> classifiers = IFileHelper.readStringCollection(dataInputStream);
		//
		float startBackgroundAbundance = dataInputStream.readFloat(); // Start Background Abundance
		float stopBackgroundAbundance = dataInputStream.readFloat(); // Stop Background Abundance
		//
		int retentionTime = dataInputStream.readInt();
		int relativeRetentionTime = dataInputStream.readInt();
		float totalSignalScan = dataInputStream.readFloat();
		IVendorScan peakMaximum = new VendorScan(retentionTime, totalSignalScan);
		peakMaximum.setRelativeRetentionTime(relativeRetentionTime);
		int retentionTimeColumn1 = dataInputStream.readInt();
		int retentionTimeColumn2 = dataInputStream.readInt();
		float retentionIndexScan = dataInputStream.readFloat(); // Retention Index
		if(dataInputStream.readBoolean()) {
			int size = dataInputStream.readInt();
			for(int i = 0; i < size; i++) {
				SeparationColumnType separationColumnType = SeparationColumnFactory.getSeparationColumnType(readString(dataInputStream));
				float retentionIndexAdditional = dataInputStream.readFloat();
				peakMaximum.setRetentionIndex(separationColumnType, retentionIndexAdditional);
			}
		}
		//
		int timeSegmentId = dataInputStream.readInt();
		int cycleNumber = dataInputStream.readInt();
		//
		peakMaximum.setRetentionIndex(retentionIndexScan);
		peakMaximum.setRetentionTimeColumn1(retentionTimeColumn1);
		peakMaximum.setRetentionTimeColumn2(retentionTimeColumn2);
		peakMaximum.setTimeSegmentId(timeSegmentId);
		peakMaximum.setCycleNumber(cycleNumber);
		//
		int numberOfRetentionTimes = dataInputStream.readInt(); // Number Retention Times
		IPeakIntensityValues intensityValues = new PeakIntensityValues(Float.MAX_VALUE);
		for(int i = 1; i <= numberOfRetentionTimes; i++) {
			int retentionTimePeak = dataInputStream.readInt(); // Retention Time
			float relativeIntensity = dataInputStream.readFloat(); // Intensity
			intensityValues.addIntensityValue(retentionTimePeak, relativeIntensity);
		}
		intensityValues.normalize();
		//
		IPeakModelCSD peakModel = new PeakModelCSD(peakMaximum, intensityValues, startBackgroundAbundance, stopBackgroundAbundance);
		peakModel.setStrictModel(true); // Legacy
		ChromatogramPeakCSD peak = new ChromatogramPeakCSD(peakModel, chromatogram);
		peak.setDetectorDescription(detectorDescription);
		peak.setQuantifierDescription(quantifierDescription);
		peak.setActiveForAnalysis(activeForAnalysis);
		peak.setIntegratorDescription(integratorDescription);
		peak.setModelDescription(modelDescription);
		peak.setPeakType(peakType);
		peak.setSuggestedNumberOfComponents(suggestedNumberOfComponents);
		for(String c : classifiers) {
			peak.addClassifier(c);
		}
		//
		List<IIntegrationEntry> integrationEntries = readIntegrationEntries(dataInputStream);
		peak.setIntegratedArea(integrationEntries, integratorDescription);
		/*
		 * Identification Results
		 */
		reader1501.readIdentificationTargets(dataInputStream, false, peak);
		/*
		 * Quantitation Results
		 */
		readPeakQuantitationEntries(dataInputStream, peak);
		/*
		 * Internal Standards
		 */
		List<IInternalStandard> internalStandards = readInternalStandards(dataInputStream);
		peak.addInternalStandards(internalStandards);
		/*
		 * Quantitation References
		 */
		List<String> quantitationReferences = readQuantitationReferences(dataInputStream);
		peak.addQuantitationReferences(quantitationReferences);
		//
		return peak;
	}

	private void readArea(DataInputStream dataInputStream, boolean closeStream, IChromatogramCSD chromatogram) throws IOException {

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

	@Override
	public String readString(DataInputStream dataInputStream) throws IOException {

		return IFileHelper.readString(dataInputStream);
	}

	private void setAdditionalInformation(File file, IChromatogramCSD chromatogram) {

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

	private List<IIntegrationEntry> readIntegrationEntries(DataInputStream dataInputStream) throws IOException {

		List<IIntegrationEntry> integrationEntries = new ArrayList<>();
		int numberOfIntegrationEntries = dataInputStream.readInt(); // Number Integration Entries
		for(int i = 1; i <= numberOfIntegrationEntries; i++) {
			double integratedArea = dataInputStream.readDouble(); // Integrated Area
			IIntegrationEntry integrationEntry = new IntegrationEntry(integratedArea);
			integrationEntries.add(integrationEntry);
		}
		return integrationEntries;
	}

	private List<IInternalStandard> readInternalStandards(DataInputStream dataInputStream) throws IOException {

		List<IInternalStandard> internalStandards = new ArrayList<>();
		int numberOfInternalStandards = dataInputStream.readInt();
		for(int i = 1; i <= numberOfInternalStandards; i++) {
			String name = readString(dataInputStream);
			double concentration = dataInputStream.readDouble();
			String concentrationUnit = readString(dataInputStream);
			double compensationFactor = dataInputStream.readDouble();
			String chemicalClass = readString(dataInputStream);
			IInternalStandard internalStandard = new InternalStandard(name, concentration, concentrationUnit, compensationFactor);
			internalStandard.setChemicalClass(chemicalClass);
			internalStandards.add(internalStandard);
		}
		return internalStandards;
	}

	private List<String> readQuantitationReferences(DataInputStream dataInputStream) throws IOException {

		List<String> quanitationReferences = new ArrayList<>();
		int size = dataInputStream.readInt();
		for(int i = 0; i < size; i++) {
			quanitationReferences.add(readString(dataInputStream));
		}
		//
		return quanitationReferences;
	}

	private void readHistory(DataInputStream dataInputStream, boolean closeStream, IChromatogramCSD chromatogram) throws IOException {

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
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private void readMiscellaneous(DataInputStream dataInputStream, boolean closeStream, IChromatogramCSD chromatogram) throws IOException {

		/*
		 * Header
		 */
		int numberOfEntries = dataInputStream.readInt();
		for(int i = 0; i < numberOfEntries; i++) {
			String key = readString(dataInputStream);
			String value = readString(dataInputStream);
			chromatogram.putHeaderData(key, value);
		}
		/*
		 * Miscellaneous
		 */
		reader1501.readTargetDisplaySettings(dataInputStream, chromatogram);
		chromatogram.setFinalized(dataInputStream.readBoolean());
		//
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private void readSeparationColumn(DataInputStream dataInputStream, boolean closeStream, IChromatogramCSD chromatogram) throws IOException {

		int numberOfEntries = dataInputStream.readInt();
		ISeparationColumnIndices separationColumnIndices = chromatogram.getSeparationColumnIndices();
		for(int i = 0; i < numberOfEntries; i++) {
			String name = readString(dataInputStream);
			int retentionTime = dataInputStream.readInt();
			float retentionIndex = dataInputStream.readFloat();
			IRetentionIndexEntry retentionIndexEntry = new RetentionIndexEntry(retentionTime, retentionIndex, name);
			separationColumnIndices.put(retentionIndexEntry);
		}
		//
		ISeparationColumn separationColumnSource = reader1501.readSeparationColumn(dataInputStream);
		ISeparationColumn separationColumnSink = separationColumnIndices.getSeparationColumn();
		separationColumnSource.copyFrom(separationColumnSink);
		//
		if(closeStream) {
			dataInputStream.close();
		}
	}

	private int readChromatogramReferenceInfo(DataInputStream dataInputStream, boolean closeStream) throws IOException {

		int size = dataInputStream.readInt();
		if(closeStream) {
			dataInputStream.close();
		}
		return size;
	}

	private void readReferencedChromatograms(Object object, String directoryPrefix, IChromatogramCSD chromatogram, int size, boolean closeStream, IProgressMonitor monitor) throws IOException {

		for(int i = 0; i < size; i++) {
			//
			String directory = directoryPrefix + Format.DIR_CHROMATOGRAM_REFERENCE + Format.CHROMATOGRAM_REFERENCE_SEPARATOR + i + Format.DIR_SEPARATOR;
			DataInputStream dataInputStream = getDataInputStream(object, directory + Format.FILE_CHROMATOGRAM_TYPE);
			String dataType = readString(dataInputStream);
			//
			if(closeStream) {
				dataInputStream.close();
			}
			//
			parseChromatogram(object, dataType, directory, chromatogram, monitor);
		}
	}

	private void parseChromatogram(Object object, String dataType, String directoryPrefix, IChromatogramCSD chromatogram, IProgressMonitor monitor) throws IOException {

		String directory = directoryPrefix + Format.DIR_CHROMATOGRAM_REFERENCE + Format.DIR_SEPARATOR;
		if(object instanceof ZipFile zipFile) {
			/*
			 * Chromatogram
			 */
			if(dataType.equals(Format.DATA_TYPE_MSD)) {
				ChromatogramReaderMSD chromatogramReaderMSD = new ChromatogramReaderMSD();
				IChromatogramMSD chromatogramMSD = chromatogramReaderMSD.read(zipFile, directory, monitor);
				chromatogram.addReferencedChromatogram(chromatogramMSD);
			} else if(dataType.equals(Format.DATA_TYPE_CSD)) {
				ChromatogramReaderCSD chromatogramReaderCSD = new ChromatogramReaderCSD();
				IChromatogramCSD chromatogramCSD = chromatogramReaderCSD.read(zipFile, directory, monitor);
				chromatogram.addReferencedChromatogram(chromatogramCSD);
			} else if(dataType.equals(Format.DATA_TYPE_WSD)) {
				ChromatogramReaderWSD chromatogramReaderWSD = new ChromatogramReaderWSD();
				IChromatogramWSD chromatogramWSD = chromatogramReaderWSD.read(zipFile, directory, monitor);
				chromatogram.addReferencedChromatogram(chromatogramWSD);
			}
		} else {
			/*
			 * Reading from a stream currently makes problems.
			 */
			// ZipInputStream zipInputStream = new ZipInputStream(getDataInputStream(object, directory, true));
			//
			// if(closeStream) {
			// zipInputStream.close();
			// }
		}
	}

	private void readPeakQuantitationEntries(DataInputStream dataInputStream, IPeakCSD peak) throws IOException {

		int numberOfQuantitationEntries = dataInputStream.readInt(); // Number Quantitation Entries
		for(int i = 1; i <= numberOfQuantitationEntries; i++) {
			//
			String name = readString(dataInputStream); // Name
			String chemicalClass = readString(dataInputStream); // Chemical Class
			double concentration = dataInputStream.readDouble(); // Concentration
			String concentrationUnit = readString(dataInputStream); // Concentration Unit
			double area = dataInputStream.readDouble(); // Area
			String calibrationMethod = readString(dataInputStream); // Calibration Method
			boolean usedCrossZero = dataInputStream.readBoolean(); // Used Cross Zero
			String description = readString(dataInputStream); // Description
			QuantitationFlag quantitationFlag = getQuantitationFlag(readString(dataInputStream)); // Flag
			String group = readString(dataInputStream);
			/*
			 * Signals
			 */
			List<Double> signals = new ArrayList<>();
			int sizeSignals = dataInputStream.readInt();
			for(int j = 0; j < sizeSignals; j++) {
				signals.add(dataInputStream.readDouble());
			}
			//
			IQuantitationEntry quantitationEntry = new QuantitationEntry(name, group, concentration, concentrationUnit, area);
			quantitationEntry.setSignals(signals);
			quantitationEntry.setChemicalClass(chemicalClass);
			quantitationEntry.setCalibrationMethod(calibrationMethod);
			quantitationEntry.setUsedCrossZero(usedCrossZero);
			quantitationEntry.setDescription(description);
			quantitationEntry.setQuantitationFlag(quantitationFlag);
			//
			peak.addQuantitationEntry(quantitationEntry);
		}
	}

	private QuantitationFlag getQuantitationFlag(String value) {

		try {
			return QuantitationFlag.valueOf(value);
		} catch(Exception e) {
			return QuantitationFlag.NONE;
		}
	}

	private boolean isValidFileFormat(ZipFile zipFile) throws IOException {

		boolean isValid = false;
		DataInputStream dataInputStream = getDataInputStream(zipFile, Format.FILE_VERSION);
		String version = readString(dataInputStream);
		if(version.equals(AbstractIO_1501.VERSION)) {
			isValid = true;
		}
		//
		dataInputStream.close();
		//
		return isValid;
	}
}