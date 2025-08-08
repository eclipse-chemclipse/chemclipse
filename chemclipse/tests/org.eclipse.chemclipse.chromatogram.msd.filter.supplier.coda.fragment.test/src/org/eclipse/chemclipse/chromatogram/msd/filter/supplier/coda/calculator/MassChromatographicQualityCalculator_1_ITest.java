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

public class MassChromatographicQualityCalculator_1_ITest {

	private IMassChromatographicQualityResult result;
	private IChromatogramMSD chromatogram;
	private IChromatogramSelectionMSD chromatogramSelection;
	private float codaThreshold;
	private int windowSize = 3;
	private File importFile;

	@Before
	public void setUp() {

		importFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1));
		IProcessingInfo<IChromatogramMSD> processingInfo = ChromatogramConverterMSD.getInstance().convert(importFile, new NullProgressMonitor());
		chromatogram = processingInfo.getProcessingResult();
		codaThreshold = 0.7f;
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Test
	public void testGetMassChromatographicQualityResult_1() {

		try {
			result = MassChromatographicQualityCalculator.calculate(chromatogramSelection, codaThreshold, windowSize);
			assertNotNull(result);
			float drv = result.getDataReductionValue();
			assertEquals("Data reduction value", 0.87713313f, drv, 0);
			IMarkedIons exludedIons = result.getExcludedIons();
			assertNotNull(exludedIons);
		} catch(CodaCalculatorException e) {
			assertTrue("CodaCalculatorException", false);
		}
	}

	@Test
	public void testGetMassChromatographicQualityResult_2() {

		try {
			result = MassChromatographicQualityCalculator.calculate(null, codaThreshold, windowSize);
		} catch(CodaCalculatorException e) {
			assertTrue("CodaCalculatorException", true);
		}
	}

	@Test
	public void testGetMassChromatographicQualityResult_3() {

		try {
			result = MassChromatographicQualityCalculator.calculate(chromatogramSelection, -1, windowSize);
		} catch(CodaCalculatorException e) {
			assertTrue("CodaCalculatorException", true);
		}
	}

	@Test
	public void testGetMassChromatographicQualityResult_4() {

		try {
			result = MassChromatographicQualityCalculator.calculate(chromatogramSelection, codaThreshold, 0);
		} catch(CodaCalculatorException e) {
			assertTrue("CodaCalculatorException", true);
		}
	}
}
