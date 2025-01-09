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

public class HeaderUtil_1_Test extends TestCase {

	private IChromatogram<?> chromatogram = new Chromatogram();

	public void test1() {

		assertEquals("", HeaderUtil.getHeaderData(null, null, ""));
	}

	public void test2() {

		assertEquals("", HeaderUtil.getHeaderData(chromatogram, null, ""));
	}

	public void test3() {

		assertEquals("", HeaderUtil.getHeaderData(chromatogram, HeaderField.DATA_NAME, ""));
	}

	public void test4() {

		assertEquals("DefaultData", HeaderUtil.getHeaderData(chromatogram, HeaderField.DATA_NAME, "DefaultData"));
	}

	public void test5() {

		HeaderField headerField = HeaderField.NAME;
		assertEquals("Chromatogram", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setFile(new File("Demo.ocb"));
		assertEquals("Demo.ocb", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test6() {

		HeaderField headerField = HeaderField.DATA_NAME;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setDataName("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test7() {

		HeaderField headerField = HeaderField.SAMPLE_NAME;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setSampleName("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test8() {

		HeaderField headerField = HeaderField.SAMPLE_GROUP;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setSampleGroup("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test9() {

		HeaderField headerField = HeaderField.SHORT_INFO;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setShortInfo("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test10() {

		HeaderField headerField = HeaderField.MISC_INFO;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setMiscInfo("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test11() {

		HeaderField headerField = HeaderField.TAGS;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
		chromatogram.setTags("Test");
		assertEquals("Test", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}

	public void test12() {

		HeaderField headerField = HeaderField.DEFAULT;
		assertEquals("", HeaderUtil.getHeaderData(chromatogram, headerField, ""));
	}
}