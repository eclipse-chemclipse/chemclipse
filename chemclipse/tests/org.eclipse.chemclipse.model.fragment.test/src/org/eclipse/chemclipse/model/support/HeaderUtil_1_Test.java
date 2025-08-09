/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.support;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.model.implementation.Chromatogram;
import org.junit.Test;

public class HeaderUtil_1_Test {

	private IChromatogram chromatogram = new Chromatogram();

	@Test
	public void test1() {

		assertEquals("", HeaderUtil.getHeaderData(null, null, ""));
	}

	@Test
	public void test2() {

		assertEquals("", HeaderUtil.getHeaderData(chromatogram, null, ""));
	}

	@Test
	public void test3() {

		assertEquals("", HeaderUtil.getHeaderData(chromatogram, HeaderField.DATA_NAME, ""));
	}

	@Test
	public void test4() {

		assertEquals("DefaultData", HeaderUtil.getHeaderData(chromatogram, HeaderField.DATA_NAME, "DefaultData"));
	}

	@Test
	public void test5() {

		HeaderField headerField = HeaderField.NAME;
		assertEquals("Chromatogram", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setFile(new File("Demo.ocb"));
		assertEquals("Demo.ocb", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test6() {

		HeaderField headerField = HeaderField.DATA_NAME;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setDataName("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test7() {

		HeaderField headerField = HeaderField.SAMPLE_NAME;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setSampleName("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test8() {

		HeaderField headerField = HeaderField.SAMPLE_GROUP;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setSampleGroup("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test9() {

		HeaderField headerField = HeaderField.SHORT_INFO;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setShortInfo("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test10() {

		HeaderField headerField = HeaderField.MISC_INFO;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setMiscInfo("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test11() {

		HeaderField headerField = HeaderField.TAGS;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setTags("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test12() {

		HeaderField headerField = HeaderField.DEFAULT;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}
}