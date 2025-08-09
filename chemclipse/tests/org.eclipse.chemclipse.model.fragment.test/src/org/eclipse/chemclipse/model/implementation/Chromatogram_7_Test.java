/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Chromatogram_7_Test {

	private Chromatogram chromatogram = new Chromatogram();

	@Test
	public void test1() {

		assertNotNull(chromatogram.getBaselineModel());
	}

	@Test
	public void test2() {

		assertEquals("Default", chromatogram.getActiveBaseline());
	}

	@Test
	public void test3() {

		assertEquals(1, chromatogram.getBaselineIds().size());
	}

	@Test
	public void test4() {

		assertTrue(chromatogram.getBaselineIds().contains("Default"));
	}

	@Test
	public void test5() {

		chromatogram.setActiveBaseline(null);
		assertEquals("Default", chromatogram.getActiveBaseline());
	}

	@Test
	public void test6() {

		chromatogram.setActiveBaseline("");
		assertEquals("Default", chromatogram.getActiveBaseline());
	}

	@Test
	public void test7() {

		chromatogram.removeBaseline("");
		assertEquals("Default", chromatogram.getActiveBaseline());
	}

	@Test
	public void test8() {

		chromatogram.removeBaseline(null);
		assertEquals("Default", chromatogram.getActiveBaseline());
	}
}
