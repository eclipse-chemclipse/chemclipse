/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - extract common methods to base class
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.csd.peak.detector.supplier.firstderivative.core;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.csd.peak.detector.core.IPeakDetectorCSD;
import org.eclipse.chemclipse.chromatogram.csd.peak.detector.settings.IPeakDetectorSettingsCSD;
import org.eclipse.chemclipse.chromatogram.csd.peak.detector.supplier.firstderivative.Activator;
import org.eclipse.chemclipse.chromatogram.csd.peak.detector.supplier.firstderivative.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.csd.peak.detector.supplier.firstderivative.settings.PeakDetectorSettingsCSD;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.ValueMustNotBeNullException;
import org.eclipse.chemclipse.chromatogram.peak.detector.model.Threshold;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.IRawPeak;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.core.noise.NoiseChromatogramSupport;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.core.BasePeakDetector;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.model.DetectorType;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.support.FirstDerivativeDetectorSlope;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.support.FirstDerivativeDetectorSlopes;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.support.IFirstDerivativeDetectorSlope;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.support.IFirstDerivativeDetectorSlopes;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramPeakCSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.csd.model.core.support.PeakBuilderCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.PeakType;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalsModifier;
import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.eclipse.chemclipse.numeric.equations.Equations;
import org.eclipse.chemclipse.numeric.equations.LinearEquation;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.chemclipse.support.l10n.TranslationSupport;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.e4.core.services.translation.TranslationService;

public class PeakDetectorCSD extends BasePeakDetector implements IPeakDetectorCSD {

	private static final Logger logger = Logger.getLogger(PeakDetectorCSD.class);

	@Override
	public IProcessingInfo<?> detect(IChromatogramSelectionCSD chromatogramSelection, IPeakDetectorSettingsCSD detectorSettings, IProgressMonitor monitor) {

		/*
		 * Check whether the chromatogram selection is null or not.
		 */
		IProcessingInfo<?> processingInfo = validate(chromatogramSelection, detectorSettings, monitor);
		if(!processingInfo.hasErrorMessages()) {
			if(detectorSettings instanceof PeakDetectorSettingsCSD peakDetectorSettings) {
				SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
				IChromatogramCSD chromatogram = chromatogramSelection.getChromatogram();
				/*
				 * Extract the noise segments.
				 */
				List<INoiseSegment> noiseSegments = null;
				if(peakDetectorSettings.isUseNoiseSegments()) {
					noiseSegments = NoiseChromatogramSupport.getNoiseSegments(chromatogram, chromatogramSelection, false, subMonitor.split(10));
				}
				/*
				 * Detect and add the peaks.
				 */
				List<IChromatogramPeakCSD> peaks = detectPeaks(chromatogramSelection, peakDetectorSettings, noiseSegments, subMonitor.split(90));
				for(IChromatogramPeakCSD peak : peaks) {
					chromatogram.getPeaks().add(peak);
				}
				chromatogramSelection.getChromatogram().setDirty(true);
				TranslationService translationService = TranslationSupport.getTranslationService();
				String peakDetectedMessage = MessageFormat.format(translationService.translate("%PeaksDetected", Activator.getContributorURI()), String.valueOf(peaks.size()));
				processingInfo.addMessage(new ProcessingMessage(MessageType.INFO, translationService.translate("%FirstDerivative", Activator.getContributorURI()), peakDetectedMessage));
			} else {
				logger.warn("Settings is not of type: " + PeakDetectorSettingsCSD.class);
			}
		}
		return processingInfo;
	}

	@Override
	public IProcessingInfo<?> detect(IChromatogramSelectionCSD chromatogramSelection, IProgressMonitor monitor) {

		PeakDetectorSettingsCSD peakDetectorSettings = PreferenceSupplier.getPeakDetectorSettingsCSD();
		chromatogramSelection.getChromatogram().setDirty(true);
		return detect(chromatogramSelection, peakDetectorSettings, monitor);
	}

	/**
	 * Use this method if peaks shall be detected without adding them to the chromatogram.
	 * Detect the peaks in the selection.
	 * This method does not add the peaks to the chromatogram.
	 * It needs to be handled separately.
	 * 
	 * @param chromatogramSelection
	 * @throws ValueMustNotBeNullException
	 */
	public List<IChromatogramPeakCSD> detectPeaks(IChromatogramSelectionCSD chromatogramSelection, PeakDetectorSettingsCSD peakDetectorSettings, IProgressMonitor monitor) {

		chromatogramSelection.getChromatogram().setDirty(true);
		return detectPeaks(chromatogramSelection, peakDetectorSettings, null, monitor);
	}

	/**
	 * Additionally, noise segments are used if not null.
	 */
	public List<IChromatogramPeakCSD> detectPeaks(IChromatogramSelectionCSD chromatogramSelection, PeakDetectorSettingsCSD peakDetectorSettings, List<INoiseSegment> noiseSegments, IProgressMonitor monitor) {

		Threshold threshold = peakDetectorSettings.getThreshold();
		int windowSize = peakDetectorSettings.getMovingAverageWindowSize();
		List<IRawPeak> rawPeaks = new ArrayList<>();
		//
		if(noiseSegments != null && !noiseSegments.isEmpty()) {
			/*
			 * Initial retention time range before running the detection using
			 * noise segments.
			 * | --- [S] --- [N] --- [E] --- |
			 */
			Iterator<INoiseSegment> iterator = noiseSegments.iterator();
			int startRetentionTime = chromatogramSelection.getStartRetentionTime();
			int stopRetentionTime = chromatogramSelection.getStopRetentionTime();
			INoiseSegment noiseSegment = iterator.hasNext() ? iterator.next() : null;
			/*
			 * Range from the start of the chromatogram selection to the first noise segment
			 * | --- [S]
			 */
			if(noiseSegment != null) {
				chromatogramSelection.setRangeRetentionTime(startRetentionTime, noiseSegment.getStartRetentionTime());
				IFirstDerivativeDetectorSlopes slopes = getFirstDerivativeSlopes(chromatogramSelection, windowSize);
				rawPeaks.addAll(getRawPeaks(slopes, threshold, monitor));
			}
			/*
			 * Ranges between the noise segments
			 * [S] --- [N] --- [E]
			 */
			while(iterator.hasNext()) {
				int startRetentionTimeSegment = noiseSegment.getStopRetentionTime();
				noiseSegment = iterator.next();
				int stopRetentionTimeSegment = noiseSegment.getStartRetentionTime();
				chromatogramSelection.setRangeRetentionTime(startRetentionTimeSegment, stopRetentionTimeSegment);
				IFirstDerivativeDetectorSlopes slopes = getFirstDerivativeSlopes(chromatogramSelection, windowSize);
				rawPeaks.addAll(getRawPeaks(slopes, threshold, monitor));
			}
			/*
			 * Range from the last noise segment to the end of the chromatogram selection
			 * [E] --- |
			 */
			if(noiseSegment != null) {
				chromatogramSelection.setRangeRetentionTime(noiseSegment.getStopRetentionTime(), stopRetentionTime);
				IFirstDerivativeDetectorSlopes slopes = getFirstDerivativeSlopes(chromatogramSelection, windowSize);
				rawPeaks.addAll(getRawPeaks(slopes, threshold, monitor));
			}
			/*
			 * Reset the retention time range to its initial values.
			 */
			chromatogramSelection.setRangeRetentionTime(startRetentionTime, stopRetentionTime);
		} else {
			/*
			 * Default: no noise segments
			 */
			IFirstDerivativeDetectorSlopes slopes = getFirstDerivativeSlopes(chromatogramSelection, windowSize);
			rawPeaks.addAll(getRawPeaks(slopes, threshold, monitor));
		}
		chromatogramSelection.getChromatogram().setDirty(true);
		return extractPeaks(rawPeaks, chromatogramSelection.getChromatogram(), peakDetectorSettings);
	}

	/**
	 * Builds from each raw peak a valid {@link IChromatogramPeakCSD} and adds it to the
	 * chromatogram.
	 * 
	 * @param rawPeaks
	 * @param chromatogram
	 * @return List<IChromatogramPeakCSD>
	 */
	private List<IChromatogramPeakCSD> extractPeaks(List<IRawPeak> rawPeaks, IChromatogramCSD chromatogram, PeakDetectorSettingsCSD peakDetectorSettings) {

		List<IChromatogramPeakCSD> peaks = new ArrayList<>();
		DetectorType detectorType = peakDetectorSettings.getDetectorType();
		boolean optimizeBaseline = peakDetectorSettings.isOptimizeBaseline();
		//
		for(IRawPeak rawPeak : rawPeaks) {
			/*
			 * Build the peak and add it.
			 */
			try {
				/*
				 * Optimize the scan range.
				 */
				ScanRange scanRange = new ScanRange(rawPeak.getStartScan(), rawPeak.getStopScan());
				if(isValleyOption(detectorType) && optimizeBaseline) {
					scanRange = optimizeBaseline(chromatogram, scanRange.getStartScan(), rawPeak.getMaximumScan(), scanRange.getStopScan());
				}
				/*
				 * Detector Type
				 */
				IChromatogramPeakCSD peak = null;
				switch(detectorType) {
					case BB:
						peak = PeakBuilderCSD.createPeak(chromatogram, scanRange, PeakType.BB);
						break;
					case CB:
						peak = PeakBuilderCSD.createPeak(chromatogram, scanRange, PeakType.CB);
						break;
					default:
						peak = PeakBuilderCSD.createPeak(chromatogram, scanRange, PeakType.VV);
						break;
				}
				/*
				 * Validate
				 * Add the detector description.
				 */
				if(isValidPeak(peak, peakDetectorSettings)) {
					TranslationService translationService = TranslationSupport.getTranslationService();
					peak.setDetectorDescription(translationService.translate("%FirstDerivative", Activator.getContributorURI()));
					peaks.add(peak);
				}
			} catch(IllegalArgumentException e) {
				logger.warn(e);
			} catch(PeakException e) {
				logger.warn(e);
			}
		}
		chromatogram.setDirty(true);
		return peaks;
	}

	/**
	 * Initializes the slope values.
	 * 
	 * @param chromatogramSelection
	 * @param window
	 * @return {@link IFirstDerivativeDetectorSlopes}
	 */
	public static IFirstDerivativeDetectorSlopes getFirstDerivativeSlopes(IChromatogramSelectionCSD chromatogramSelection, int windowSize) {

		ITotalScanSignals signals = new TotalScanSignals(chromatogramSelection);
		TotalScanSignalsModifier.normalize(signals, NORMALIZATION_BASE);
		/*
		 * Get the start and stop scan of the chromatogram selection.
		 */
		IFirstDerivativeDetectorSlopes slopes = new FirstDerivativeDetectorSlopes(signals);
		/*
		 * Fill the slope list.
		 */
		int startScan = signals.getStartScan();
		int stopScan = signals.getStopScan();
		//
		for(int scan = startScan; scan < stopScan; scan++) {
			ITotalScanSignal s1 = signals.getTotalScanSignal(scan);
			ITotalScanSignal s2 = signals.getNextTotalScanSignal(scan);
			if(s1 != null && s2 != null) {
				IPoint p1 = new Point(s1.getRetentionTime(), s1.getTotalSignal());
				IPoint p2 = new Point(s2.getRetentionTime(), s2.getTotalSignal());
				IFirstDerivativeDetectorSlope slope = new FirstDerivativeDetectorSlope(p1, p2, s1.getRetentionTime());
				slopes.add(slope);
			}
		}
		/*
		 * Moving average on the slopes
		 */
		if(windowSize != 0) {
			slopes.calculateMovingAverage(windowSize);
		}
		//
		return slopes;
	}

	/**
	 * Checks that the peak is not null and that it matches
	 * the min S/N requirements.
	 * 
	 * @param peak
	 * @return boolean
	 */
	private boolean isValidPeak(IChromatogramPeakCSD peak, PeakDetectorSettingsCSD peakDetectorSettings) {

		return (peak != null && peak.getSignalToNoiseRatio() >= peakDetectorSettings.getMinimumSignalToNoiseRatio());
	}

	private ScanRange optimizeBaseline(IChromatogramCSD chromatogram, int startScan, int centerScan, int stopScan) {

		int stopScanOptimized = optimizeRightBaseline(chromatogram, startScan, centerScan, stopScan);
		int startScanOptimized = optimizeLeftBaseline(chromatogram, startScan, centerScan, stopScanOptimized);
		//
		return new ScanRange(startScanOptimized, stopScanOptimized);
	}

	private int optimizeRightBaseline(IChromatogramCSD chromatogram, int startScan, int centerScan, int stopScan) {

		IPoint p1 = new Point(getRetentionTime(chromatogram, startScan), getScanSignal(chromatogram, startScan));
		IPoint p2 = new Point(getRetentionTime(chromatogram, stopScan), getScanSignal(chromatogram, stopScan));
		LinearEquation backgroundEquation = Equations.createLinearEquation(p1, p2);
		/*
		 * Right border optimization
		 */
		int stopScanOptimized = stopScan;
		for(int i = stopScan; i > centerScan; i--) {
			float signal = getScanSignal(chromatogram, i);
			int retentionTime = chromatogram.getScan(i).getRetentionTime();
			if(signal < backgroundEquation.calculateY(retentionTime)) {
				stopScanOptimized = i;
			}
		}
		//
		return stopScanOptimized;
	}

	private int optimizeLeftBaseline(IChromatogramCSD chromatogram, int startScan, int centerScan, int stopScan) {

		IPoint p1 = new Point(getRetentionTime(chromatogram, startScan), getScanSignal(chromatogram, startScan));
		IPoint p2 = new Point(getRetentionTime(chromatogram, stopScan), getScanSignal(chromatogram, stopScan));
		LinearEquation backgroundEquation = Equations.createLinearEquation(p1, p2);
		/*
		 * Right border optimization
		 */
		int startScanOptimized = startScan;
		for(int i = startScan; i < centerScan; i++) {
			float signal = getScanSignal(chromatogram, i);
			int retentionTime = chromatogram.getScan(i).getRetentionTime();
			if(signal < backgroundEquation.calculateY(retentionTime)) {
				/*
				 * Create a new equation
				 */
				startScanOptimized = i;
				p1 = new Point(getRetentionTime(chromatogram, startScanOptimized), getScanSignal(chromatogram, startScanOptimized));
				p2 = new Point(getRetentionTime(chromatogram, stopScan), getScanSignal(chromatogram, stopScan));
				backgroundEquation = Equations.createLinearEquation(p1, p2);
			}
		}
		//
		return startScanOptimized;
	}

	protected float getScanSignal(IChromatogramCSD chromatogram, int scanNumber) {

		IScan scan = chromatogram.getScan(scanNumber);
		return scan.getTotalSignal();
	}

	private boolean isValleyOption(DetectorType detectorType) {

		return DetectorType.VV.equals(detectorType);
	}
}