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
import org.eclipse.chemclipse.model.core.support.ColumnField;
import org.eclipse.chemclipse.model.implementation.Chromatogram;
import org.junit.Test;

public class ColumnUtil_1_Test {

	private IChromatogram chromatogram = new Chromatogram();

	@Test
	public void test1() {

		assertEquals("", ColumnUtil.getColumnData(null, null, ""));
	}

	@Test
	public void test2() {

		assertEquals("", ColumnUtil.getColumnData(chromatogram, null, ""));
	}

	@Test
	public void test3() {

		assertEquals("", ColumnUtil.getColumnData(chromatogram, ColumnField.DATA_NAME, ""));
	}

	@Test
	public void test4() {

		assertEquals("DefaultData", ColumnUtil.getColumnData(chromatogram, ColumnField.DATA_NAME, "DefaultData"));
	}

	@Test
	public void test5() {

		ColumnField columnField = ColumnField.NAME;
		assertEquals("Chromatogram", ColumnUtil.getColumnData(chromatogram, columnField, ""));
		chromatogram.setFile(new File("Demo.ocb"));
		assertEquals("Demo.ocb", ColumnUtil.getColumnData(chromatogram, columnField, ""));
	}

	@Test
	public void test6() {

		ColumnField columnField = ColumnField.DATA_NAME;
		assertEquals("", ColumnUtil.getColumnData(chromatogram, columnField, ""));
		chromatogram.setDataName("Test");
		assertEquals("Test", ColumnUtil.getColumnData(chromatogram, columnField, ""));
	}

	@Test
	public void test7() {

		ColumnField columnField = ColumnField.SAMPLE_NAME;
		assertEquals("", ColumnUtil.getColumnData(chromatogram, columnField, ""));
		chromatogram.setSampleName("Test");
		assertEquals("Test", ColumnUtil.getColumnData(chromatogram, columnField, ""));
	}

	@Test
	public void test8() {

		ColumnField columnField = ColumnField.SAMPLE_GROUP;
		assertEquals("", ColumnUtil.getColumnData(chromatogram, columnField, ""));
		chromatogram.setSampleGroup("Test");
		assertEquals("Test", ColumnUtil.getColumnData(chromatogram, columnField, ""));
	}

	@Test
	public void test9() {

		ColumnField columnField = ColumnField.SHORT_INFO;
		assertEquals("", ColumnUtil.getColumnData(chromatogram, columnField, ""));
		chromatogram.setShortInfo("Test");
		assertEquals("Test", ColumnUtil.getColumnData(chromatogram, columnField, ""));
	}

	@Test
	public void test10() {

		ColumnField columnField = ColumnField.MISC_INFO;
		assertEquals("", ColumnUtil.getColumnData(chromatogram, columnField, ""));
		chromatogram.setMiscInfo("Test");
		assertEquals("Test", ColumnUtil.getColumnData(chromatogram, columnField, ""));
	}

	@Test
	public void test11() {

		ColumnField columnField = ColumnField.TAGS;
		assertEquals("", ColumnUtil.getColumnData(chromatogram, columnField, ""));
		chromatogram.setTags("Test");
		assertEquals("Test", ColumnUtil.getColumnData(chromatogram, columnField, ""));
	}

	@Test
	public void test12() {

		ColumnField columnField = ColumnField.DEFAULT;
		assertEquals("", ColumnUtil.getColumnData(chromatogram, columnField, ""));
	}

	@Test
	public void test13() {

		ColumnField columnField = ColumnField.COLUMN_DETAILS;
		assertEquals("", ColumnUtil.getColumnData(chromatogram, columnField, ""));
		chromatogram.setColumnDetails("FFAP");
		assertEquals("FFAP", ColumnUtil.getColumnData(chromatogram, columnField, ""));
	}
}