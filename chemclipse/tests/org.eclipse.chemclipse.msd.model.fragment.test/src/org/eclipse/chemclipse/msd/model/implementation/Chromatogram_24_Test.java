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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.junit.Test;

public class Chromatogram_24_Test {

	private IChromatogramMSD chromatogram = new ChromatogramMSD();

	@Test
	public void testGetChromatogramIntegratedArea_1() {

		assertEquals(0.0d, chromatogram.getChromatogramIntegratedArea(), 0);
	}

	@Test
	public void testGetChromatogramIntegratorDescription_1() {

		assertEquals("", chromatogram.getIntegratorDescription());
	}

	@Test
	public void testGetChromatogramIntegratorDescription_2() {

		chromatogram.setIntegratorDescription(null);
		assertEquals("", chromatogram.getIntegratorDescription());
	}

	@Test
	public void testGetChromatogramIntegratorDescription_3() {

		chromatogram.setIntegratorDescription("ChromatogramIntegrator");
		assertEquals("ChromatogramIntegrator", chromatogram.getIntegratorDescription());
	}

	@Test
	public void testGetBackgroundIntegratedArea_1() {

		assertEquals(0.0d, chromatogram.getBackgroundIntegratedArea(), 0);
	}

	@Test
	public void testGetBackgroundIntegratorDescription_1() {

		assertEquals("", chromatogram.getIntegratorDescription());
	}

	@Test
	public void testGetBackgroundIntegratorDescription_2() {

		chromatogram.setIntegratorDescription(null);
		assertEquals("", chromatogram.getIntegratorDescription());
	}

	@Test
	public void testGetBackgroundIntegratorDescription_3() {

		chromatogram.setIntegratorDescription("BackgroundIntegrator");
		assertEquals("BackgroundIntegrator", chromatogram.getIntegratorDescription());
	}
}
