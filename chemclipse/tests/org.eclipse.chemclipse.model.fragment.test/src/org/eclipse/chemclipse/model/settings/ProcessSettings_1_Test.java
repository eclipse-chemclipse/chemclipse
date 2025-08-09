/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.settings;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.implementation.Chromatogram;
import org.junit.Before;
import org.junit.Test;

public class ProcessSettings_1_Test {

	private ProcessSettingsTest processSettings = new ProcessSettingsTest();
	private IChromatogram chromatogram;

	@Before
	public void setUp() throws Exception {

		chromatogram = new Chromatogram();
		chromatogram.setFile(new File("Chromatogram"));
		chromatogram.putHeaderData("Data Name", "This is");
		chromatogram.putHeaderData("Sample Group", "a group name");
		chromatogram.putHeaderData("Short Info", "and short info demo");
	}

	@Test
	public void test1() {

		String fileNamePattern = "{chromatogram_name}{extension}";
		String fileName = processSettings.getFileName(chromatogram, fileNamePattern, ".ocb");
		assertEquals("Chromatogram.ocb", fileName);
	}

	@Test
	public void test2() {

		String fileNamePattern = "{chromatogram_dataname}{extension}";
		String fileName = processSettings.getFileName(chromatogram, fileNamePattern, ".ocb");
		assertEquals("This is.ocb", fileName);
	}

	@Test
	public void test3() {

		String fileNamePattern = "{chromatogram_samplegroup}{extension}";
		String fileName = processSettings.getFileName(chromatogram, fileNamePattern, ".ocb");
		assertEquals("a group name.ocb", fileName);
	}

	@Test
	public void test4() {

		String fileNamePattern = "{chromatogram_shortinfo}{extension}";
		String fileName = processSettings.getFileName(chromatogram, fileNamePattern, ".ocb");
		assertEquals("and short info demo.ocb", fileName);
	}

	@Test
	public void test5() {

		String fileNamePattern = "{chromatogram_dataname} {chromatogram_samplegroup} {chromatogram_shortinfo}{extension}";
		String fileName = processSettings.getFileName(chromatogram, fileNamePattern, ".ocb");
		assertEquals("This is a group name and short info demo.ocb", fileName);
	}
}
