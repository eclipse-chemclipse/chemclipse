/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.junit.Test;

public class Chromatogram_6_Test {

	private IChromatogram chromatogram = new Chromatogram();

	@Test
	public void test_1() {

		assertNotNull(chromatogram.getHeaderDataMap());
	}

	@Test
	public void test_2() {

		assertEquals(17, chromatogram.getHeaderDataMap().size());
	}

	@Test
	public void test_3() {

		chromatogram.putHeaderData("Test", "This is a test case.");
		assertEquals("This is a test case.", chromatogram.getHeaderData("Test"));
	}

	@Test
	public void test_4() {

		chromatogram.putHeaderData("Test", "This is a test case.");
		assertEquals("This is a test case.", chromatogram.getHeaderData("Test"));
	}
}