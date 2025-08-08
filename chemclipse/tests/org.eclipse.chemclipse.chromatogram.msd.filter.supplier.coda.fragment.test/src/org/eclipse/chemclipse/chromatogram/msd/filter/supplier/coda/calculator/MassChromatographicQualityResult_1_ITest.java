/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.coda.calculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.coda.TestPathHelper;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.coda.exceptions.CodaCalculatorException;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class MassChromatographicQualityResult_1_ITest {

	private IMassChromatographicQualityResult result;
	private IChromatogramMSD chromatogram;
	private IChromatogramSelectionMSD chromatogramSelection;
	private File importFile;

	@Before
	public void setUp() throws Exception {

		importFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1));
		IProcessingInfo<IChromatogramMSD> processingInfo = ChromatogramConverterMSD.getInstance().convert(importFile, new NullProgressMonitor());
		chromatogram = processingInfo.getProcessingResult();
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Test
	public void testConstructor_1() {

		try {
			result = new MassChromatographicQualityResult(chromatogramSelection, 0.7f, 3);
			float drv = result.getDataReductionValue();
			assertEquals("Data reduction value", 0.87713313f, drv, 0);
			IMarkedIons exludedIons = result.getExcludedIons();
			assertNotNull(exludedIons);
		} catch(CodaCalculatorException e) {
			assertTrue("CodaCalculatorException", false);
		}
	}

	@Test
	public void testConstructor_2() {

		try {
			result = new MassChromatographicQualityResult(chromatogramSelection, 0.7f, 5);
			float drv = result.getDataReductionValue();
			assertEquals("Data reduction value", 0.79522187f, drv, 0);
			IMarkedIons exludedIons = result.getExcludedIons();
			assertNotNull(exludedIons);
		} catch(CodaCalculatorException e) {
			assertTrue("CodaCalculatorException", false);
		}
	}

	@Test
	public void testConstructor_3() {

		try {
			result = new MassChromatographicQualityResult(chromatogramSelection, 0.7f, 7);
			float drv = result.getDataReductionValue();
			assertEquals("Data reduction value", 0.7337884f, drv, 0);
			IMarkedIons exludedIons = result.getExcludedIons();
			assertNotNull(exludedIons);
		} catch(CodaCalculatorException e) {
			assertTrue("CodaCalculatorException", false);
		}
	}
}
