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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class PeakIntegrationResult_7_Test {

	private IPeakIntegrationResult result = new PeakIntegrationResult();

	@Test
	public void testGetIntegratedIons_1() {

		result.addIntegratedTrace(55);
		Set<Integer> ions = result.getIntegratedTraces();
		assertEquals(1, ions.size());
		assertTrue(ions.contains(55));
		assertFalse(ions.contains(73));
		assertFalse(ions.contains(34));
		assertFalse(ions.contains(48));
	}

	@Test
	public void testGetIntegratedIons_2() {

		result.addIntegratedTrace(55);
		result.addIntegratedTrace(73);
		result.addIntegratedTrace(34);
		result.addIntegratedTrace(48);
		result.removeIntegratedTrace(73);
		result.removeIntegratedTrace(34);
		result.removeIntegratedTrace(48);
		Set<Integer> ions = result.getIntegratedTraces();
		assertEquals(1, ions.size());
		assertTrue(ions.contains(55));
		assertFalse(ions.contains(73));
		assertFalse(ions.contains(34));
		assertFalse(ions.contains(48));
	}

	@Test
	public void testGetIntegratedIons_3() {

		Set<Integer> ions = new HashSet<Integer>();
		ions.add(55);
		ions.add(73);
		ions.add(34);
		ions.add(48);
		result.addIntegratedTraces(ions);
		Set<Integer> ions2 = result.getIntegratedTraces();
		assertEquals(4, ions2.size());
		assertTrue(ions2.contains(55));
		assertTrue(ions2.contains(73));
		assertTrue(ions2.contains(34));
		assertTrue(ions2.contains(48));
	}

	@Test
	public void testGetIntegratedIons_4() {

		result.addIntegratedTrace(55);
		result.addIntegratedTrace(55);
		result.addIntegratedTrace(55);
		result.addIntegratedTrace(55);
		Set<Integer> ions = result.getIntegratedTraces();
		assertEquals(1, ions.size());
		assertTrue(ions.contains(55));
	}
}
