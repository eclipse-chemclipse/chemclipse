/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.support;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class CombinedMassSpectrumCalculator_7_Test {

	private static final float NORMALIZATION_FACTOR = 1000.0f;
	private ICombinedMassSpectrum noiseMassSpectrum;

	@Before
	public void setUp() {

		CombinedMassSpectrumCalculator combinedMassSpectrumCalculator = new CombinedMassSpectrumCalculator();
		combinedMassSpectrumCalculator.addIon(18.0f, 200.0f);
		combinedMassSpectrumCalculator.addIon(28.0f, 320.0f);
		combinedMassSpectrumCalculator.addIon(43.0f, 400.0f);
		combinedMassSpectrumCalculator.addIon(103.0f, 5000.0f);
		combinedMassSpectrumCalculator.addIon(104.0f, 20500.0f);
		combinedMassSpectrumCalculator.addIon(155.0f, 18000.0f);

		noiseMassSpectrum = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		noiseMassSpectrum.normalize(NORMALIZATION_FACTOR);
	}

	@Test
	public void testValues_1() {

		int ion = 18;
		assertEquals(9.756098f, noiseMassSpectrum.getIon(ion).getAbundance(), 0);
	}

	@Test
	public void testValues_2() {

		int ion = 28;
		assertEquals(15.609756f, noiseMassSpectrum.getIon(ion).getAbundance(), 0);
	}

	@Test
	public void testValues_3() {

		int ion = 43;
		assertEquals(19.512196f, noiseMassSpectrum.getIon(ion).getAbundance(), 0);
	}

	@Test
	public void testValues_4() {

		int ion = 103;
		assertEquals(243.90244f, noiseMassSpectrum.getIon(ion).getAbundance(), 0);
	}

	@Test
	public void testValues_5() {

		int ion = 104;
		assertEquals(1000.0f, noiseMassSpectrum.getIon(ion).getAbundance(), 0);
	}

	@Test
	public void testValues_6() {

		int ion = 155;
		assertEquals(878.04877f, noiseMassSpectrum.getIon(ion).getAbundance(), 0);
	}
}
