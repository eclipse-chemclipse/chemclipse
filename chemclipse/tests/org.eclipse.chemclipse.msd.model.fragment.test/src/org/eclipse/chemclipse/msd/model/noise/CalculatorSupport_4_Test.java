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
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.noise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.model.support.AnalysisSegment;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.support.CombinedNominalMassSpectrumCalculator;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignals;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class CalculatorSupport_4_Test {

	private ICombinedMassSpectrum noiseMassSpectrum;

	@BeforeAll
	public void setUp() {

		CalculatorSupport calculatorSupport = new CalculatorSupport();
		AnalysisSegment analysisSegment = new AnalysisSegment(1, 3) {

			@Override
			public int getStartRetentionTime() {

				return 0;
			}

			@Override
			public int getStopRetentionTime() {

				return 0;
			}
		};
		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(1, 3);
		/*
		 * Scan #1
		 */
		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(43, 104);
		extractedIonSignal.setAbundance(104, 500.0f);
		extractedIonSignal.setAbundance(103, 2500.0f);
		extractedIonSignal.setAbundance(43, 120.0f);
		extractedIonSignals.add(extractedIonSignal);
		/*
		 * Scan #2
		 */
		extractedIonSignal = new ExtractedIonSignal(28, 160);
		extractedIonSignal.setAbundance(155, 18000.0f);
		extractedIonSignal.setAbundance(103, 2500.0f);
		extractedIonSignal.setAbundance(28, 320.0f);
		extractedIonSignals.add(extractedIonSignal);
		/*
		 * Scan #3
		 */
		extractedIonSignal = new ExtractedIonSignal(18, 104);
		extractedIonSignal.setAbundance(18, 200.0f);
		extractedIonSignal.setAbundance(43, 280.0f);
		extractedIonSignal.setAbundance(104, 20000.0f);
		extractedIonSignals.add(extractedIonSignal);
		CombinedNominalMassSpectrumCalculator combinedMassSpectrumCalculator = calculatorSupport.getCombinedMassSpectrumCalculator(analysisSegment, extractedIonSignals);
		// ---------------------------
		// Mass fragments to preserve will be removed from the noise mass
		// spectrum.
		IMarkedTraces<ITrace> ionsToPreserve = new MarkedTraces(MarkedTraceModus.INCLUDE);
		ionsToPreserve.add(new TraceNominalMSD(104));
		ionsToPreserve.add(new TraceNominalMSD(103));
		noiseMassSpectrum = calculatorSupport.getNoiseMassSpectrum(combinedMassSpectrumCalculator, ionsToPreserve);
	}

	@Test
	public void testGetNoiseMassSpectrum_1() {

		assertEquals(4, noiseMassSpectrum.getNumberOfIons());
	}
}
