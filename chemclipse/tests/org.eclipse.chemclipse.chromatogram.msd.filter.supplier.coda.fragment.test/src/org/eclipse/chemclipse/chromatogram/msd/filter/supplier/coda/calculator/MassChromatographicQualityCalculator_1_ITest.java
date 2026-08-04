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
import static org.junit.jupiter.api.Assertions.assertThrows;

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
public class MassChromatographicQualityCalculator_1_ITest {

	private IMassChromatographicQualityResult result;
	private IChromatogramSelectionMSD chromatogramSelection;
	private float codaThreshold;
	private int windowSize = 3;

	@BeforeAll
	public void setUp() {

		File importFile = new File("testData/files/import/Chromatogram1.ocb");
		IProcessingInfo<IChromatogramMSD> processingInfo = ChromatogramConverterMSD.getInstance().convert(importFile, new NullProgressMonitor());
		IChromatogramMSD chromatogram = processingInfo.getProcessingResult();
		codaThreshold = 0.7f;
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Test
	public void testGetMassChromatographicQualityResult_1() throws CodaCalculatorException {

		result = MassChromatographicQualityCalculator.calculate(chromatogramSelection, codaThreshold, windowSize);
		assertNotNull(result);
		float drv = result.getDataReductionValue();
		assertEquals(0.87713313f, drv, 0, "Data reduction value");
		IMarkedTraces<ITrace> exludedIons = result.getExcludedIons();
		assertNotNull(exludedIons);
	}

	@Test
	public void testGetMassChromatographicQualityResult_2() {

		assertThrows(CodaCalculatorException.class, () -> {
			result = MassChromatographicQualityCalculator.calculate(null, codaThreshold, windowSize);
		});
	}

	@Test
	public void testGetMassChromatographicQualityResult_3() throws CodaCalculatorException {

		result = MassChromatographicQualityCalculator.calculate(chromatogramSelection, -1, windowSize);
	}

	@Test
	public void testGetMassChromatographicQualityResult_4() throws CodaCalculatorException {

		result = MassChromatographicQualityCalculator.calculate(chromatogramSelection, codaThreshold, 0);
	}
}