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
 * Christoph Läubrich - refactor to use noise segments for calculations
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.noise.stein.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.INoiseCalculator;
import org.eclipse.chemclipse.model.results.ChromatogramSegmentation;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.eclipse.chemclipse.model.support.ChromatogramSegment;
import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.chemclipse.model.support.ISegmentValidator;
import org.eclipse.chemclipse.model.support.NoiseSegment;
import org.eclipse.chemclipse.model.support.SegmentValidatorClassic;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.eclipse.chemclipse.numeric.statistics.Calculations;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

/*
 * S/N = Math.sqrt(intensity) * noiseFactor
 */
public class NoiseCalculator implements INoiseCalculator {

	private IChromatogram chromatogram = null;
	private float noiseFactor = Float.NaN;

	@Override
	public void reset() {

		this.chromatogram = null;
	}

	@Override
	public String getName() {

		return "Stein (experimental)";
	}

	@Override
	public float getNoiseFactor() {

		return noiseFactor;
	}

	@Override
	public float getSignalToNoiseRatio(IChromatogram chromatogram, float intensity) {

		setNoiseFactor(chromatogram);
		if(Float.isFinite(noiseFactor) && noiseFactor > 0) {
			return (float)(Math.sqrt(intensity) * noiseFactor);
		} else {
			return Float.NaN;
		}
	}

	@Override
	public List<INoiseSegment> getNoiseSegments(IChromatogram chromatogram, IProgressMonitor monitor) {

		if(chromatogram instanceof IChromatogramMSD) {
			return getNoiseSegments(chromatogram, IIon.TIC_ION, monitor);
		}
		//
		if(chromatogram != null) {
			ChromatogramSegmentation segmentation = chromatogram.getMeasurementResult(ChromatogramSegmentation.class);
			if(segmentation != null) {
				ISegmentValidator segmentValidator = new SegmentValidatorClassic();
				ITotalScanSignals signals = new TotalScanSignals(chromatogram);
				List<ChromatogramSegment> segments = segmentation.getResult();
				SubMonitor subMonitor = SubMonitor.convert(monitor, segments.size());
				List<INoiseSegment> result = new ArrayList<>();
				for(IAnalysisSegment segment : segments) {
					Double factor = calculateNoiseFactor(segmentValidator, signals.getValues(segment));
					if(factor != null) {
						INoiseSegment noiseSegment = new NoiseSegment(segment, factor);
						result.add(noiseSegment);
					}
					subMonitor.worked(1);
				}
				return result;
			}
		}
		return Collections.emptyList();
	}

	private void setNoiseFactor(IChromatogram chromatogram) {

		if(this.chromatogram != chromatogram) {
			noiseFactor = calculateNoiseFactor(chromatogram);
			this.chromatogram = chromatogram;
		}
	}

	/**
	 * See S.E. Stein:
	 * "An Integrated Method for Spectrum Extraction and Compound Identification from Gas Chromatography/Mass Spectrometry Data"
	 * 
	 * @param IChromatogram
	 */
	private float calculateNoiseFactor(IChromatogram chromatogram) {

		if(chromatogram != null) {
			List<Double> noiseFactors = new ArrayList<>();
			Consumer<INoiseSegment> consumer = segment -> noiseFactors.add(segment.getNoiseFactor());
			getNoiseSegments(chromatogram, null).forEach(consumer);
			if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
				ISegmentValidator segmentValidator = new SegmentValidatorClassic();
				IExtractedIonSignalExtractor extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogramMSD);
				IExtractedIonSignals extractedSignals = extractedIonSignalExtractor.getExtractedIonSignals();
				int startIon = extractedSignals.getStartIon();
				int stopIon = extractedSignals.getStopIon();
				for(int ion = startIon; ion <= stopIon; ion++) {
					List<INoiseSegment> ionNoiseSegments = getNoiseSegments(ion, chromatogram.getMeasurementResult(ChromatogramSegmentation.class), segmentValidator, extractedSignals);
					ionNoiseSegments.forEach(consumer);
				}
			}
			double median = Calculations.getMedian(noiseFactors);
			if(median > 0) {
				return (float)median;
			} else {
				/*
				 * If there is no noise segment at all, take the min signal.
				 * It's not the best solution, but 0 is no option.
				 */
				return chromatogram.getMinSignal();
			}
		}
		return Float.NaN;
	}

	/**
	 * Calculates the noise factor for the given segment if the segment is valid.
	 * 
	 * @param baseSegment
	 * 
	 * @param values
	 * @return
	 */
	private static Double calculateNoiseFactor(ISegmentValidator segmentValidator, float[] values) {

		double mean = Calculations.getMean(values);
		if(!segmentValidator.acceptSegment(values, mean)) {
			return null;
		} else {
			/*
			 * Calculate the median from mean.
			 */
			double medianFromMedian = Calculations.getMedianDeviationFromMedian(values);
			return medianFromMedian / Math.sqrt(mean);
		}
	}

	private List<INoiseSegment> getNoiseSegments(IChromatogram chromatogram, double ion, IProgressMonitor monitor) {

		if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
			ChromatogramSegmentation segmentation = chromatogram.getMeasurementResult(ChromatogramSegmentation.class);
			if(segmentation != null) {
				ISegmentValidator segmentValidator = new SegmentValidatorClassic();
				IExtractedIonSignalExtractor extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogramMSD);
				IExtractedIonSignals signals = extractedIonSignalExtractor.getExtractedIonSignals();
				return getNoiseSegments(ion, segmentation, segmentValidator, signals);
			}
		}
		return Collections.emptyList();
	}

	private List<INoiseSegment> getNoiseSegments(double ion, ChromatogramSegmentation segmentation, ISegmentValidator segmentValidator, IExtractedIonSignals signals) {

		List<INoiseSegment> result = new ArrayList<>();
		if(segmentation != null) {
			List<ChromatogramSegment> segments = segmentation.getResult();
			for(IAnalysisSegment segment : segments) {
				Double factor = calculateNoiseFactor(segmentValidator, signals.getValues(segment, (int)ion));
				if(factor != null) {
					// IScan scan;
					// if(ion == IIon.TIC_ION) {
					// scan = CalculatorSupport.getCombinedMassSpectrum(signals, segment).normalize();
					// } else {
					// scan = new ScanMSD(Collections.singleton(new Ion(ion))).normalize();
					// }
					result.add(new NoiseSegment(segment, factor));
				}
			}
		}
		return result;
	}
}
