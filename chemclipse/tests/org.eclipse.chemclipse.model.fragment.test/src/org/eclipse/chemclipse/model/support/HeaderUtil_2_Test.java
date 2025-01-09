/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.support;

import java.io.File;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.model.implementation.Chromatogram;

import junit.framework.TestCase;

public class HeaderUtil_2_Test extends TestCase {

	private IChromatogram<?> chromatogram = new Chromatogram();

	public void test1() {

		try {
			HeaderUtil.setHeaderData(null, null, "");
		} catch(Exception e) {
			assertTrue(false);
		}
	}

	public void test2() {

		try {
			HeaderUtil.setHeaderData(chromatogram, null, "");
		} catch(Exception e) {
			assertTrue(false);
		}
	}

	public void test3() {

		HeaderUtil.setHeaderData(chromatogram, HeaderField.DATA_NAME, "");
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, HeaderField.DATA_NAME, ""));
	}

	public void test4() {

		HeaderUtil.setHeaderData(chromatogram, HeaderField.DATA_NAME, "DefaultData");
		assertEquals("DefaultData", HeaderUtil.getHeaderData(chromatogram, HeaderField.DATA_NAME, "DefaultData"));
	}

	public void test5a() {

		HeaderField headerField = HeaderField.NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Demo.ocb");
		assertEquals("Demo.ocb", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test5b() {

		HeaderField headerField = HeaderField.NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Demo.ocb", false);
		assertEquals("Chromatogram", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test5c() {

		HeaderField headerField = HeaderField.NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Demo.ocb", true);
		assertEquals("Demo.ocb", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test5d() {

		HeaderField headerField = HeaderField.NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, new File("Demo.ocb"));
		assertEquals("Demo.ocb", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test5e() {

		HeaderField headerField = HeaderField.DEFAULT;
		HeaderUtil.setHeaderData(chromatogram, headerField, new File("Demo.ocb"));
		assertEquals("Demo.ocb", HeaderUtil.getHeaderData(chromatogram, HeaderField.NAME, ""));
	}

	public void test6() {

		HeaderField headerField = HeaderField.DATA_NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test7() {

		HeaderField headerField = HeaderField.SAMPLE_NAME;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test8() {

		HeaderField headerField = HeaderField.SAMPLE_GROUP;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test9() {

		HeaderField headerField = HeaderField.SHORT_INFO;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test10() {

		HeaderField headerField = HeaderField.MISC_INFO;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test11() {

		HeaderField headerField = HeaderField.TAGS;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test12() {

		HeaderField headerField = HeaderField.DEFAULT;
		HeaderUtil.setHeaderData(chromatogram, headerField, "Test");
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}
}