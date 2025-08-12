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

import org.junit.Before;
import org.junit.Test;

public class CombinedMassSpectrumCalculator_3_Test {

	private CombinedMassSpectrumCalculator combinedMassSpectrumCalculator;

	@Before
	public void setUp() {

		combinedMassSpectrumCalculator = new CombinedMassSpectrumCalculator();
		combinedMassSpectrumCalculator.addIon(56.0f, 5100.0f);
		combinedMassSpectrumCalculator.addIon(60.0f, 52900.0f);
		combinedMassSpectrumCalculator.addIon(104.0f, 5300.0f);
		combinedMassSpectrumCalculator.addIon(28.0f, 5400.0f);
		combinedMassSpectrumCalculator.addIon(103.0f, 5500.0f);
	}

	@Test
	public void testValues_1() {

		int ion = 103;
		assertEquals(5500.0, combinedMassSpectrumCalculator.getValues().get(ion).get(0), 0);
		combinedMassSpectrumCalculator.removeIon(ion);
		assertEquals(null, combinedMassSpectrumCalculator.getValues().get(ion));
	}

	@Test
	public void testValues_2() {

		int ion = 104;
		assertEquals(5300.0, combinedMassSpectrumCalculator.getValues().get(ion).get(0), 0);
		combinedMassSpectrumCalculator.removeIon(ion);
		assertEquals(null, combinedMassSpectrumCalculator.getValues().get(ion));
	}

	@Test
	public void testValues_3() {

		assertEquals(5100.0, combinedMassSpectrumCalculator.getValues().get(56).get(0), 0);
		assertEquals(5500.0, combinedMassSpectrumCalculator.getValues().get(103).get(0), 0);
		combinedMassSpectrumCalculator.removeIon(56);
		combinedMassSpectrumCalculator.removeIon(103);
		assertEquals(null, combinedMassSpectrumCalculator.getValues().get(56));
		assertEquals(null, combinedMassSpectrumCalculator.getValues().get(103));
	}
}
