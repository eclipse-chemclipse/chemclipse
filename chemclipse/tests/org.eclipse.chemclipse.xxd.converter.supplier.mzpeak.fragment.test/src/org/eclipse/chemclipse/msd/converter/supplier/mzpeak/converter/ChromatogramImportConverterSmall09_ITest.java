/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzpeak.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ChromatogramImportConverterSmall09_ITest {

	private IChromatogramMSD chromatogram;

	@BeforeAll
	public void setUp() {

		File importFile = new File("testData/small.mzpeak");
		ChromatogramImportConverter converter = new ChromatogramImportConverter();
		IProcessingInfo<IChromatogramMSD> processingInfo = converter.convert(importFile, new NullProgressMonitor());
		chromatogram = processingInfo.getProcessingResult();
	}

	@Test
	public void testImport() {

		assertNotNull(chromatogram);
	}

	@Test
	public void testInstrument() {

		assertEquals("LTQ FT", chromatogram.getInstrument());
	}

	@Test
	public void testScans() {

		assertEquals(48, chromatogram.getNumberOfScans());
	}

	@Test
	public void testIons() {

		chromatogram.enforceLoadScanProxies(new NullProgressMonitor());
		assertEquals(25344, chromatogram.getNumberOfScanIons());
	}
}
