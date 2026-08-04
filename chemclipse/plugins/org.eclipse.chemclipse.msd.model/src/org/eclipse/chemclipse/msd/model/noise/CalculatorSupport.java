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
 * Christoph Läubrich - add method to get combined spectrum
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.noise;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.model.support.SegmentValidatorClassic;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.exceptions.FilterException;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.msd.model.support.CombinedNominalMassSpectrumCalculator;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.eclipse.chemclipse.support.traces.ITrace;

public class CalculatorSupport {

	private static final Logger logger = Logger.getLogger(CalculatorSupport.class);
	private static final float NORMALIZATION_FACTOR = 1000.0f;
	private final SegmentValidatorClassic segmentValidator;

	public CalculatorSupport() {

		segmentValidator = new SegmentValidatorClassic();
	}

	/**
	 * Calculates whether the segment will be accepted or not.
	 * 
	 * @param values
	 * @param mean
	 * @return boolean
	 */
	public boolean acceptSegment(double[] values, double mean) {

		return segmentValidator.acceptSegment(values, mean);
	}

	/**
	 * Calculate a noise mass spectrum from the given segment.
	 * 
	 * @return
	 */
	public CombinedNominalMassSpectrumCalculator getCombinedMassSpectrumCalculator(IAnalysisSegment analysisSegment, IExtractedIonSignals extractedIonSignals) {

		IExtractedIonSignal extractedIonSignal;
		CombinedNominalMassSpectrumCalculator combinedMassSpectrumCalculator = new CombinedNominalMassSpectrumCalculator();
		for(int scan = analysisSegment.getStartScan(); scan <= analysisSegment.getStopScan(); scan++) {
			try {
				extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
				/*
				 * Add the abundance for each ion in the signal to summed signal.
				 */
				for(int ion = extractedIonSignal.getStartIon(); ion <= extractedIonSignal.getStopIon(); ion++) {
					combinedMassSpectrumCalculator.addIon(ion, extractedIonSignal.getAbundance(ion));
				}
			} catch(NoExtractedIonSignalStoredException e) {
				logger.warn(e);
			}
		}
		return combinedMassSpectrumCalculator;
	}

	public static ICombinedMassSpectrum getCombinedMassSpectrum(IExtractedIonSignals extractedIonSignals, IScanRange range) {

		CombinedNominalMassSpectrumCalculator combinedMassSpectrumCalculator = new CombinedNominalMassSpectrumCalculator();
		for(int scan = range.getStartScan(); scan <= range.getStopScan(); scan++) {
			try {
				IExtractedIonSignal extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
				/*
				 * Add the abundance for each ion in the signal to summed signal.
				 */
				for(int ion = extractedIonSignal.getStartIon(); ion <= extractedIonSignal.getStopIon(); ion++) {
					combinedMassSpectrumCalculator.addIon(ion, extractedIonSignal.getAbundance(ion));
				}
			} catch(NoExtractedIonSignalStoredException e) {
			}
		}

		ICombinedMassSpectrum noiseMassSpectrum = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		noiseMassSpectrum.normalize(NORMALIZATION_FACTOR);

		return noiseMassSpectrum;
	}

	/*
	 * Returns a combined mass spectrum.
	 */
	public ICombinedMassSpectrum getNoiseMassSpectrum(CombinedNominalMassSpectrumCalculator combinedMassSpectrumCalculator, IMarkedTraces<ITrace> ionsToPreserve) {

		/*
		 * That's correct here to remove the ions to preserve from the noise mass spectrum.
		 */
		combinedMassSpectrumCalculator.removeIons(ionsToPreserve);
		ICombinedMassSpectrum noiseMassSpectrum = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		noiseMassSpectrum.normalize(NORMALIZATION_FACTOR);
		return noiseMassSpectrum;
	}

	/**
	 * Checks the scan range.
	 * 
	 * @param scanRange
	 * @throws FilterException
	 */
	public void checkScanRange(ScanRange scanRange, int segmentWidth) throws FilterException {

		/*
		 * Check the scan range.
		 */
		if(scanRange == null || scanRange.getWidth() <= segmentWidth) {
			throw new FilterException("The selected scan width is lower than the segment width.");
		}
	}
}