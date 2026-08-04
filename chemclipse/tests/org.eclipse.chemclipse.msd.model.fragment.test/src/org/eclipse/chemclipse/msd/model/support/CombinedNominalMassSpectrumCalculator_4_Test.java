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
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CombinedNominalMassSpectrumCalculator_4_Test {

	private CombinedNominalMassSpectrumCalculator combinedNominalMassSpectrumCalculator;
	private IMarkedTraces<ITrace> excludedIons;

	@BeforeEach
	public void setUp() {

		combinedNominalMassSpectrumCalculator = new CombinedNominalMassSpectrumCalculator();
		combinedNominalMassSpectrumCalculator.addIon(56.0f, 5100.0f);
		combinedNominalMassSpectrumCalculator.addIon(60.0f, 52900.0f);
		combinedNominalMassSpectrumCalculator.addIon(104.0f, 5300.0f);
		combinedNominalMassSpectrumCalculator.addIon(28.0f, 5400.0f);
		combinedNominalMassSpectrumCalculator.addIon(103.0f, 5500.0f);
		excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);
	}

	@Test
	public void testValues_1() {

		int ion = 103;
		assertEquals(5500.0, combinedNominalMassSpectrumCalculator.getValues().get(ion).get(0), 0);
		excludedIons.add(new TraceNominalMSD(ion));
		combinedNominalMassSpectrumCalculator.removeIons(excludedIons);
		assertNull(combinedNominalMassSpectrumCalculator.getValues().get(ion));
	}

	@Test
	public void testValues_2() {

		int ion = 104;
		assertEquals(5300.0, combinedNominalMassSpectrumCalculator.getValues().get(ion).get(0), 0);
		excludedIons.add(new TraceNominalMSD(ion));
		combinedNominalMassSpectrumCalculator.removeIons(excludedIons);
		assertNull(combinedNominalMassSpectrumCalculator.getValues().get(ion));
	}

	@Test
	public void testValues_3() {

		assertEquals(5100.0, combinedNominalMassSpectrumCalculator.getValues().get(56).get(0), 0);
		assertEquals(5500.0, combinedNominalMassSpectrumCalculator.getValues().get(103).get(0), 0);
		excludedIons.add(new TraceNominalMSD(56));
		excludedIons.add(new TraceNominalMSD(103));
		combinedNominalMassSpectrumCalculator.removeIons(excludedIons);
		assertNull(combinedNominalMassSpectrumCalculator.getValues().get(56));
		assertNull(combinedNominalMassSpectrumCalculator.getValues().get(103));
	}
}
