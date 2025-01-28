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

import static org.eclipse.chemclipse.converter.io.IFileHelper.writeString;
import static org.eclipse.chemclipse.converter.io.IFileHelper.writeStringCollection;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramWriter;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.io.ChromatogramWriterCSD;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.io.IChromatogramCSDZipWriter;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakModelCSD;
import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.eclipse.chemclipse.model.columns.IRetentionIndexEntry;
import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.columns.ISeparationColumnIndices;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IIntegrationEntry;
import org.eclipse.chemclipse.model.core.IMethod;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.ISignal;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.quantitation.IInternalStandard;
import org.eclipse.chemclipse.model.quantitation.IQuantitationEntry;
import org.eclipse.chemclipse.model.ranges.TimeRange;
import org.eclipse.chemclipse.model.targets.ITargetDisplaySettings;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.io.ChromatogramWriterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.support.history.IEditHistory;
import org.eclipse.chemclipse.support.history.IEditInformation;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.io.ChromatogramWriterWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.support.RetentionIndexTypeSupport;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.settings.Format;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

/**
 * Methods are copied to ensure that file formats are kept readable even if they contain errors.
 * This is suitable but I know, it's not the best way to achieve long term support for older formats.
 */
public class ChromatogramWriter_1400 extends AbstractChromatogramWriter implements IChromatogramCSDZipWriter {

	@Override
	public void writeChromatogram(File file, IChromatogramCSD chromatogram, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		/*
		 * ZIP
		 */
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
		zipOutputStream.setLevel(PreferenceSupplier.getChromatogramCompressionLevel());
		zipOutputStream.setMethod(Format.CHROMATOGRAM_COMPRESSION_TYPE);
		/*
		 * Write the data
		 */
		writeChromatogram(zipOutputStream, "", chromatogram, monitor);
		/*
		 * Flush and close the output stream.
		 */
		zipOutputStream.flush();
		zipOutputStream.close();
	}

	@Override
	public void writeChromatogram(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram, IProgressMonitor monitor) throws IOException {

		writeVersion(zipOutputStream, directoryPrefix, monitor);
		writeChromatogramFolder(zipOutputStream, directoryPrefix, chromatogram, monitor);
		/*
		 * Referenced Chromatograms
		 */
		List<IChromatogram<?>> referencedChromatograms = chromatogram.getReferencedChromatograms();
		writeChromatogramReferenceInfo(zipOutputStream, directoryPrefix, referencedChromatograms);
		writeReferencedChromatograms(zipOutputStream, directoryPrefix, referencedChromatograms, monitor);
	}

	private void writeVersion(ZipOutputStream zipOutputStream, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		ZipEntry zipEntry;
		DataOutputStream dataOutputStream;
		/*
		 * Version
		 */
		zipEntry = new ZipEntry(directoryPrefix + Format.FILE_VERSION);
		zipOutputStream.putNextEntry(zipEntry);
		dataOutputStream = new DataOutputStream(zipOutputStream);
		String version = Format.CHROMATOGRAM_VERSION_1400;
		dataOutputStream.writeInt(version.length()); // Length Version
		dataOutputStream.writeChars(version); // Version
		//
		dataOutputStream.flush();
		zipOutputStream.closeEntry();
	}

	private void writeChromatogramFolder(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram, IProgressMonitor monitor) throws IOException {

		SubMonitor subMonitor = SubMonitor.convert(monitor, ConverterMessages.writeChromatogram, 100);
		try {
			/*
			 * Create the chromatogram folder
			 */
			ZipEntry zipEntry = new ZipEntry(directoryPrefix + Format.DIR_CHROMATOGRAM_CSD);
			zipOutputStream.putNextEntry(zipEntry);
			zipOutputStream.closeEntry();
			/*
			 * WRITE THE FILES
			 */
			writeChromatogramMethod(zipOutputStream, directoryPrefix, chromatogram);
			subMonitor.worked(20);
			writeChromatogramScans(zipOutputStream, directoryPrefix, chromatogram, subMonitor);
			writeChromatogramBaseline(zipOutputStream, directoryPrefix, chromatogram);
			subMonitor.worked(20);
			writeChromatogramPeaks(zipOutputStream, directoryPrefix, chromatogram);
			writeChromatogramArea(zipOutputStream, directoryPrefix, chromatogram);
			subMonitor.worked(20);
			writeChromatogramIdentification(zipOutputStream, directoryPrefix, chromatogram);
			writeChromatogramHistory(zipOutputStream, directoryPrefix, chromatogram);
			subMonitor.worked(20);
			writeChromatogramMiscellaneous(zipOutputStream, directoryPrefix, chromatogram);
			writeSeparationColumn(zipOutputStream, directoryPrefix, chromatogram);
			subMonitor.worked(20);
		} finally {
			SubMonitor.done(subMonitor);
		}
	}

	private void writeChromatogramMethod(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram) throws IOException {

		ZipEntry zipEntry;
		DataOutputStream dataOutputStream;
		/*
		 * Edit-History
		 */
		zipEntry = new ZipEntry(directoryPrefix + Format.FILE_SYSTEM_SETTINGS_CSD);
		zipOutputStream.putNextEntry(zipEntry);
		dataOutputStream = new DataOutputStream(zipOutputStream);
		IMethod method = chromatogram.getMethod();
		//
		writeString(dataOutputStream, method.getInstrumentName());
		writeString(dataOutputStream, method.getIonSource());
		dataOutputStream.writeDouble(method.getSamplingRate());
		dataOutputStream.writeInt(method.getSolventDelay());
		dataOutputStream.writeDouble(method.getSourceHeater());
		writeString(dataOutputStream, method.getStopMode());
		dataOutputStream.writeInt(method.getStopTime());
		dataOutputStream.writeInt(method.getTimeFilterPeakWidth());
		//
		dataOutputStream.flush();
		zipOutputStream.closeEntry();
	}

	private void writeChromatogramScans(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram, IProgressMonitor monitor) throws IOException {

		ZipEntry zipEntry;
		DataOutputStream dataOutputStream;
		/*
		 * Scans
		 */
		zipEntry = new ZipEntry(directoryPrefix + Format.FILE_SCANS_CSD);
		zipOutputStream.putNextEntry(zipEntry);
		dataOutputStream = new DataOutputStream(zipOutputStream);
		int scans = chromatogram.getNumberOfScans();
		dataOutputStream.writeInt(scans); // Number of Scans
		//
		SubMonitor subMonitor = SubMonitor.convert(monitor, ConverterMessages.writeScans, scans);
		try {
			for(int scan = 1; scan <= scans; scan++) {
				IScanCSD scanCSD = chromatogram.getSupplierScan(scan);
				//
				dataOutputStream.writeInt(scanCSD.getRetentionTime()); // Retention Time
				dataOutputStream.writeInt(scanCSD.getRelativeRetentionTime());
				dataOutputStream.writeFloat(scanCSD.getTotalSignal()); // Total Signal
				dataOutputStream.writeInt(scanCSD.getRetentionTimeColumn1());
				dataOutputStream.writeInt(scanCSD.getRetentionTimeColumn2());
				dataOutputStream.writeFloat(scanCSD.getRetentionIndex()); // Retention Index
				dataOutputStream.writeBoolean(scanCSD.hasAdditionalRetentionIndices());
				// TODO: Check (Scan) - are additional RI values stored? RetentionIndexType.POLAR, ...
				if(scanCSD.hasAdditionalRetentionIndices()) {
					Map<SeparationColumnType, Float> retentionIndicesTyped = scanCSD.getRetentionIndicesTyped();
					dataOutputStream.writeInt(retentionIndicesTyped.size());
					for(Map.Entry<SeparationColumnType, Float> retentionIndexTyped : retentionIndicesTyped.entrySet()) {
						writeString(dataOutputStream, RetentionIndexTypeSupport.getBackwardCompatibleName(retentionIndexTyped.getKey()));
						dataOutputStream.writeFloat(retentionIndexTyped.getValue());
					}
				}
				dataOutputStream.writeInt(scanCSD.getTimeSegmentId()); // Time Segment Id
				dataOutputStream.writeInt(scanCSD.getCycleNumber()); // Cycle Number
				/*
				 * Identification Results
				 */
				Set<IIdentificationTarget> scanTargets = scanCSD.getTargets();
				dataOutputStream.writeInt(scanTargets.size()); // Number Mass Spectrum Targets
				for(IIdentificationTarget scanTarget : scanTargets) {
					if(scanTarget instanceof IIdentificationTarget) {
						IIdentificationTarget identificationEntry = scanTarget;
						writeIdentificationEntry(dataOutputStream, identificationEntry);
					}
				}
				//
				subMonitor.worked(1);
			}
		} finally {
			SubMonitor.done(subMonitor);
		}
		//
		dataOutputStream.flush();
		zipOutputStream.closeEntry();
	}

	private void writeChromatogramBaseline(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram) throws IOException {

		ZipEntry zipEntry;
		DataOutputStream dataOutputStream;
		/*
		 * Baseline Models
		 */
		zipEntry = new ZipEntry(directoryPrefix + Format.FILE_BASELINE_CSD);
		zipOutputStream.putNextEntry(zipEntry);
		dataOutputStream = new DataOutputStream(zipOutputStream);
		int scans = chromatogram.getNumberOfScans();
		dataOutputStream.writeInt(scans); // Number of Scans
		Set<String> baselineIds = chromatogram.getBaselineIds();
		dataOutputStream.writeInt(baselineIds.size()); // Number of Models
		for(String baselineId : baselineIds) {
			writeString(dataOutputStream, baselineId); // Baseline Id
			chromatogram.setActiveBaseline(baselineId);
			IBaselineModel baselineModel = chromatogram.getBaselineModel();
			for(int scan = 1; scan <= scans; scan++) {
				int retentionTime = chromatogram.getSupplierScan(scan).getRetentionTime();
				float backgroundAbundance = baselineModel.getBackground(retentionTime);
				dataOutputStream.writeInt(retentionTime); // Retention Time
				dataOutputStream.writeFloat(backgroundAbundance); // Background Abundance
			}
		}
		chromatogram.setActiveBaselineDefault();
		//
		dataOutputStream.flush();
		zipOutputStream.closeEntry();
	}

	private void writeChromatogramPeaks(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram) throws IOException {

		ZipEntry zipEntry;
		DataOutputStream dataOutputStream;
		/*
		 * Peaks
		 */
		zipEntry = new ZipEntry(directoryPrefix + Format.FILE_PEAKS_CSD);
		zipOutputStream.putNextEntry(zipEntry);
		dataOutputStream = new DataOutputStream(zipOutputStream);
		List<IChromatogramPeakCSD> peaks = getPeaks(chromatogram);
		dataOutputStream.writeInt(peaks.size()); // Number of Peaks
		for(IChromatogramPeakCSD peak : peaks) {
			writePeak(dataOutputStream, peak);
		}
		//
		dataOutputStream.flush();
		zipOutputStream.closeEntry();
	}

	private List<IChromatogramPeakCSD> getPeaks(IChromatogramCSD chromatogram) {

		TimeRange timeRangeChromatogram = new TimeRange("Chromatogram", chromatogram.getStartRetentionTime(), chromatogram.getStopRetentionTime());
		List<IChromatogramPeakCSD> peaks = new ArrayList<>();
		for(IChromatogramPeakCSD peak : chromatogram.getPeaks()) {
			if(isValidPeak(peak, timeRangeChromatogram)) {
				peaks.add(peak);
			}
		}
		//
		return peaks;
	}

	private boolean isValidPeak(IChromatogramPeakCSD peak, TimeRange timeRangeChromatogram) {

		/*
		 * If scans of a region have been deleted, peaks shall be not saved, otherwise the import fails.
		 */
		IPeakModelCSD peakModel = peak.getPeakModel();
		if(!peakModel.isStrictModel()) {
			return false;
		} else if(peakModel.getStartRetentionTime() < timeRangeChromatogram.getStart() || peakModel.getStopRetentionTime() > timeRangeChromatogram.getStop()) {
			return false;
		} else if(peak.getPeakModel().getWidthByInflectionPoints() <= 0) {
			return false; // P_SKIP_PEAK_WIDTH_CHECK
		}
		//
		return true;
	}

	private void writeChromatogramArea(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram) throws IOException {

		ZipEntry zipEntry;
		DataOutputStream dataOutputStream;
		/*
		 * Area
		 */
		zipEntry = new ZipEntry(directoryPrefix + Format.FILE_AREA_CSD);
		zipOutputStream.putNextEntry(zipEntry);
		dataOutputStream = new DataOutputStream(zipOutputStream);
		//
		List<IIntegrationEntry> chromatogramIntegrationEntries = chromatogram.getChromatogramIntegrationEntries();
		writeString(dataOutputStream, chromatogram.getIntegratorDescription()); // Chromatogram Integrator Description
		writeIntegrationEntries(dataOutputStream, chromatogramIntegrationEntries);
		//
		List<IIntegrationEntry> backgroundIntegrationEntries = chromatogram.getBackgroundIntegrationEntries();
		writeString(dataOutputStream, chromatogram.getIntegratorDescription()); // Background Integrator Description
		writeIntegrationEntries(dataOutputStream, backgroundIntegrationEntries);
		//
		dataOutputStream.flush();
		zipOutputStream.closeEntry();
	}

	private void writePeak(DataOutputStream dataOutputStream, IPeakCSD peak) throws IOException {

		IPeakModelCSD peakModel = peak.getPeakModel();
		//
		writeString(dataOutputStream, peak.getDetectorDescription()); // Detector Description
		writeString(dataOutputStream, peak.getQuantifierDescription());
		dataOutputStream.writeBoolean(peak.isActiveForAnalysis());
		writeString(dataOutputStream, peak.getIntegratorDescription()); // Integrator Description
		writeString(dataOutputStream, peak.getModelDescription()); // Model Description
		writeString(dataOutputStream, peak.getPeakType().toString()); // Peak Type
		dataOutputStream.writeInt(peak.getSuggestedNumberOfComponents()); // Suggest Number Of Components
		writeString(dataOutputStream, null); // Keep this for backward compatibility 2020/09/11
		writeStringCollection(dataOutputStream, peak.getClassifier());
		//
		dataOutputStream.writeFloat(peakModel.getBackgroundAbundance(peakModel.getStartRetentionTime())); // Start Background Abundance
		dataOutputStream.writeFloat(peakModel.getBackgroundAbundance(peakModel.getStopRetentionTime())); // Stop Background Abundance
		//
		IScan scan = peakModel.getPeakMaximum();
		dataOutputStream.writeInt(scan.getRetentionTime()); // Retention Time
		dataOutputStream.writeInt(scan.getRelativeRetentionTime());
		dataOutputStream.writeFloat(scan.getTotalSignal()); // Total Signal
		dataOutputStream.writeInt(scan.getRetentionTimeColumn1());
		dataOutputStream.writeInt(scan.getRetentionTimeColumn2());
		dataOutputStream.writeFloat(scan.getRetentionIndex()); // Retention Index
		dataOutputStream.writeBoolean(scan.hasAdditionalRetentionIndices());
		if(scan.hasAdditionalRetentionIndices()) {
			Map<SeparationColumnType, Float> retentionIndicesTyped = scan.getRetentionIndicesTyped();
			dataOutputStream.writeInt(retentionIndicesTyped.size());
			for(Map.Entry<SeparationColumnType, Float> retentionIndexTyped : retentionIndicesTyped.entrySet()) {
				writeString(dataOutputStream, RetentionIndexTypeSupport.getBackwardCompatibleName(retentionIndexTyped.getKey()));
				dataOutputStream.writeFloat(retentionIndexTyped.getValue());
			}
		}
		//
		dataOutputStream.writeInt(scan.getTimeSegmentId()); // Time Segment Id
		dataOutputStream.writeInt(scan.getCycleNumber()); // Cycle Number
		//
		List<Integer> retentionTimes = peakModel.getRetentionTimes();
		dataOutputStream.writeInt(retentionTimes.size()); // Number Retention Times
		for(int retentionTime : retentionTimes) {
			dataOutputStream.writeInt(retentionTime); // Retention Time
			dataOutputStream.writeFloat(peakModel.getPeakAbundance(retentionTime)); // Intensity
		}
		//
		List<IIntegrationEntry> integrationEntries = peak.getIntegrationEntries();
		writeIntegrationEntries(dataOutputStream, integrationEntries);
		/*
		 * Identification Results
		 */
		Set<IIdentificationTarget> peakTargets = peak.getTargets();
		dataOutputStream.writeInt(peakTargets.size()); // Number Peak Targets
		for(IIdentificationTarget peakTarget : peakTargets) {
			if(peakTarget instanceof IIdentificationTarget) {
				IIdentificationTarget identificationEntry = peakTarget;
				writeIdentificationEntry(dataOutputStream, identificationEntry);
			}
		}
		/*
		 * Quantitation Results
		 */
		List<IQuantitationEntry> quantitationEntries = peak.getQuantitationEntries();
		dataOutputStream.writeInt(quantitationEntries.size()); // Number Quantitation Entries
		for(IQuantitationEntry quantitationEntry : quantitationEntries) {
			writeString(dataOutputStream, quantitationEntry.getName()); // Name
			writeString(dataOutputStream, quantitationEntry.getChemicalClass()); // Chemical Class
			dataOutputStream.writeDouble(quantitationEntry.getConcentration()); // Concentration
			writeString(dataOutputStream, quantitationEntry.getConcentrationUnit()); // Concentration Unit
			dataOutputStream.writeDouble(quantitationEntry.getArea()); // Area
			writeString(dataOutputStream, quantitationEntry.getCalibrationMethod()); // Calibration Method
			dataOutputStream.writeBoolean(quantitationEntry.getUsedCrossZero()); // Used Cross Zero
			writeString(dataOutputStream, quantitationEntry.getDescription()); // Description
			// TODO: QuantitationEntry - Quantitation Flag
			/*
			 * Legacy support
			 */
			if(quantitationEntry.getSignal() != ISignal.TOTAL_INTENSITY) {
				dataOutputStream.writeBoolean(true);
				dataOutputStream.writeDouble(quantitationEntry.getSignal());
			} else {
				dataOutputStream.writeBoolean(false);
			}
		}
		/*
		 * Internal Standards
		 */
		writeIntenalStandards(dataOutputStream, peak.getInternalStandards());
		/*
		 * Quantitation References
		 */
		List<String> quantitationReferences = peak.getQuantitationReferences();
		dataOutputStream.writeInt(quantitationReferences.size());
		for(String quantitationReference : quantitationReferences) {
			writeString(dataOutputStream, quantitationReference);
		}
	}

	private void writeIntegrationEntries(DataOutputStream dataOutputStream, List<? extends IIntegrationEntry> integrationEntries) throws IOException {

		dataOutputStream.writeInt(integrationEntries.size()); // Number Integration Entries
		for(IIntegrationEntry integrationEntry : integrationEntries) {
			dataOutputStream.writeDouble(integrationEntry.getIntegratedArea()); // Integrated Area
		}
	}

	private void writeIntenalStandards(DataOutputStream dataOutputStream, List<IInternalStandard> internalStandards) throws IOException {

		dataOutputStream.writeInt(internalStandards.size()); // size
		for(IInternalStandard internalStandard : internalStandards) {
			writeString(dataOutputStream, internalStandard.getName());
			dataOutputStream.writeDouble(internalStandard.getConcentration());
			writeString(dataOutputStream, internalStandard.getConcentrationUnit());
			dataOutputStream.writeDouble(internalStandard.getCompensationFactor());
			writeString(dataOutputStream, internalStandard.getChemicalClass());
		}
	}

	private void writeChromatogramIdentification(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram) throws IOException {

		ZipEntry zipEntry;
		DataOutputStream dataOutputStream;
		/*
		 * Identification
		 */
		zipEntry = new ZipEntry(directoryPrefix + Format.FILE_IDENTIFICATION_CSD);
		zipOutputStream.putNextEntry(zipEntry);
		dataOutputStream = new DataOutputStream(zipOutputStream);
		//
		Set<IIdentificationTarget> chromatogramTargets = chromatogram.getTargets();
		dataOutputStream.writeInt(chromatogramTargets.size()); // Number of Targets
		for(IIdentificationTarget chromatogramTarget : chromatogramTargets) {
			if(chromatogramTarget instanceof IIdentificationTarget) {
				IIdentificationTarget identificationEntry = chromatogramTarget;
				writeIdentificationEntry(dataOutputStream, identificationEntry);
			}
		}
		//
		dataOutputStream.flush();
		zipOutputStream.closeEntry();
	}

	private void writeIdentificationEntry(DataOutputStream dataOutputStream, IIdentificationTarget identificationEntry) throws IOException {

		ILibraryInformation libraryInformation = identificationEntry.getLibraryInformation();
		IComparisonResult comparisonResult = identificationEntry.getComparisonResult();
		//
		writeString(dataOutputStream, identificationEntry.getIdentifier()); // Identifier
		dataOutputStream.writeBoolean(identificationEntry.isVerified());
		/*
		 * ILibraryInformation
		 */
		dataOutputStream.writeInt(libraryInformation.getRetentionTime());
		dataOutputStream.writeFloat(libraryInformation.getRetentionIndex());
		writeString(dataOutputStream, libraryInformation.getCasNumber()); // CAS-Number
		writeString(dataOutputStream, libraryInformation.getComments()); // Comments
		writeString(dataOutputStream, libraryInformation.getReferenceIdentifier());
		writeString(dataOutputStream, libraryInformation.getMiscellaneous()); // Miscellaneous
		writeString(dataOutputStream, libraryInformation.getDatabase());
		writeString(dataOutputStream, libraryInformation.getContributor());
		writeString(dataOutputStream, libraryInformation.getName()); // Name
		Set<String> synonyms = libraryInformation.getSynonyms(); // Synonyms
		int numberOfSynonyms = synonyms.size();
		dataOutputStream.writeInt(numberOfSynonyms);
		for(String synonym : synonyms) {
			writeString(dataOutputStream, synonym);
		}
		writeString(dataOutputStream, libraryInformation.getFormula()); // Formula
		writeString(dataOutputStream, libraryInformation.getSmiles()); // SMILES
		writeString(dataOutputStream, libraryInformation.getInChI()); // InChI
		dataOutputStream.writeDouble(libraryInformation.getMolWeight()); // Mol Weight
		writeString(dataOutputStream, libraryInformation.getMoleculeStructure());
		/*
		 * IComparisonResult
		 */
		dataOutputStream.writeFloat(comparisonResult.getMatchFactor()); // Match Factor
		dataOutputStream.writeFloat(comparisonResult.getMatchFactorDirect()); // Match Factor Direct
		dataOutputStream.writeFloat(comparisonResult.getReverseMatchFactor()); // Reverse Match Factor
		dataOutputStream.writeFloat(comparisonResult.getReverseMatchFactorDirect()); // Reverse Match Factor Direct
		dataOutputStream.writeFloat(comparisonResult.getProbability()); // Probability
		dataOutputStream.writeBoolean(comparisonResult.isMatch()); // Is Match
	}

	private void writeChromatogramHistory(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram) throws IOException {

		ZipEntry zipEntry;
		DataOutputStream dataOutputStream;
		/*
		 * Edit-History
		 */
		zipEntry = new ZipEntry(directoryPrefix + Format.FILE_HISTORY_CSD);
		zipOutputStream.putNextEntry(zipEntry);
		dataOutputStream = new DataOutputStream(zipOutputStream);
		IEditHistory editHistory = chromatogram.getEditHistory();
		dataOutputStream.writeInt(editHistory.size()); // Number of entries
		// Date, Description
		for(IEditInformation editInformation : editHistory) {
			dataOutputStream.writeLong(editInformation.getDate().getTime()); // Date
			writeString(dataOutputStream, editInformation.getDescription()); // Description
		}
		//
		dataOutputStream.flush();
		zipOutputStream.closeEntry();
	}

	private void writeChromatogramMiscellaneous(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram) throws IOException {

		/*
		 * Miscellaneous
		 */
		ZipEntry zipEntry = new ZipEntry(directoryPrefix + Format.FILE_MISC_CSD);
		zipOutputStream.putNextEntry(zipEntry);
		DataOutputStream dataOutputStream = new DataOutputStream(zipOutputStream);
		//
		Map<String, String> headerData = chromatogram.getHeaderDataMap();
		dataOutputStream.writeInt(headerData.size());
		for(Map.Entry<String, String> data : headerData.entrySet()) {
			writeString(dataOutputStream, data.getKey());
			writeString(dataOutputStream, data.getValue());
		}
		/*
		 * Peak/Scan Target Label Visibility
		 */
		writeTargetDisplaySettings(dataOutputStream, chromatogram);
		//
		dataOutputStream.flush();
		zipOutputStream.closeEntry();
	}

	private void writeTargetDisplaySettings(DataOutputStream dataOutputStream, ITargetDisplaySettings targetDisplaySettings) throws IOException {

		dataOutputStream.writeBoolean(targetDisplaySettings.isShowPeakLabels());
		dataOutputStream.writeBoolean(targetDisplaySettings.isShowScanLabels());
		dataOutputStream.writeInt(targetDisplaySettings.getCollisionDetectionDepth());
		dataOutputStream.writeInt(targetDisplaySettings.getRotation());
		writeString(dataOutputStream, targetDisplaySettings.getLibraryField().name());
		Map<String, Boolean> visibilityMap = targetDisplaySettings.getVisibilityMap();
		dataOutputStream.writeInt(visibilityMap.size());
		for(Map.Entry<String, Boolean> entry : visibilityMap.entrySet()) {
			writeString(dataOutputStream, entry.getKey());
			dataOutputStream.writeBoolean(entry.getValue());
		}
	}

	private void writeSeparationColumn(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram) throws IOException {

		ZipEntry zipEntry;
		DataOutputStream dataOutputStream;
		//
		zipEntry = new ZipEntry(directoryPrefix + Format.FILE_SEPARATION_COLUMN_CSD);
		zipOutputStream.putNextEntry(zipEntry);
		dataOutputStream = new DataOutputStream(zipOutputStream);
		//
		ISeparationColumnIndices separationColumnIndices = chromatogram.getSeparationColumnIndices();
		dataOutputStream.writeInt(separationColumnIndices.size());
		for(Map.Entry<Integer, IRetentionIndexEntry> entry : separationColumnIndices.entrySet()) {
			IRetentionIndexEntry retentionIndexEntry = entry.getValue();
			writeString(dataOutputStream, retentionIndexEntry.getName());
			dataOutputStream.writeInt(retentionIndexEntry.getRetentionTime());
			dataOutputStream.writeFloat(retentionIndexEntry.getRetentionIndex());
		}
		//
		ISeparationColumn separationColumn = separationColumnIndices.getSeparationColumn();
		writeString(dataOutputStream, separationColumn.getName());
		writeString(dataOutputStream, separationColumn.getLength());
		writeString(dataOutputStream, separationColumn.getDiameter());
		writeString(dataOutputStream, separationColumn.getPhase());
		//
		dataOutputStream.flush();
		zipOutputStream.closeEntry();
	}

	private void writeChromatogramReferenceInfo(ZipOutputStream zipOutputStream, String directoryPrefix, List<IChromatogram<?>> referencedChromatograms) throws IOException {

		ZipEntry zipEntryType = new ZipEntry(directoryPrefix + Format.FILE_REFERENCE_INFO);
		zipOutputStream.putNextEntry(zipEntryType);
		DataOutputStream dataOutputStream = new DataOutputStream(zipOutputStream);
		dataOutputStream.writeInt(referencedChromatograms.size());
		zipOutputStream.closeEntry();
	}

	private void writeReferencedChromatograms(ZipOutputStream zipOutputStream, String directoryPrefix, List<IChromatogram<?>> referencedChromatograms, IProgressMonitor monitor) throws IOException {

		SubMonitor subMonitor = SubMonitor.convert(monitor, ConverterMessages.writeChromatogram, referencedChromatograms.size() * 20);
		try {
			ChromatogramWriterMSD chromatogramWriterMSD = new ChromatogramWriterMSD();
			ChromatogramWriterCSD chromatogramWriterCSD = new ChromatogramWriterCSD();
			ChromatogramWriterWSD chromatogramWriterWSD = new ChromatogramWriterWSD();
			//
			int i = 0;
			for(IChromatogram<?> referencedChromatogram : referencedChromatograms) {
				/*
				 * Create the measurement folder.
				 */
				String prefix = directoryPrefix + Format.DIR_CHROMATOGRAM_REFERENCE + Format.CHROMATOGRAM_REFERENCE_SEPARATOR + i++ + Format.DIR_SEPARATOR;
				ZipEntry zipEntryType = new ZipEntry(prefix + Format.FILE_CHROMATOGRAM_TYPE);
				zipOutputStream.putNextEntry(zipEntryType);
				DataOutputStream dataOutputStream = new DataOutputStream(zipOutputStream);
				//
				if(referencedChromatogram instanceof IChromatogramMSD referencedChromatogramMSD) {
					/*
					 * MSD
					 */
					writeString(dataOutputStream, Format.DATA_TYPE_MSD);
					dataOutputStream.flush();
					//
					prefix += Format.DIR_CHROMATOGRAM_REFERENCE + Format.DIR_SEPARATOR;
					ZipEntry zipEntryChromtogram = new ZipEntry(prefix);
					zipOutputStream.putNextEntry(zipEntryChromtogram);
					chromatogramWriterMSD.writeChromatogram(zipOutputStream, prefix, referencedChromatogramMSD, monitor);
				} else if(referencedChromatogram instanceof IChromatogramCSD referencedChromatogramCSD) {
					/*
					 * CSD
					 */
					writeString(dataOutputStream, Format.DATA_TYPE_CSD);
					dataOutputStream.flush();
					//
					prefix += Format.DIR_CHROMATOGRAM_REFERENCE + Format.DIR_SEPARATOR;
					ZipEntry zipEntryChromtogram = new ZipEntry(prefix);
					zipOutputStream.putNextEntry(zipEntryChromtogram);
					chromatogramWriterCSD.writeChromatogram(zipOutputStream, prefix, referencedChromatogramCSD, monitor);
				} else if(referencedChromatogram instanceof IChromatogramWSD referencedChromatogramWSD) {
					/*
					 * WSD
					 */
					writeString(dataOutputStream, Format.DATA_TYPE_WSD);
					dataOutputStream.flush();
					//
					prefix += Format.DIR_CHROMATOGRAM_REFERENCE + Format.DIR_SEPARATOR;
					ZipEntry zipEntryChromtogram = new ZipEntry(prefix);
					zipOutputStream.putNextEntry(zipEntryChromtogram);
					chromatogramWriterWSD.writeChromatogram(zipOutputStream, prefix, referencedChromatogramWSD, monitor);
				}
				//
				subMonitor.worked(20);
				zipOutputStream.closeEntry();
			}
		} finally {
			SubMonitor.done(subMonitor);
		}
	}
}