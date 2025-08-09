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

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.junit.Test;

public class Chromatogram_5_Test {

	private IChromatogram chromatogram = new Chromatogram();

	@Test
	public void test_1() {

		assertEquals("", chromatogram.getMiscInfo());
		assertEquals("", chromatogram.getMiscInfoSeparated());
	}

	@Test
	public void test_2() {

		chromatogram.setMiscInfo("Hello World");
		chromatogram.setMiscInfo(null);
		assertEquals("", chromatogram.getMiscInfo());
		assertEquals("", chromatogram.getMiscInfoSeparated());
	}

	@Test
	public void test_3() {

		chromatogram.setMiscInfo("Hello World");
		assertEquals("Hello World", chromatogram.getMiscInfo());
		assertEquals("", chromatogram.getMiscInfoSeparated());
	}

	@Test
	public void test_4() {

		chromatogram.setMiscInfo("Hello World!How ya doing?");
		assertEquals("Hello World", chromatogram.getMiscInfo());
		assertEquals("How ya doing?", chromatogram.getMiscInfoSeparated());
	}

	@Test
	public void test_5() {

		chromatogram.setMiscInfo("Hello World!How ya doing?!What a day");
		assertEquals("Hello World", chromatogram.getMiscInfo());
		assertEquals("How ya doing? What a day", chromatogram.getMiscInfoSeparated());
	}

	@Test
	public void test_6() {

		chromatogram.setMiscInfo("Hello World!How ya doing?!What a day!");
		assertEquals("Hello World", chromatogram.getMiscInfo());
		assertEquals("How ya doing? What a day", chromatogram.getMiscInfoSeparated());
	}
}
