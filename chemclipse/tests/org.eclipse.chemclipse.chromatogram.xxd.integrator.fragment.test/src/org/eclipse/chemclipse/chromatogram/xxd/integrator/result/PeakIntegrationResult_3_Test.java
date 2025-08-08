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

import org.junit.Test;

public class PeakIntegrationResult_3_Test {

	@Test
	public void testEquals_1() {

		PeakIntegrationResult result1 = new PeakIntegrationResult();
		PeakIntegrationResult result2 = new PeakIntegrationResult();
		assertEquals("equals", result1.hashCode(), result2.hashCode());
	}

	@Test
	public void testEquals_2() {

		PeakIntegrationResult result1 = new PeakIntegrationResult();
		result1.setStartRetentionTime(1500);
		PeakIntegrationResult result2 = new PeakIntegrationResult();
		assertFalse("equals", result1.hashCode() == result2.hashCode());
	}
}
