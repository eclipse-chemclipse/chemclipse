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
 * Christoph Läubrich - refactor the code to use NoiseSegments
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.noise.dyson.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.INoiseCalculator;
import org.eclipse.chemclipse.model.results.ChromatogramSegmentation;
import org.eclipse.chemclipse.model.results.NoiseSegmentMeasurementResult;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.chemclipse.model.support.ISegmentValidator;
import org.eclipse.chemclipse.model.support.NoiseSegment;
import org.eclipse.chemclipse.model.support.SegmentValidatorClassic;
import org.eclipse.chemclipse.model.support.SegmentValidatorUserSelection;
import org.eclipse.chemclipse.numeric.statistics.Calculations;
import org.eclipse.core.runtime.IProgressMonitor;

/*
 * S/N = intensity / noiseValue
 */
public class NoiseCalculator implements INoiseCalculator {

	private static final String CALCULATOR_ID = "org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.noise.dyson";
	//
	private IChromatogram chromatogram = null;
	private float noiseFactor = Float.NaN;

	@Override
	public void reset() {

		this.chromatogram = null;
	}

	@Override
	public String getName() {

		return "Dyson";
	}

	@Override
	public float getNoiseFactor() {

		return noiseFactor;
	}

	@Override
	public float getSignalToNoiseRatio(IChromatogram chromatogram, float intensity) {

		setNoiseFactor(chromatogram);
		if(Float.isFinite(noiseFactor) && noiseFactor > 0) {
			return intensity / noiseFactor;
		} else {
			return Float.NaN;
		}
	}

	private void setNoiseFactor(IChromatogram chromatogram) {

		if(this.chromatogram != chromatogram) {
			noiseFactor = calculateNoiseFactor(chromatogram);
			this.chromatogram = chromatogram;
		}
	}

	/**
	 * Method described by "Norman Dyson".
	 * Chromatographic Integration Methods, Seconds edition
	 * 
	 * @param IChromatogram
	 */
	private float calculateNoiseFactor(IChromatogram chromatogram) {

		if(chromatogram != null) {
			/*
			 * Calculate the mean value of the standard deviations.
			 */
			List<INoiseSegment> noiseSegments = null;
			NoiseSegmentMeasurementResult noiseSegmentMeasurementResult = chromatogram.getMeasurementResult(NoiseSegmentMeasurementResult.class);
			if(noiseSegmentMeasurementResult != null) {
				noiseSegments = noiseSegmentMeasurementResult.getResult();
				ISegmentValidator segmentValidatorClassic = new SegmentValidatorClassic();
				ISegmentValidator segmentValidatorUserSelection = new SegmentValidatorUserSelection();
				ITotalScanSignals signals = new TotalScanSignals(chromatogram);
				for(INoiseSegment noiseSegment : noiseSegments) {
					if(noiseSegment.getNoiseFactor() == 0) {
						ISegmentValidator segmentValidator = noiseSegment.isUserSelection() ? segmentValidatorUserSelection : segmentValidatorClassic;
						Double segmentNoiseFactor = calculateNoiseFactor(noiseSegment, segmentValidator, signals);
						if(segmentNoiseFactor != null) {
							noiseSegment.setNoiseFactor(segmentNoiseFactor);
						}
					}
				}
			}
			/*
			 * Empty, then reload.
			 */
			if(noiseSegments == null || noiseSegments.isEmpty()) {
				noiseSegments = getNoiseSegments(chromatogram, null);
			}
			/*
			 * Active Available (User Selection)
			 */
			List<Double> segmentNoiseFactors = new ArrayList<>();
			for(INoiseSegment noiseSegment : noiseSegments) {
				if(noiseSegment.isUse()) {
					segmentNoiseFactors.add(noiseSegment.getNoiseFactor());
				}
			}
			/*
			 * Default Selection
			 */
			double noiseFactor = 0;
			if(segmentNoiseFactors.isEmpty()) {
				/*
				 * Median value
				 */
				noiseSegments.forEach(n -> n.setUse(false));
				segmentNoiseFactors = noiseSegments.stream().map(s -> s.getNoiseFactor()).collect(Collectors.toList());
				noiseFactor = Calculations.getMedian(segmentNoiseFactors);
			} else {
				/*
				 * Mean Value
				 */
				noiseFactor = Calculations.getMean(segmentNoiseFactors);
			}
			/*
			 * Final Check
			 */
			if(noiseFactor > 0) {
				return (float)noiseFactor;
			} else {
				/*
				 * If there is no noise segment at all, take the min signal.
				 * It's not the best solution, but 0 is no option.
				 */
				noiseFactor = chromatogram.getMinSignal();
				if(noiseFactor > 0) {
					return (float)noiseFactor;
				}
			}
		}
		//
		return Float.NaN;
	}

	private Double calculateNoiseFactor(IAnalysisSegment segment, ISegmentValidator segmentValidator, ITotalScanSignals signals) {

		/*
		 * Check that there is at least a width of 1.
		 */
		int segmentWidth = segment.getWidth();
		if(segmentWidth < 1) {
			return null;
		}
		/*
		 * Get the total signal values.
		 */
		double[] values = new double[segmentWidth];
		int counter = 0;
		for(int scan = segment.getStartScan(); scan <= segment.getStopScan(); scan++) {
			ITotalScanSignal signal = signals.getTotalScanSignal(scan);
			if(signal != null) {
				values[counter] = signal.getTotalSignal();
			}
			counter++;
		}
		/*
		 * Check if the segment is accepted.<br/> If yes, than calculate its
		 * delta signal height.<br/> If no, than throw an exception.
		 */
		double mean = Calculations.getMean(values);
		if(!segmentValidator.acceptSegment(values, mean)) {
			/*
			 * The calling method has now the chance to not add the value to its
			 * calculation.
			 */
			return null;
		} else {
			/*
			 * Calculate the difference between highest and lowest value.
			 */
			double highestValue = Calculations.getMax(values);
			double lowestValue = Calculations.getMin(values);
			return highestValue - lowestValue;
		}
	}

	@Override
	public List<INoiseSegment> getNoiseSegments(IChromatogram chromatogram, IProgressMonitor monitor) {

		if(chromatogram != null) {
			ChromatogramSegmentation segmentation = chromatogram.getMeasurementResult(ChromatogramSegmentation.class);
			if(segmentation != null) {
				ISegmentValidator segmentValidator = new SegmentValidatorClassic();
				ITotalScanSignals signals = new TotalScanSignals(chromatogram);
				List<INoiseSegment> noiseSegments = new ArrayList<>();
				List<Integer> widths = new ArrayList<>();
				//
				for(IAnalysisSegment analysisSegment : segmentation.getResult()) {
					/*
					 * TIC (use only the total signal)
					 */
					widths.add(analysisSegment.getWidth());
					Double segmentNoiseFactor = calculateNoiseFactor(analysisSegment, segmentValidator, signals);
					if(segmentNoiseFactor != null) {
						noiseSegments.add(new NoiseSegment(analysisSegment, segmentNoiseFactor));
					}
				}
				/*
				 * Chromatogram Measurement Results
				 */
				int width = (int)Math.round(widths.stream().mapToDouble(Integer::doubleValue).average().orElse(0));
				ChromatogramSegmentation chromatogramSegmentation = new ChromatogramSegmentation(chromatogram, width);
				chromatogram.addMeasurementResult(chromatogramSegmentation);
				NoiseSegmentMeasurementResult noiseSegmentMeasurementResult = new NoiseSegmentMeasurementResult(noiseSegments, chromatogramSegmentation, CALCULATOR_ID);
				chromatogram.addMeasurementResult(noiseSegmentMeasurementResult);
				//
				return noiseSegments;
			}
		}
		//
		return Collections.emptyList();
	}
}
