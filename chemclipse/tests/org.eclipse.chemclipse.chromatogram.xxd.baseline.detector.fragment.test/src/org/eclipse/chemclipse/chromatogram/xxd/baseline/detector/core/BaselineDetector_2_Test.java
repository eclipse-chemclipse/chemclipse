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
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.core;

import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.exceptions.BaselineDetectorSettingsException;
import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.exceptions.NoBaselineDetectorAvailableException;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * Test the BaselineDetector.
 */
public class BaselineDetector_2_Test extends TestCase {

	IBaselineDetectorSupport support = BaselineDetector.getBaselineDetectorSupport();

	@Test
	public void testBaselineDetector_1() throws BaselineDetectorSettingsException {

		try {
			String detectorId = BaselineDetector.getBaselineDetectorSupport().getDetectorId(0);
			IProcessingInfo<?> processingInfo = BaselineDetector.setBaseline(null, null, detectorId, new NullProgressMonitor());
			assertTrue(processingInfo.hasErrorMessages());
		} catch(NoBaselineDetectorAvailableException e) {
			assertTrue("NoBaselineDetectorAvailableException", false);
		}
	}

	@Test
	public void testBaselineDetector_2() throws org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException {

		String detectorId = "";
		IChromatogramSelectionMSD chromatogramSelection = new ChromatogramSelectionMSD(new ChromatogramMSD());
		IProcessingInfo<?> processingInfo = BaselineDetector.setBaseline(chromatogramSelection, null, detectorId, new NullProgressMonitor());
		assertTrue(processingInfo.hasErrorMessages());
	}
}