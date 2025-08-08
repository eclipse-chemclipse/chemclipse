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

import org.junit.Test;

public class ChromatogramIntegrationResults_3_Test {

	private IChromatogramIntegrationResults results = new ChromatogramIntegrationResults();

	@Test
	public void testGetBackgroundArea_1() {

		assertEquals("BackgroundArea", 0.0d, results.getTotalBackgroundArea(), 0);
	}

	@Test
	public void testGetChromatogramArea_1() {

		assertEquals("ChromatogramArea", 0.0d, results.getTotalChromatogramArea(), 0);
	}
}
