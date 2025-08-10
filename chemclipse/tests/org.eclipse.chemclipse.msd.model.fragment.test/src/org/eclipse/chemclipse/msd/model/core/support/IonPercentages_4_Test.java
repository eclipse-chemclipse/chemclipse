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
package org.eclipse.chemclipse.msd.model.core.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.Before;
import org.junit.Test;

public class IonPercentages_4_Test {

	private IScanMSD massSpectrum;
	private IIonPercentages ionPercentages;
	private Map<Integer, Float> ions;

	@Before
	public void setUp() throws Exception {

		ions = new HashMap<Integer, Float>();
		massSpectrum = null;
		ionPercentages = new IonPercentages(massSpectrum);
	}

	@Test
	public void testMassSpectrum_1() {

		assertNull("MassSpectrum", massSpectrum);
	}

	@Test
	public void testIonPercentages_1() {

		assertEquals("45", 0.0f, ionPercentages.getPercentage(45), 0);
	}

	@Test
	public void testIonPercentages_2() {

		List<Integer> ionList = new ArrayList<Integer>(ions.keySet());
		assertEquals("All", 0.0f, ionPercentages.getPercentage(ionList), 0);
	}
}
