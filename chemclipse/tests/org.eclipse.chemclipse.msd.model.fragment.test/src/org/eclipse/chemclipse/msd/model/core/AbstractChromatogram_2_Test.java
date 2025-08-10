/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.core;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.msd.model.TestPathHelper;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.junit.Test;

public class AbstractChromatogram_2_Test {

	private ChromatogramMSD chromatogram = new ChromatogramMSD();
	private String nameDefault = "default";

	@Test
	public void testExtractNameFromFile_1() {

		chromatogram.setFile(new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_FILE_10102040_CDF)));
		assertEquals("10.102040", chromatogram.extractNameFromFile(nameDefault));
	}

	@Test
	public void testExtractNameFromFile_2() {

		chromatogram.setFile(new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_FILE_102040_MZXML)));
		assertEquals("102040", chromatogram.extractNameFromFile(nameDefault));
	}

	@Test
	public void testExtractNameFromFile_3() {

		chromatogram.setFile(new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_FILE_2012102040_SMS)));
		assertEquals("2012.102040", chromatogram.extractNameFromFile(nameDefault));
	}

	@Test
	public void testExtractNameFromFile_4() {

		chromatogram.setFile(new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_FILE_201220102078_SMS)));
		assertEquals("2012.20.102078", chromatogram.extractNameFromFile(nameDefault));
	}

	@Test
	public void testExtractNameFromFile_5() {

		chromatogram.setFile(new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_FILE_POLYETHYLENE_CDF)));
		assertEquals("polyethylene", chromatogram.extractNameFromFile(nameDefault));
	}

	@Test
	public void testExtractNameFromFile_6() {

		chromatogram.setFile(new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_FILE_POLYETHYLENE_TEST)));
		assertEquals(nameDefault, chromatogram.extractNameFromFile(nameDefault));
	}
}
