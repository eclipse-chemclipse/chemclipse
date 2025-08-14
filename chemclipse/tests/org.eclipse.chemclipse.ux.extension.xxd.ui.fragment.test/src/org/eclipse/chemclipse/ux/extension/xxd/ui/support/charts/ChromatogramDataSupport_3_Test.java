/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.implementation.ChromatogramWSD;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramDataSupport_3_Test {

	private IChromatogramWSD chromatogram = new ChromatogramWSD();

	@Before
	public void setUp() {

		setData(chromatogram);
	}

	private void setData(IChromatogram chromatogram) {

		chromatogram.setFile(new File("This"));
		chromatogram.setSampleName("sample");
		chromatogram.setDataName("is");
		chromatogram.setSampleGroup("a");
		chromatogram.setShortInfo("test.");
	}

	@Test
	public void test1() {

		assertEquals("Chromatogram", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.DEFAULT, -1, false));
	}

	@Test
	public void test2() {

		assertEquals("Master Chromatogram", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.DEFAULT, 0, false));
	}

	@Test
	public void test3() {

		assertEquals("Referenced Chromatogram (1)", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.DEFAULT, 1, false));
	}

	@Test
	public void test4() {

		assertEquals("Chromatogram [WSD]", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.DEFAULT, -1, true));
	}

	@Test
	public void test5() {

		assertEquals("Master Chromatogram [WSD]", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.DEFAULT, 0, true));
	}

	@Test
	public void test6() {

		assertEquals("Referenced Chromatogram (1) [WSD]", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.DEFAULT, 1, true));
	}

	@Test
	public void test7() {

		assertEquals("This", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.NAME, -1, false));
	}

	@Test
	public void test8() {

		assertEquals("This", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.NAME, 0, false));
	}

	@Test
	public void test9() {

		assertEquals("This", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.NAME, 1, false));
	}

	@Test
	public void test10() {

		assertEquals("This [WSD]", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.NAME, -1, true));
	}

	@Test
	public void test11() {

		assertEquals("This [WSD]", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.NAME, 0, true));
	}

	@Test
	public void test12() {

		assertEquals("This [WSD]", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.NAME, 1, true));
	}

	@Test
	public void test13() {

		assertEquals("is", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.DATA_NAME, -1, false));
	}

	@Test
	public void test14() {

		assertEquals("is [WSD]", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.DATA_NAME, -1, true));
	}

	@Test
	public void test15() {

		assertEquals("a", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.SAMPLE_GROUP, -1, false));
	}

	@Test
	public void test16() {

		assertEquals("a [WSD]", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.SAMPLE_GROUP, -1, true));
	}

	@Test
	public void test17() {

		assertEquals("test.", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.SHORT_INFO, -1, false));
	}

	@Test
	public void test18() {

		assertEquals("test. [WSD]", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.SHORT_INFO, -1, true));
	}

	@Test
	public void test19() {

		assertEquals("sample", ChromatogramDataSupport.getReferenceLabel(chromatogram, HeaderField.SAMPLE_NAME, -1, false));
	}
}
