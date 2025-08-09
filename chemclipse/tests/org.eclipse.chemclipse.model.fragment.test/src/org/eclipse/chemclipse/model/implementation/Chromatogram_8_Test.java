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

import org.junit.Before;
import org.junit.Test;

public class Chromatogram_8_Test {

	private Chromatogram chromatogram;

	@Before
	public void setUp() throws Exception {

		chromatogram = new Chromatogram();
		chromatogram.setActiveBaseline("MyBaseline");
	}

	@Test
	public void test1() {

		assertNotNull(chromatogram.getBaselineModel());
	}

	@Test
	public void test2() {

		assertEquals("MyBaseline", chromatogram.getActiveBaseline());
	}

	@Test
	public void test3() {

		assertEquals(2, chromatogram.getBaselineIds().size());
	}

	@Test
	public void test4() {

		assertTrue(chromatogram.getBaselineIds().contains("Default"));
		assertTrue(chromatogram.getBaselineIds().contains("MyBaseline"));
	}

	@Test
	public void test5() {

		chromatogram.setActiveBaseline(null);
		assertEquals("MyBaseline", chromatogram.getActiveBaseline());
	}

	@Test
	public void test6() {

		chromatogram.setActiveBaseline("");
		assertEquals("MyBaseline", chromatogram.getActiveBaseline());
	}

	@Test
	public void test7() {

		assertEquals(2, chromatogram.getBaselineIds().size());
		chromatogram.removeBaseline("");
		assertEquals("MyBaseline", chromatogram.getActiveBaseline());
		chromatogram.removeBaseline("MyBaseline");
		assertEquals(1, chromatogram.getBaselineIds().size());
		assertEquals("Default", chromatogram.getActiveBaseline());
	}

	@Test
	public void test8() {

		assertEquals(2, chromatogram.getBaselineIds().size());
		chromatogram.removeBaseline(null);
		assertEquals("MyBaseline", chromatogram.getActiveBaseline());
		chromatogram.removeBaseline("MyBaseline");
		assertEquals(1, chromatogram.getBaselineIds().size());
		assertEquals("Default", chromatogram.getActiveBaseline());
	}
}
