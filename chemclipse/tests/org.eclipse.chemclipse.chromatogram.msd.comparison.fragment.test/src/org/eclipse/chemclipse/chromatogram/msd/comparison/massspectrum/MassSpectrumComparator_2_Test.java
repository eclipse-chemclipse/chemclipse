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

import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.junit.Test;

public class MassSpectrumComparator_2_Test {

	private IScanMSD massSpectrum1 = new ScanMSD();
	private IScanMSD massSpectrum2 = new ScanMSD();

	private boolean usePreOptimization = false;
	private double thresholdPreOptimization = 0.1d;

	@Test
	public void testMassSpectrumComparatorCompare_1() {

		IProcessingInfo<IComparisonResult> processingInfo = MassSpectrumComparator.compare(null, null, (String)null, usePreOptimization, thresholdPreOptimization);
		assertTrue(processingInfo.hasErrorMessages());
		try {
			processingInfo.getProcessingResult();
		} catch(Exception e) {
			assertTrue("Exception", true);
		}
	}

	@Test
	public void testMassSpectrumComparatorCompare_2() {

		IProcessingInfo<IComparisonResult> processingInfo = MassSpectrumComparator.compare(massSpectrum1, null, (String)null, usePreOptimization, thresholdPreOptimization);
		assertTrue(processingInfo.hasErrorMessages());
		try {
			processingInfo.getProcessingResult();
		} catch(Exception e) {
			assertTrue("Exception", true);
		}
	}

	@Test
	public void testMassSpectrumComparatorCompare_3() {

		IProcessingInfo<IComparisonResult> processingInfo = MassSpectrumComparator.compare(null, massSpectrum2, (String)null, usePreOptimization, thresholdPreOptimization);
		assertTrue(processingInfo.hasErrorMessages());
		try {
			processingInfo.getProcessingResult();
		} catch(Exception e) {
			assertTrue("Exception", true);
		}
	}

	@Test
	public void testMassSpectrumComparatorCompare_4() {

		IProcessingInfo<IComparisonResult> processingInfo = MassSpectrumComparator.compare(null, null, "?", usePreOptimization, thresholdPreOptimization);
		assertTrue(processingInfo.hasErrorMessages());
		try {
			processingInfo.getProcessingResult();
		} catch(Exception e) {
			assertTrue("Exception", true);
		}
	}

	@Test
	public void testMassSpectrumComparatorCompare_5() {

		IProcessingInfo<IComparisonResult> processingInfo = MassSpectrumComparator.compare(massSpectrum1, massSpectrum2, "?", usePreOptimization, thresholdPreOptimization);
		assertTrue(processingInfo.hasErrorMessages());
		try {
			processingInfo.getProcessingResult();
		} catch(Exception e) {
			assertTrue("Exception", true);
		}
	}

	@Test
	public void testMassSpectrumComparatorCompare_6() {

		IProcessingInfo<IComparisonResult> processingInfo = MassSpectrumComparator.compare(massSpectrum1, null, "?", usePreOptimization, thresholdPreOptimization);
		assertTrue(processingInfo.hasErrorMessages());
		try {
			processingInfo.getProcessingResult();
		} catch(Exception e) {
			assertTrue("Exception", true);
		}
	}
}
