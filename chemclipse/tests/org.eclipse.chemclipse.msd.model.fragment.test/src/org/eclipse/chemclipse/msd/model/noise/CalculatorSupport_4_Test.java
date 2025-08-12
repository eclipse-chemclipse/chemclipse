/*******************************************************************************
 * Copyright (c) 2010, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.support.AnalysisSegment;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIon;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIons;
import org.eclipse.chemclipse.msd.model.support.CombinedMassSpectrumCalculator;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignals;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class CalculatorSupport_4_Test {

	private CalculatorSupport calculatorSupport;
	private AnalysisSegment analysisSegment;
	private IExtractedIonSignals extractedIonSignals;
	private IExtractedIonSignal extractedIonSignal;
	private CombinedMassSpectrumCalculator combinedMassSpectrumCalculator;
	private IMarkedIons ionsToPreserve;
	private ICombinedMassSpectrum noiseMassSpectrum;

	@Before
	public void setUp() {

		calculatorSupport = new CalculatorSupport();
		analysisSegment = new AnalysisSegment(1, 3) {

			@Override
			public int getStartRetentionTime() {

				return 0;
			}

			@Override
			public int getStopRetentionTime() {

				return 0;
			}
		};
		extractedIonSignals = new ExtractedIonSignals(1, 3);
		/*
		 * Scan #1
		 */
		extractedIonSignal = new ExtractedIonSignal(43, 104);
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
		combinedMassSpectrumCalculator = calculatorSupport.getCombinedMassSpectrumCalculator(analysisSegment, extractedIonSignals);
		// ---------------------------
		// Mass fragments to preserve will be removed from the noise mass
		// spectrum.
		ionsToPreserve = new MarkedIons(MarkedTraceModus.INCLUDE);
		ionsToPreserve.add(new MarkedIon(104));
		ionsToPreserve.add(new MarkedIon(103));
		noiseMassSpectrum = calculatorSupport.getNoiseMassSpectrum(combinedMassSpectrumCalculator, ionsToPreserve, new NullProgressMonitor());
	}

	@Test
	public void testGetNoiseMassSpectrum_1() {

		assertEquals("Size", 4, noiseMassSpectrum.getNumberOfIons());
	}
}
