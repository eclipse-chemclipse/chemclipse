/*******************************************************************************
 * Copyright (c) 2011, 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.coda.exceptions.CodaCalculatorException;
import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class MassChromatographicQualityResult_1_ITest {

	private IMassChromatographicQualityResult result;
	private IChromatogramSelectionMSD chromatogramSelection;

	@BeforeAll
	public void setUp() {

		File importFile = new File("testData/files/import/Chromatogram1.ocb");
		IProcessingInfo<IChromatogramMSD> processingInfo = ChromatogramConverterMSD.getInstance().convert(importFile, new NullProgressMonitor());
		IChromatogramMSD chromatogram = processingInfo.getProcessingResult();
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Test
	public void testConstructor_1() throws CodaCalculatorException {

		result = new MassChromatographicQualityResult(chromatogramSelection, 0.7f, 3);
		float drv = result.getDataReductionValue();
		assertEquals(0.87713313f, drv, 0, "Data reduction value");
		IMarkedTraces<ITrace> exludedIons = result.getExcludedIons();
		assertNotNull(exludedIons);
	}

	@Test
	public void testConstructor_2() throws CodaCalculatorException {

		result = new MassChromatographicQualityResult(chromatogramSelection, 0.7f, 5);
		float drv = result.getDataReductionValue();
		assertEquals(0.79522187f, drv, 0, "Data reduction value");
		IMarkedTraces<ITrace> exludedIons = result.getExcludedIons();
		assertNotNull(exludedIons);
	}

	@Test
	public void testConstructor_3() throws CodaCalculatorException {

		result = new MassChromatographicQualityResult(chromatogramSelection, 0.7f, 7);
		float drv = result.getDataReductionValue();
		assertEquals(0.7337884f, drv, 0, "Data reduction value");
		IMarkedTraces<ITrace> exludedIons = result.getExcludedIons();
		assertNotNull(exludedIons);
	}
}