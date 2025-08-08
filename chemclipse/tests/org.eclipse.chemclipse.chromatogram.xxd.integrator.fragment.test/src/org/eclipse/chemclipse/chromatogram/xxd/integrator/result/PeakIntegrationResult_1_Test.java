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

import org.junit.Test;

public class PeakIntegrationResult_1_Test {

	private IPeakIntegrationResult result1 = new PeakIntegrationResult();
	private IPeakIntegrationResult result2 = new PeakIntegrationResult();

	@Test
	public void testEquals_1() {

		assertEquals("equals", result1, result2);
	}

	@Test
	public void testEquals_2() {

		assertEquals("equals", result2, result1);
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
