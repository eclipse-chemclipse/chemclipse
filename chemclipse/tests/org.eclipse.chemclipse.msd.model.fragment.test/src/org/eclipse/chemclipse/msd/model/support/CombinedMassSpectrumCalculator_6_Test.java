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

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIon;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIons;
import org.junit.Before;
import org.junit.Test;

public class CombinedMassSpectrumCalculator_6_Test {

	private CombinedMassSpectrumCalculator combinedMassSpectrumCalculator;
	private IMarkedIons excludedIons;

	@Before
	public void setUp() {

		combinedMassSpectrumCalculator = new CombinedMassSpectrumCalculator();
		combinedMassSpectrumCalculator.addIon(56.0f, 5100.0f);
		combinedMassSpectrumCalculator.addIon(60.0f, 52900.0f);
		combinedMassSpectrumCalculator.addIon(104.0f, 5300.0f);
		combinedMassSpectrumCalculator.addIon(28.0f, 5400.0f);
		combinedMassSpectrumCalculator.addIon(103.0f, 5500.0f);
		excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
	}

	@Test
	public void testValues_1() {

		int ion = 103;
		ICombinedMassSpectrum massSpectrum1 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertEquals(5500.0f, massSpectrum1.getIon(ion).getAbundance(), 0);
		excludedIons.add(new MarkedIon(ion));
		combinedMassSpectrumCalculator.removeIons(excludedIons);
		ICombinedMassSpectrum massSpectrum2 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertNull(massSpectrum2.getIon(ion));
	}

	@Test
	public void testValues_2() {

		int ion = 104;
		ICombinedMassSpectrum massSpectrum1 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertEquals(5300.0f, massSpectrum1.getIon(ion).getAbundance(), 0);
		excludedIons.add(new MarkedIon(ion));
		combinedMassSpectrumCalculator.removeIons(excludedIons);
		ICombinedMassSpectrum massSpectrum2 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertNull(massSpectrum2.getIon(ion));
	}

	@Test
	public void testValues_3() {

		ICombinedMassSpectrum massSpectrum1 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertEquals(5100.0f, massSpectrum1.getIon(56).getAbundance(), 0);
		assertEquals(5500.0f, massSpectrum1.getIon(103).getAbundance(), 0);
		excludedIons.add(new MarkedIon(56));
		excludedIons.add(new MarkedIon(103));
		combinedMassSpectrumCalculator.removeIons(excludedIons);
		ICombinedMassSpectrum massSpectrum2 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertNull(massSpectrum2.getIon(56));
		assertNull(massSpectrum2.getIon(103));
	}
}
