/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - extracted from WSD/CSD/MSD variants
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.peak.detector.core.AbstractPeakDetector;
import org.eclipse.chemclipse.chromatogram.peak.detector.model.Threshold;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.IDetectorSlope;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.IRawPeak;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.RawPeak;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.support.IFirstDerivativeDetectorSlopes;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.numeric.miscellaneous.Evaluation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class BasePeakDetector extends AbstractPeakDetector {

	protected static final float NORMALIZATION_BASE = 100000.0f;
	protected static final int CONSECUTIVE_SCAN_STEPS = 3;

	/**
	 * Marks the peaks with start, stop and max.
	 * 
	 * @param slopeList
	 */
	public static List<IRawPeak> getRawPeaks(IFirstDerivativeDetectorSlopes slopes, Threshold thresholdSetting, IProgressMonitor monitor) {

		double threshold;
		switch(thresholdSetting) {
			case OFF:
				threshold = 0.0005d;
				break;
			case LOW:
				threshold = 0.005d;
				break;
			case MEDIUM:
				threshold = 0.05d;
				break;
			case HIGH:
				threshold = 0.5d;
				break;
			default:
				threshold = 0.005d;
				break;
		}
		/*
		 * It should be also possible to detect peaks in a selected retention
		 * time area of the chromatogram.<br/> The value for scan in the for
		 * loop is by default 1 (detector array), but the slopes are storing
		 * start and end point of selection (scans).<br/> E.g. the selection is
		 * from scan 850 to scan 1000, then the loop starts at >
		 * slopes.getDetectorSlope(1 + 849);
		 */
		int size = slopes.size();
		int scanOffset = slopes.getStartScan() - 1;
		IRawPeak rawPeak;
		List<IRawPeak> rawPeaks = new ArrayList<>();
		int limit = size - CONSECUTIVE_SCAN_STEPS;
		SubMonitor subMonitor = SubMonitor.convert(monitor, limit);
		for(int i = 1; i <= limit; i++) {
			/*
			 * Get the scan numbers without offset.<br/> Why? To not get out of
			 * borders of the slopes list.
			 */
			int peakStart = detectPeakStart(slopes, i, scanOffset, threshold);
			int peakMaximum = detectPeakMaximum(slopes, peakStart, scanOffset);
			int peakStop = detectPeakStop(slopes, peakMaximum, scanOffset);
			/*
			 * Begin the detection of the next peak at the end of the actual
			 * peak.
			 */
			i = peakStop;
			/*
			 * Adjust the peak to their real positions (scan numbers) in the
			 * chromatogram.<br/> Keep in mind, the slopes list starts at
			 * position and not at the position of the scan.
			 */
			peakStart += scanOffset;
			peakMaximum += scanOffset;
			peakStop += scanOffset;
			//
			rawPeak = new RawPeak(peakStart, peakMaximum, peakStop);
			if(isValidRawPeak(rawPeak)) {
				rawPeaks.add(rawPeak);
			}
			subMonitor.worked(1);
		}
		return rawPeaks;
	}

	protected int getRetentionTime(IChromatogram chromatogram, int scanNumber) {

		return chromatogram.getScan(scanNumber).getRetentionTime();
	}

	/**
	 * Checks if the peak is a valid raw peak.<br/>
	 * For example if it contains not less than the needed amount of scans.
	 * 
	 * @param rawPeak
	 * @return boolean
	 */
	private static boolean isValidRawPeak(IRawPeak rawPeak) {

		boolean isValid = false;
		int width = rawPeak.getStopScan() - rawPeak.getStartScan() + 1;
		if(width >= IPeakModel.MINIMUM_SCANS) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * Detects the peak start.
	 * 
	 * @param slope
	 * @param startScan
	 * @param scanOffset
	 * @return int
	 */
	private static int detectPeakStart(IFirstDerivativeDetectorSlopes slopes, int startScan, int scanOffset, double threshold) {

		int size = slopes.size();
		int peakStart = size - 1;
		IDetectorSlope slope;
		double[] values = new double[CONSECUTIVE_SCAN_STEPS];
		exitloop:
		for(int scan = startScan; scan <= size - CONSECUTIVE_SCAN_STEPS; scan++) {
			slope = slopes.getDetectorSlope(scan + scanOffset);
			if(slope.getSlope() > threshold) {
				/*
				 * Get the actual and the next slope values.
				 */
				for(int j = 0; j < CONSECUTIVE_SCAN_STEPS; j++) {
					values[j] = slopes.getDetectorSlope(scan + j + scanOffset).getSlope();
				}
				if(Evaluation.valuesAreGreaterThanThreshold(values, threshold) && Evaluation.valuesAreIncreasing(values)) {
					peakStart = scan;
					break exitloop;
				}
			}
		}
		return peakStart;
	}

	/**
	 * Detects the peak maxima.<br/>
	 * The peak start and stops needs to be detected previously.
	 * 
	 * @param slope
	 * @param startScan
	 * @param scanOffset
	 * @return int
	 */
	private static int detectPeakMaximum(IFirstDerivativeDetectorSlopes slopes, int startScan, int scanOffset) {

		int size = slopes.size();
		IDetectorSlope slope;
		int peakMaximum = startScan;
		exitloop:
		for(int scan = startScan; scan <= size - CONSECUTIVE_SCAN_STEPS; scan++) {
			slope = slopes.getDetectorSlope(scan + scanOffset);
			if(slope.getSlope() < 0.0d) {
				peakMaximum = scan;
				break exitloop;
			}
		}
		return peakMaximum;
	}

	/**
	 * Detects the peak stops.
	 * 
	 * @param slope
	 * @param startScan
	 * @param scanOffset
	 * @return int
	 */
	private static int detectPeakStop(IFirstDerivativeDetectorSlopes slopes, int startScan, int scanOffset) {

		int size = slopes.size();
		int peakStop = size - CONSECUTIVE_SCAN_STEPS;
		IDetectorSlope slope;
		exitloop:
		for(int scan = startScan; scan <= size - CONSECUTIVE_SCAN_STEPS; scan++) {
			slope = slopes.getDetectorSlope(scan + scanOffset);
			if(slope.getSlope() > 0.0d) {
				peakStop = scan;
				break exitloop;
			}
		}
		return peakStop;
	}
}
