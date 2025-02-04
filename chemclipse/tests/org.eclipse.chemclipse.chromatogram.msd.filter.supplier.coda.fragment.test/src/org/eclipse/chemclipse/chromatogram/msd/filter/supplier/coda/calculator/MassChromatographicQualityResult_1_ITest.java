/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.coda.calculator;

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

import junit.framework.TestCase;

public class MassChromatographicQualityResult_1_ITest extends TestCase {

	private IMassChromatographicQualityResult result;
	private IChromatogramMSD chromatogram;
	private IChromatogramSelectionMSD chromatogramSelection;
	private File importFile;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		importFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1));
		IProcessingInfo<IChromatogramMSD> processingInfo = ChromatogramConverterMSD.getInstance().convert(importFile, new NullProgressMonitor());
		chromatogram = processingInfo.getProcessingResult();
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Override
	protected void tearDown() throws Exception {

		importFile = null;
		chromatogram = null;
		chromatogramSelection = null;

		super.tearDown();
	}

	public void testConstructor_1() {

		try {
			result = new MassChromatographicQualityResult(chromatogramSelection, 0.7f, 3);
			float drv = result.getDataReductionValue();
			assertEquals("Data reduction value", 0.87713313f, drv);
			IMarkedIons exludedIons = result.getExcludedIons();
			assertNotNull(exludedIons);
		} catch(CodaCalculatorException e) {
			assertTrue("CodaCalculatorException", false);
		}
	}

	public void testConstructor_2() {

		try {
			result = new MassChromatographicQualityResult(chromatogramSelection, 0.7f, 5);
			float drv = result.getDataReductionValue();
			assertEquals("Data reduction value", 0.79522187f, drv);
			IMarkedIons exludedIons = result.getExcludedIons();
			assertNotNull(exludedIons);
		} catch(CodaCalculatorException e) {
			assertTrue("CodaCalculatorException", false);
		}
	}

	public void testConstructor_3() {

		try {
			result = new MassChromatographicQualityResult(chromatogramSelection, 0.7f, 7);
			float drv = result.getDataReductionValue();
			assertEquals("Data reduction value", 0.7337884f, drv);
			IMarkedIons exludedIons = result.getExcludedIons();
			assertNotNull(exludedIons);
		} catch(CodaCalculatorException e) {
			assertTrue("CodaCalculatorException", false);
		}
	}
}
