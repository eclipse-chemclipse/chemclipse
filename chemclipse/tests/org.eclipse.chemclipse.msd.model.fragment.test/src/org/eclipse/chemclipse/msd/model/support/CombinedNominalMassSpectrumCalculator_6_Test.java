/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CombinedNominalMassSpectrumCalculator_6_Test {

	private CombinedNominalMassSpectrumCalculator combinedMassSpectrumCalculator;
	private IMarkedTraces<ITrace> excludedIons;

	@BeforeEach
	public void setUp() {

		combinedMassSpectrumCalculator = new CombinedNominalMassSpectrumCalculator();
		combinedMassSpectrumCalculator.addIon(56.0f, 5100.0f);
		combinedMassSpectrumCalculator.addIon(60.0f, 52900.0f);
		combinedMassSpectrumCalculator.addIon(104.0f, 5300.0f);
		combinedMassSpectrumCalculator.addIon(28.0f, 5400.0f);
		combinedMassSpectrumCalculator.addIon(103.0f, 5500.0f);
		excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);
	}

	@Test
	public void testValues_1() {

		int ion = 103;
		ICombinedMassSpectrum massSpectrum1 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertEquals(5500.0f, massSpectrum1.getIon(ion).getAbundance(), 0);
		excludedIons.add(new TraceNominalMSD(ion));
		combinedMassSpectrumCalculator.removeIons(excludedIons);
		ICombinedMassSpectrum massSpectrum2 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertNull(massSpectrum2.getIon(ion));
	}

	@Test
	public void testValues_2() {

		int ion = 104;
		ICombinedMassSpectrum massSpectrum1 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertEquals(5300.0f, massSpectrum1.getIon(ion).getAbundance(), 0);
		excludedIons.add(new TraceNominalMSD(ion));
		combinedMassSpectrumCalculator.removeIons(excludedIons);
		ICombinedMassSpectrum massSpectrum2 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertNull(massSpectrum2.getIon(ion));
	}

	@Test
	public void testValues_3() {

		ICombinedMassSpectrum massSpectrum1 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertEquals(5100.0f, massSpectrum1.getIon(56).getAbundance(), 0);
		assertEquals(5500.0f, massSpectrum1.getIon(103).getAbundance(), 0);
		excludedIons.add(new TraceNominalMSD(56));
		excludedIons.add(new TraceNominalMSD(103));
		combinedMassSpectrumCalculator.removeIons(excludedIons);
		ICombinedMassSpectrum massSpectrum2 = combinedMassSpectrumCalculator.createMassSpectrum(CalculationType.SUM);
		assertNull(massSpectrum2.getIon(56));
		assertNull(massSpectrum2.getIon(103));
	}
}
