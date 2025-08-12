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
import static org.junit.Assert.assertNull;

import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class CombinedMassSpectrumCalculator_8_Test {

	private CombinedMassSpectrumCalculator combinedMassSpectrumCalculator;

	@Before
	public void setUp() {

		combinedMassSpectrumCalculator = new CombinedMassSpectrumCalculator();
		combinedMassSpectrumCalculator.addIon(56.0f, 5100.0f);
		combinedMassSpectrumCalculator.addIon(60.0f, 0.0f);
		combinedMassSpectrumCalculator.addIon(104.0f, 5300.0f);
		combinedMassSpectrumCalculator.addIon(28.0f, 5400.0f);
		combinedMassSpectrumCalculator.addIon(103.0f, 0.0f);
	}

	@Test
	public void testValues_1() {

		assertEquals(3, combinedMassSpectrumCalculator.size());
	}

	@Test
	public void testValues_2() {

		int ion = 60;
		ICombinedMassSpectrum massSpectrum = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertNull(massSpectrum.getIon(ion));
	}

	@Test
	public void testValues_3() {

		int ion = 103;
		ICombinedMassSpectrum massSpectrum = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertNull(massSpectrum.getIon(ion));
	}

	@Test
	public void testValues_4() {

		int ion = 56;
		ICombinedMassSpectrum massSpectrum = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertEquals(5100.0f, massSpectrum.getIon(ion).getAbundance(), 0);
	}

	@Test
	public void testValues_5() {

		int ion = 104;
		ICombinedMassSpectrum massSpectrum = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertEquals(5300.0f, massSpectrum.getIon(ion).getAbundance(), 0);
	}

	@Test
	public void testValues_6() {

		int ion = 28;
		ICombinedMassSpectrum massSpectrum = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertEquals(5400.0f, massSpectrum.getIon(ion).getAbundance(), 0);
	}
}
