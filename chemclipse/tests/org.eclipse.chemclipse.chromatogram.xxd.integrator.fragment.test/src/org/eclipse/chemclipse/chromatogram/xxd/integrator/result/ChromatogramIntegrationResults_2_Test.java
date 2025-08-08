/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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

import org.junit.Before;
import org.junit.Test;

public class ChromatogramIntegrationResults_2_Test {

	private IChromatogramIntegrationResults results;
	private IChromatogramIntegrationResult result;
	private float ion;
	private double backgroundArea;
	private double chromatogramArea;

	@Before
	public void setUp() throws Exception {

		results = new ChromatogramIntegrationResults();
		ion = 18.1f;
		backgroundArea = 7823090.9d;
		chromatogramArea = 23938.54d;
		result = new ChromatogramIntegrationResult(ion, chromatogramArea, backgroundArea);
		results.add(result);
	}

	@Test
	public void testGetBackgroundArea_1() {

		assertEquals("BackgroundArea", 7823090.9d, results.getTotalBackgroundArea(), 0);
	}

	@Test
	public void testGetChromatogramArea_1() {

		assertEquals("ChromatogramArea", 23938.54d, result.getChromatogramArea(), 0);
	}
}
