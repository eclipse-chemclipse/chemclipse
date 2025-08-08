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
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

public class PeakIntegrationResults_1_Test {

	private IPeakIntegrationResults results = new PeakIntegrationResults();

	@Test
	public void testGetPeakIntegrationResult_1() {

		assertNull(results.getPeakIntegrationResult(0));
	}

	@Test
	public void testGetPeakIntegrationResultList_1() {

		List<IPeakIntegrationResult> presults = results.getPeakIntegrationResultList(55);
		assertEquals("size", 0, presults.size());
	}

	@Test
	public void testGetPeakIntegrationResultThatContains_1() {

		List<IPeakIntegrationResult> presults = results.getPeakIntegrationResultThatContains(55);
		assertEquals("size", 0, presults.size());
	}

	@Test
	public void testGetSize_1() {

		assertEquals("size", 0, results.size());
	}
}
