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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CombinedNominalMassSpectrumCalculator_2_Test {

	private CombinedNominalMassSpectrumCalculator combinedMassSpectrumCalculator;
	private IMarkedTraces<ITrace> excludedIons;
	private List<IIon> ions;

	@BeforeEach
	public void setUp() {

		combinedMassSpectrumCalculator = new CombinedNominalMassSpectrumCalculator();
		ions = new ArrayList<>();
		excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);
	}

	@Test
	public void testSize_1() {

		combinedMassSpectrumCalculator.addIons(null, null);
		assertEquals(0, combinedMassSpectrumCalculator.size());
	}

	@Test
	public void testSize_2() {

		ions.add(new Ion(56.5f, 500.0f));
		combinedMassSpectrumCalculator.addIons(ions, excludedIons);
		assertEquals(1, combinedMassSpectrumCalculator.size());
	}

	@Test
	public void testSize_3() {

		ions.add(new Ion(56.5f, 500.0f));
		ions.add(new Ion(80.2f, 700.0f));
		combinedMassSpectrumCalculator.addIons(ions, excludedIons);
		assertEquals(2, combinedMassSpectrumCalculator.size());
	}

	@Test
	public void testSize_4() {

		ions.add(new Ion(56.5f, 500.0f));
		ions.add(new Ion(80.2f, 700.0f));
		ions.add(new Ion(90.3f, 800.0f));
		combinedMassSpectrumCalculator.addIons(ions, excludedIons);
		assertEquals(3, combinedMassSpectrumCalculator.size());
	}

	@Test
	public void testSize_5() {

		/*
		 * Math round is used to determine the integer value of the mass
		 * fragment.
		 */
		ions.add(new Ion(56.4f, 500.0f));
		ions.add(new Ion(56.2f, 700.0f));
		ions.add(new Ion(55.9f, 800.0f));
		combinedMassSpectrumCalculator.addIons(ions, excludedIons);
		assertEquals(1, combinedMassSpectrumCalculator.size());
	}
}
