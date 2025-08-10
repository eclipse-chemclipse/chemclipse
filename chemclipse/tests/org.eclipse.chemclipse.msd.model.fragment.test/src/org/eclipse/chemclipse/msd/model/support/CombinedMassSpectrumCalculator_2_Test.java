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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIons;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.junit.Before;
import org.junit.Test;

public class CombinedMassSpectrumCalculator_2_Test {

	private CombinedMassSpectrumCalculator combinedMassSpectrumCalculator;
	private IMarkedIons excludedIons;
	private List<IIon> ions;

	@Before
	public void setUp() {

		combinedMassSpectrumCalculator = new CombinedMassSpectrumCalculator();
		ions = new ArrayList<IIon>();
		excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
	}

	@Test
	public void testSize_1() {

		combinedMassSpectrumCalculator.addIons(null, null);
		assertEquals("Size", 0, combinedMassSpectrumCalculator.size());
	}

	@Test
	public void testSize_2() {

		ions.add(new Ion(56.5f, 500.0f));
		combinedMassSpectrumCalculator.addIons(ions, excludedIons);
		assertEquals("Size", 1, combinedMassSpectrumCalculator.size());
	}

	@Test
	public void testSize_3() {

		ions.add(new Ion(56.5f, 500.0f));
		ions.add(new Ion(80.2f, 700.0f));
		combinedMassSpectrumCalculator.addIons(ions, excludedIons);
		assertEquals("Size", 2, combinedMassSpectrumCalculator.size());
	}

	@Test
	public void testSize_4() {

		ions.add(new Ion(56.5f, 500.0f));
		ions.add(new Ion(80.2f, 700.0f));
		ions.add(new Ion(90.3f, 800.0f));
		combinedMassSpectrumCalculator.addIons(ions, excludedIons);
		assertEquals("Size", 3, combinedMassSpectrumCalculator.size());
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
		assertEquals("Size", 1, combinedMassSpectrumCalculator.size());
	}
}
