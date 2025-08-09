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

public class HeaderUtil_2_Test {

	private IChromatogram chromatogram = new Chromatogram();

	@Test
	public void test1() {

		HeaderUtil.setHeaderData(null, null, "");
	}

	@Test
	public void test2() {

		HeaderUtil.setHeaderData(chromatogram, null, "");
	}

	@Test
	public void test3() {

		HeaderUtil.setHeaderData(chromatogram, HeaderField.DATA_NAME, "");
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, HeaderField.DATA_NAME, ""));
	}

	@Test
	public void test4() {

		HeaderUtil.setHeaderData(chromatogram, HeaderField.DATA_NAME, "DefaultData");
		assertEquals("DefaultData", HeaderUtil.getHeaderData(chromatogram, HeaderField.DATA_NAME, "DefaultData"));
	}

	@Test
	public void test5a() {

		HeaderField headerField = HeaderField.NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Demo.ocb");
		assertEquals("Demo.ocb", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test5b() {

		HeaderField headerField = HeaderField.NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Demo.ocb", false);
		assertEquals("Chromatogram", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test5c() {

		HeaderField headerField = HeaderField.NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Demo.ocb", true);
		assertEquals("Demo.ocb", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test5d() {

		HeaderField headerField = HeaderField.NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, new File("Demo.ocb"));
		assertEquals("Demo.ocb", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test5e() {

		HeaderField headerField = HeaderField.DEFAULT;
		HeaderUtil.setHeaderData(chromatogram, headerField, new File("Demo.ocb"));
		assertEquals("Demo.ocb", HeaderUtil.getHeaderData(chromatogram, HeaderField.NAME, ""));
	}

	@Test
	public void test6() {

		HeaderField headerField = HeaderField.DATA_NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test7() {

		HeaderField headerField = HeaderField.SAMPLE_NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test8() {

		HeaderField headerField = HeaderField.SAMPLE_GROUP;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test9() {

		HeaderField headerField = HeaderField.SHORT_INFO;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test10() {

		HeaderField headerField = HeaderField.MISC_INFO;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test11() {

		HeaderField headerField = HeaderField.TAGS;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	@Test
	public void test12() {

		HeaderField headerField = HeaderField.DEFAULT;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}
}