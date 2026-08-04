/*******************************************************************************
 * Copyright (c) 2010, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Chrsitoph Läubrich - don't use exceptions as return values
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.noise;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.exceptions.AnalysisSupportException;
import org.eclipse.chemclipse.model.support.AnalysisSupport;
import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.exceptions.FilterException;
import org.eclipse.chemclipse.msd.model.support.CombinedNominalMassSpectrumCalculator;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.eclipse.chemclipse.numeric.statistics.Calculations;
import org.eclipse.chemclipse.support.traces.ITrace;

public class Calculator {

	private static final Logger logger = Logger.getLogger(Calculator.class);
	private final CalculatorSupport calculatorSupport;

	public Calculator() {

		/*
		 * Why do we instantiate a class CalculatorSupport instance? If e.g. the
		 * getNoiseMassSpectrum(...) method is called 1000 times, as much
		 * CalculatorSupport instances will be created. It's maybe a performance
		 * problem. That's why the calculator support will be instantiated on
		 * class creation once.
		 */
		calculatorSupport = new CalculatorSupport();
	}

	/**
	 * Calculates a noise mass spectrum, normalized to 1000 by the given noise
	 * mass spectra.
	 */
	public ICombinedMassSpectrum getNoiseMassSpectrum(List<ICombinedMassSpectrum> noiseMassSpectra, IMarkedTraces<ITrace> ionsToPreserve) {

		/*
		 * Iterate through all given noise mass spectra.
		 */
		CombinedNominalMassSpectrumCalculator combinedMassSpectrumCalculator = new CombinedNominalMassSpectrumCalculator();
		for(ICombinedMassSpectrum noiseMassSpectrum : noiseMassSpectra) {
			/*
			 * Add the value of each ion to the combined mass spectrum
			 * calculator.
			 */
			for(IIon ion : noiseMassSpectrum.getIons()) {
				combinedMassSpectrumCalculator.addIon(ion.getIon(), ion.getAbundance());
			}
		}
		return calculatorSupport.getNoiseMassSpectrum(combinedMassSpectrumCalculator, ionsToPreserve);
	}

	/**
	 * Calculates the noise segments. May return null.
	 */
	public List<INoiseSegmentMSD> getNoiseSegments(IExtractedIonSignals extractedIonSignals, IMarkedTraces<ITrace> ionsToPreserve, int segmentWidth) throws FilterException {

		/*
		 * Check the scan range.
		 */
		int width = segmentWidth;
		ScanRange scanRange = new ScanRange(extractedIonSignals.getStartScan(), extractedIonSignals.getStopScan());
		calculatorSupport.checkScanRange(scanRange, width);
		/*
		 * Try to calculate an appropriate set of segments.
		 */
		List<INoiseSegmentMSD> noiseSegments = null;
		try {
			AnalysisSupport analysisSupport = new AnalysisSupport(scanRange, width);
			List<IAnalysisSegment> analysisSegments = analysisSupport.getAnalysisSegments();
			noiseSegments = calculateNoiseSegments(analysisSegments, extractedIonSignals, ionsToPreserve);
		} catch(AnalysisSupportException e) {
			logger.warn(e);
		}
		return noiseSegments;
	}

	// --------------------------------------------private Methods
	/**
	 * See S.E. Stein:
	 * "An Integrated Method for Spectrum Extraction and Compound Identification from Gas Chromatography/Mass Spectrometry Data"
	 * .
	 */
	private List<INoiseSegmentMSD> calculateNoiseSegments(List<IAnalysisSegment> analysisSegments, IExtractedIonSignals extractedIonSignals, IMarkedTraces<ITrace> ionsToPreserve) {

		@SuppressWarnings("unused")
		int rejected = 0;
		@SuppressWarnings("unused")
		int accepted = 0;

		List<INoiseSegmentMSD> noiseSegments = new ArrayList<>();
		for(IAnalysisSegment analysisSegment : analysisSegments) {
			/*
			 * TIC
			 */
			if(calculateMedianFromMean(analysisSegment, extractedIonSignals)) {
				accepted++;
				/*
				 * If no exception will be thrown, the segment is accepted. The
				 * combined mass spectrum will be calculated using the segment
				 * and will be converted in a noise mass spectrum, associated
				 * with the segment. The noise segment will be stored in the
				 * noise segment list.
				 */
				CombinedNominalMassSpectrumCalculator combinedMassSpectrumCalculator = calculatorSupport.getCombinedMassSpectrumCalculator(analysisSegment, extractedIonSignals);
				ICombinedMassSpectrum noiseMassSpectrum = calculatorSupport.getNoiseMassSpectrum(combinedMassSpectrumCalculator, ionsToPreserve);
				INoiseSegmentMSD noiseSegment = new NoiseSegmentMSD(analysisSegment, noiseMassSpectrum);
				noiseSegments.add(noiseSegment);
			} else {
				rejected++;
			}
		}
		return noiseSegments;
	}

	/*
	 * Calculates the median from mean.
	 */
	private boolean calculateMedianFromMean(IAnalysisSegment analysisSegment, IExtractedIonSignals extractedIonSignals) {

		IExtractedIonSignal signal;
		int size = analysisSegment.getWidth();
		if(size <= 0) {
			return false;
		}
		double[] values = new double[size];
		int counter = 0;
		for(int scan = analysisSegment.getStartScan(); scan <= analysisSegment.getStopScan(); scan++) {
			try {
				signal = extractedIonSignals.getExtractedIonSignal(scan);
				values[counter] = signal.getTotalSignal();
			} catch(Exception e) {
				logger.warn(e);
			} finally {
				/*
				 * Increment counters position.
				 */
				counter++;
			}
		}
		/*
		 * Check if the segment is accepted.<br/> If yes, than calculate its
		 * median.<br/> If no, than throw an exception.
		 */
		double mean = Calculations.getMean(values);
		return calculatorSupport.acceptSegment(values, mean);
	}
	// --------------------------------------------private Methods
}
