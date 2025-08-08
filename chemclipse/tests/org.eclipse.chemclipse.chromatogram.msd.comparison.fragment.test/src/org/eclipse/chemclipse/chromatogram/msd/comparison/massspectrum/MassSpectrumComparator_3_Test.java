/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.comparison.massspectrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.chromatogram.msd.comparison.massspectrum.purity.IMassSpectrumPurityResult;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.junit.Test;

public class MassSpectrumComparator_3_Test {

	private IScanMSD massSpectrum1 = new ScanMSD();
	private IScanMSD massSpectrum2 = new ScanMSD();

	@Test
	public void testMassSpectrumComparatorCompare_1() {

		IProcessingInfo<IMassSpectrumPurityResult> processingInfo = MassSpectrumComparator.getPurityResult(null, null);
		assertTrue(processingInfo.hasErrorMessages());
	}

	@Test
	public void testMassSpectrumComparatorCompare_2() {

		IProcessingInfo<IMassSpectrumPurityResult> processingInfo = MassSpectrumComparator.getPurityResult(massSpectrum1, null);
		assertTrue(processingInfo.hasErrorMessages());
	}

	@Test
	public void testMassSpectrumComparatorCompare_3() {

		IProcessingInfo<IMassSpectrumPurityResult> processingInfo = MassSpectrumComparator.getPurityResult(null, massSpectrum2);
		assertTrue(processingInfo.hasErrorMessages());
	}

	@Test
	public void testMassSpectrumComparatorCompare_4() {

		IProcessingInfo<IMassSpectrumPurityResult> processingInfo = MassSpectrumComparator.getPurityResult(massSpectrum1, massSpectrum2);
		assertFalse(processingInfo.hasErrorMessages());
		IMassSpectrumPurityResult result = processingInfo.getProcessingResult();
		assertEquals(0.0f, result.getFitValue(), 0);
		assertEquals(0.0f, result.getReverseFitValue(), 0);
	}
}
