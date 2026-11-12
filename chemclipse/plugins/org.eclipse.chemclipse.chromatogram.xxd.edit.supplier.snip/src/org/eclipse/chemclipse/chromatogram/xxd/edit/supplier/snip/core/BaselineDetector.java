/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - use static {@link SnipCalculator} method, remove warnings and make method static for reuse
 * Matthias Mailänder - remove the window size enum
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.edit.supplier.snip.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.core.AbstractBaselineDetector;
import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.settings.IBaselineDetectorSettings;
import org.eclipse.chemclipse.chromatogram.xxd.edit.supplier.snip.calculator.SnipCalculator;
import org.eclipse.chemclipse.chromatogram.xxd.edit.supplier.snip.settings.BaselineDetectorSettings;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceEmpty;
import org.eclipse.chemclipse.support.traces.TraceFactory;
import org.eclipse.chemclipse.support.traces.TraceGeneric;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.eclipse.chemclipse.support.traces.TraceRasteredWSD;
import org.eclipse.chemclipse.support.validators.TraceValidator;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.chemclipse.wsd.model.xwc.IExtractedWavelengthSignal;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;

public class BaselineDetector extends AbstractBaselineDetector {

	@Override
	public IProcessingInfo<?> setBaseline(IChromatogramSelection chromatogramSelection, IBaselineDetectorSettings baselineDetectorSettings, IProgressMonitor monitor) {

		IProcessingInfo<?> processingInfo = super.validate(chromatogramSelection, baselineDetectorSettings, monitor);
		if(!processingInfo.hasErrorMessages()) {
			if(baselineDetectorSettings instanceof BaselineDetectorSettings settings) {
				calculateBaseline(chromatogramSelection, settings, monitor);
			}
		}
		return processingInfo;
	}

	/**
	 * Calculates the baseline.
	 */
	public static void calculateBaseline(IChromatogramSelection chromatogramSelection, BaselineDetectorSettings detectorSettings, IProgressMonitor monitor) {

		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		int startScan = chromatogram.getScanNumber(chromatogramSelection.getStartRetentionTime());
		int stopScan = chromatogram.getScanNumber(chromatogramSelection.getStopRetentionTime());
		IScanRange scanRange = new ScanRange(startScan, stopScan);
		/*
		 * Iterations
		 */
		int iterations = detectorSettings.getIterations();
		int windowSize = detectorSettings.getWindowSize();
		/*
		 * If the scan range is lower than the given window size, do nothing.
		 */
		if(windowSize == 0 || scanRange.getWidth() <= windowSize) {
			return;
		}
		/*
		 * Calculate the Baseline
		 */
		try {
			String specificTraces = detectorSettings.getSpecificTraces();
			if(!specificTraces.isBlank()) {
				if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
					Set<Integer> ions = getUnitResolutionTraces(specificTraces);
					if(!ions.isEmpty()) {
						Map<Integer, ITotalScanSignals> totalScanSignalsMap = getExtractedIonSignals(chromatogramMSD, startScan, stopScan, ions);
						SubMonitor subMonitor = SubMonitor.convert(monitor, ions.size());
						for(int ion : ions) {
							TraceNominalMSD traceNominalMSD = new TraceNominalMSD();
							traceNominalMSD.setMZ(ion);
							ITotalScanSignals totalScanSignals = totalScanSignalsMap.get(ion);
							applyBaseline(totalScanSignals, chromatogram, startScan, stopScan, iterations, traceNominalMSD, subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE));
						}
					} else {
						applyBaselineTIC(chromatogramMSD, startScan, stopScan, iterations, monitor);
					}
				} else if(chromatogram instanceof IChromatogramWSD chromatogramWSD) {
					Set<Integer> wavelengths = getUnitResolutionTraces(specificTraces);
					if(!wavelengths.isEmpty()) {
						Map<Integer, ITotalScanSignals> totalScanSignalsMap = getExtractedWavelengthSignals(chromatogramWSD, startScan, stopScan, wavelengths);
						SubMonitor subMonitor = SubMonitor.convert(monitor, wavelengths.size());
						for(int wavelength : wavelengths) {
							TraceRasteredWSD traceRasteredWSD = new TraceRasteredWSD();
							traceRasteredWSD.setWavelength(wavelength);
							ITotalScanSignals totalScanSignals = totalScanSignalsMap.get(wavelength);
							applyBaseline(totalScanSignals, chromatogram, startScan, stopScan, iterations, traceRasteredWSD, subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE));
						}
					} else {
						applyBaselineTIC(chromatogramWSD, startScan, stopScan, iterations, monitor);
					}
				}
			} else {
				applyBaselineTIC(chromatogram, startScan, stopScan, iterations, monitor);
			}
		} catch(ChromatogramIsNullException e) {
			return;
		}
	}

	private static void applyBaselineTIC(IChromatogram chromatogram, int startScan, int stopScan, int iterations, IProgressMonitor monitor) {

		ITotalScanSignalExtractor totalScanSignalExtractor = new TotalScanSignalExtractor(chromatogram);
		ITotalScanSignals totalScanSignals = totalScanSignalExtractor.getTotalScanSignals(startScan, stopScan);
		applyBaseline(totalScanSignals, chromatogram, startScan, stopScan, iterations, new TraceEmpty(), monitor);
	}

	private static void applyBaseline(ITotalScanSignals totalScanSignals, IChromatogram chromatogram, int startScan, int stopScan, int iterations, ITrace trace, IProgressMonitor monitor) {

		calculate(totalScanSignals, iterations);
		IBaselineModel baselineModel = chromatogram.getBaselineModel();
		apply(baselineModel, totalScanSignals, startScan, stopScan, trace, monitor);
	}

	private static Map<Integer, ITotalScanSignals> getExtractedIonSignals(IChromatogramMSD chromatogramMSD, int startScan, int stopScan, Set<Integer> ions) {

		Map<Integer, ITotalScanSignals> totalScanSignalsMap = new HashMap<>();
		for(int ion : ions) {
			totalScanSignalsMap.put(ion, new TotalScanSignals(startScan, stopScan));
		}
		/*
		 * Collect
		 */
		for(int i = startScan; i <= stopScan; i++) {
			if(chromatogramMSD.getScan(i) instanceof IScanMSD scanMSD) {
				IExtractedIonSignal extractedIonSignal = scanMSD.getExtractedIonSignal();
				int retentionTime = scanMSD.getRetentionTime();
				float retentionIndex = scanMSD.getRetentionIndex();
				for(int ion : ions) {
					ITotalScanSignals totalScanSignals = totalScanSignalsMap.get(ion);
					double totalSignal = extractedIonSignal.getAbundance(ion);
					totalScanSignals.add(new TotalScanSignal(retentionTime, retentionIndex, (float)totalSignal, true));
				}
			}
		}

		return totalScanSignalsMap;
	}

	private static Map<Integer, ITotalScanSignals> getExtractedWavelengthSignals(IChromatogramWSD chromatogramWSD, int startScan, int stopScan, Set<Integer> wavelengths) {

		Map<Integer, ITotalScanSignals> totalScanSignalsMap = new HashMap<>();
		for(int wavelength : wavelengths) {
			totalScanSignalsMap.put(wavelength, new TotalScanSignals(startScan, stopScan));
		}
		/*
		 * Collect
		 */
		for(int i = startScan; i <= stopScan; i++) {
			if(chromatogramWSD.getScan(i) instanceof IScanWSD scanWSD) {
				IExtractedWavelengthSignal extractedWavelengthSignal = scanWSD.getExtractedWavelengthSignal();
				int retentionTime = scanWSD.getRetentionTime();
				float retentionIndex = scanWSD.getRetentionIndex();
				for(int wavelength : wavelengths) {
					ITotalScanSignals totalScanSignals = totalScanSignalsMap.get(wavelength);
					double totalSignal = extractedWavelengthSignal.getAbundance(wavelength);
					totalScanSignals.add(new TotalScanSignal(retentionTime, retentionIndex, (float)totalSignal, true));
				}
			}
		}

		return totalScanSignalsMap;
	}

	private static Set<Integer> getUnitResolutionTraces(String traces) {

		Set<Integer> unitResolutionTraces = new HashSet<>();
		TraceValidator traceValidator = new TraceValidator();
		IStatus status = traceValidator.validate(traces);
		if(status.isOK()) {
			/*
			 * m/z or wavelength (nominal)
			 */
			for(TraceGeneric traceGeneric : TraceFactory.parseTraces(traceValidator.getTraces(), TraceGeneric.class)) {
				unitResolutionTraces.add(traceGeneric.getTrace());
			}
		}

		return unitResolutionTraces;
	}

	private static void calculate(ITotalScanSignals totalIonSignals, int iterations) {

		int size = totalIonSignals.size();
		float[] intensityValues = new float[size];
		int counter = 0;
		for(ITotalScanSignal signal : totalIonSignals.getTotalScanSignals()) {
			intensityValues[counter++] = signal.getTotalSignal();
		}
		SnipCalculator.calculateBaselineIntensityValues(intensityValues, iterations);
		/*
		 * Set the calculated values.
		 */
		counter = 0;
		for(ITotalScanSignal signal : totalIonSignals.getTotalScanSignals()) {
			signal.setTotalSignal(intensityValues[counter++]);
		}
	}

	private static void apply(IBaselineModel baselineModel, ITotalScanSignals totalIonSignals, int startScan, int stopScan, ITrace trace, IProgressMonitor monitor) {

		ITotalScanSignal actualTotalIonSignal;
		ITotalScanSignal nextTotalIonSignal;

		int size = stopScan - startScan;
		SubMonitor subMonitor = SubMonitor.convert(monitor, "Apply baseline", size);
		try {
			/*
			 * Why scan < numberOfScans instead of scan <= numberOfScans?
			 * Because of .getNextTotalIonSignal();
			 */
			for(int scan = startScan; scan < stopScan; scan++) {

				actualTotalIonSignal = totalIonSignals.getTotalScanSignal(scan);
				nextTotalIonSignal = totalIonSignals.getNextTotalScanSignal(scan);
				/*
				 * Retention times and background abundances.
				 */
				int startRetentionTime = actualTotalIonSignal.getRetentionTime();
				float startBackgroundAbundance = actualTotalIonSignal.getTotalSignal();
				int stopRetentionTime = nextTotalIonSignal.getRetentionTime();
				float stopBackgroundAbundance = nextTotalIonSignal.getTotalSignal();
				/*
				 * Set the baseline.
				 * It is validate == false, cause we know that the segments are calculated without overlap.
				 */
				baselineModel.addBaseline(startRetentionTime, stopRetentionTime, startBackgroundAbundance, stopBackgroundAbundance, trace, false);
				subMonitor.worked(1);
			}
		} finally {
			SubMonitor.done(subMonitor);
		}
	}
}