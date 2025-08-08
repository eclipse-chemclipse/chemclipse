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

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class PeakIntegrationResult_6_Test {

	private IPeakIntegrationResult result;

	@Before
	public void setUp() throws Exception {

		result = new PeakIntegrationResult();
		result.setIntegratedArea(-500325.0d);
		result.addIntegratedTrace(55);
		result.setIntegratorType("FirstDerivative");
		result.setModelDescription("TIC");
		result.setPeakType("VV");
		result.setPurity(-0.85f);
		result.setSN(-159.5f);
		result.setStartRetentionTime(-1500);
		result.setStopRetentionTime(-5500);
		result.setTailing(-1.56f);
		result.setWidth(-4000);
	}

	@Test
	public void testGetIntegratedArea_1() {

		assertEquals(-500325.0d, result.getIntegratedArea(), 0);
	}

	@Test
	public void testGetIntegratedIons_1() {

		Set<Integer> ions = result.getIntegratedTraces();
		assertEquals(1, ions.size());
		assertTrue(ions.contains(55));
		assertFalse(ions.contains(73));
		assertFalse(ions.contains(34));
		assertFalse(ions.contains(48));
	}

	@Test
	public void testGetIntegratorType_1() {

		assertEquals("FirstDerivative", result.getIntegratorType());
	}

	@Test
	public void testGetModelDescription_1() {

		assertEquals("TIC", result.getModelDescription());
	}

	@Test
	public void testGetPeakType_1() {

		assertEquals("VV", result.getPeakType());
	}

	@Test
	public void testGetPurity_1() {

		assertEquals(-0.85f, result.getPurity(), 0);
	}

	@Test
	public void testGetSN_1() {

		assertEquals(-159.5f, result.getSN(), 0);
	}

	@Test
	public void testGetStartRetentionTime_1() {

		assertEquals(-1500, result.getStartRetentionTime());
	}

	@Test
	public void testGetStopRetentionTime_1() {

		assertEquals(-5500, result.getStopRetentionTime());
	}

	@Test
	public void testGetTailing_1() {

		assertEquals(-1.56f, result.getTailing(), 0);
	}

	@Test
	public void testGetWidth_1() {

		assertEquals(-4000, result.getWidth());
	}
}
