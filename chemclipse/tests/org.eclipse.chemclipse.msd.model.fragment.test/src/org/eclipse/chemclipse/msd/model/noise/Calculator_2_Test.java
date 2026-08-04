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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.CombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class Calculator_2_Test {

	private ICombinedMassSpectrum noiseMassSpectrum;
	private IExtractedIonSignal extractedIonSignal;

	@BeforeAll
	public void setUp() {

		Calculator calculator = new Calculator();
		List<ICombinedMassSpectrum> noiseMassSpectra = new ArrayList<>();
		/*
		 * Noise mass spectrum #1
		 */
		noiseMassSpectrum = new CombinedMassSpectrum();
		noiseMassSpectrum.addIon(new Ion(28.0f, 500.0f));
		noiseMassSpectrum.addIon(new Ion(48.0f, 750.0f));
		noiseMassSpectrum.addIon(new Ion(55.0f, 250.0f));
		noiseMassSpectrum.addIon(new Ion(103.0f, 1000.0f));
		noiseMassSpectrum.addIon(new Ion(178.0f, 200.0f));
		noiseMassSpectra.add(noiseMassSpectrum);
		/*
		 * Noise mass spectrum #2
		 */
		noiseMassSpectrum = new CombinedMassSpectrum();
		noiseMassSpectrum.addIon(new Ion(18.0f, 700.0f));
		noiseMassSpectrum.addIon(new Ion(55.0f, 250.0f));
		noiseMassSpectrum.addIon(new Ion(89.0f, 250.0f));
		noiseMassSpectrum.addIon(new Ion(104.0f, 15000.0f));
		noiseMassSpectrum.addIon(new Ion(155.0f, 20000.0f));
		noiseMassSpectra.add(noiseMassSpectrum);
		/*
		 * Noise mass spectrum #3
		 */
		noiseMassSpectrum = new CombinedMassSpectrum();
		noiseMassSpectrum.addIon(new Ion(43.0f, 800.0f));
		noiseMassSpectrum.addIon(new Ion(48.0f, 760.0f));
		noiseMassSpectrum.addIon(new Ion(52.0f, 1250.0f));
		noiseMassSpectrum.addIon(new Ion(104.0f, 15000.0f));
		noiseMassSpectrum.addIon(new Ion(201.0f, 8900.0f));
		noiseMassSpectra.add(noiseMassSpectrum);
		IMarkedTraces<ITrace> ionsToPreserve = new MarkedTraces(MarkedTraceModus.INCLUDE);
		ionsToPreserve.add(new TraceNominalMSD(103));
		ionsToPreserve.add(new TraceNominalMSD(104));
		ionsToPreserve.add(new TraceNominalMSD(201));
		noiseMassSpectrum = calculator.getNoiseMassSpectrum(noiseMassSpectra, ionsToPreserve);
		extractedIonSignal = noiseMassSpectrum.getExtractedIonSignal();
	}

	@Test
	public void testGetNoiseMassSpectrum_1() {

		assertNotNull(noiseMassSpectrum);
	}

	@Test
	public void testGetNoiseMassSpectrum_2() {

		assertEquals(9, noiseMassSpectrum.getNumberOfIons());
	}

	@Test
	public void testGetNoiseMassSpectrum_3() {

		assertEquals(18, extractedIonSignal.getStartIon());
	}

	@Test
	public void testGetNoiseMassSpectrum_4() {

		assertEquals(178, extractedIonSignal.getStopIon());
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_1() {

		int ion = 18;
		assertEquals(35.0f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_2() {

		int ion = 28;
		assertEquals(25.0f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_3() {

		int ion = 43;
		assertEquals(40.0f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_4() {

		int ion = 48;
		assertEquals(75.5f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_5() {

		int ion = 52;
		assertEquals(62.5f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_6() {

		int ion = 55;
		assertEquals(25.0f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_7() {

		int ion = 89;
		assertEquals(12.5f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_8() {

		int ion = 103;
		assertEquals(0.0f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_9() {

		int ion = 104;
		assertEquals(0.0f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_10() {

		int ion = 155;
		assertEquals(1000.0f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_11() {

		int ion = 178;
		assertEquals(10.0f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}

	@Test
	public void testGetNoiseMassSpectrumAbundance_12() {

		int ion = 201;
		assertEquals(0.0f, extractedIonSignal.getAbundance(ion), 0, "Ion " + ion);
	}
}
