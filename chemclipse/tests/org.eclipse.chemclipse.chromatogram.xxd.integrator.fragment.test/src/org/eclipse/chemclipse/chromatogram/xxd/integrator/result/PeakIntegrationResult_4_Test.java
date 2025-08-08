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

import org.junit.Test;

public class PeakIntegrationResult_4_Test {

	private IPeakIntegrationResult result = new PeakIntegrationResult();

	@Test
	public void testGetIntegratedArea_1() {

		assertEquals(0.0d, result.getIntegratedArea(), 0);
	}

	@Test
	public void testGetIntegratedIons_1() {

		assertEquals(0, result.getIntegratedTraces().size());
	}

	@Test
	public void testGetIntegratorType_1() {

		assertEquals("", result.getIntegratorType());
	}

	@Test
	public void testGetModelDescription_1() {

		assertEquals("", result.getModelDescription());
	}

	@Test
	public void testGetPeakType_1() {

		assertEquals("", result.getPeakType());
	}

	@Test
	public void testGetPurity_1() {

		assertEquals(0.0f, result.getPurity(), 0);
	}

	@Test
	public void testGetSN_1() {

		assertEquals(0.0f, result.getSN(), 0);
	}

	@Test
	public void testGetStartRetentionTime_1() {

		assertEquals(0, result.getStartRetentionTime());
	}

	@Test
	public void testGetStopRetentionTime_1() {

		assertEquals(0, result.getStopRetentionTime());
	}

	@Test
	public void testGetTailing_1() {

		assertEquals(0.0f, result.getTailing(), 0);
	}

	@Test
	public void testGetWidth_1() {

		assertEquals(0, result.getWidth());
	}
}
