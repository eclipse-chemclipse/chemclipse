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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class PeakIntegrationResult_2_Test {

	private IPeakIntegrationResult result1;
	private IPeakIntegrationResult result2;

	@Before
	public void setUp() throws Exception {

		result1 = new PeakIntegrationResult();
		result1.setStartRetentionTime(1500);
		result1.setStopRetentionTime(1700);

		result2 = new PeakIntegrationResult();
		result2.setStartRetentionTime(1500);
		result2.setStopRetentionTime(1600);
	}

	@Test
	public void testEquals_1() {

		assertNotEquals("equals", result1, result2);
	}

	@Test
	public void testEquals_2() {

		assertNotEquals("equals", result2, result1);
	}

	@Test
	public void testEquals_3() {

		assertEquals("equals", result1, result1);
	}

	@Test
	public void testEquals_4() {

		assertNotNull("equals", result1);
	}

	@Test
	public void testEquals_5() {

		assertNotEquals("equals", result2, new Object());
	}
}
