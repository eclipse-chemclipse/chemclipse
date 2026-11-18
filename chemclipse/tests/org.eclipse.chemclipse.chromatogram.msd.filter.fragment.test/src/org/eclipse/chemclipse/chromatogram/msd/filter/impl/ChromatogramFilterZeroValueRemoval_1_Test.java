/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.chemclipse.chromatogram.filter.result.IChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.msd.filter.impl.settings.CoverageReference;
import org.eclipse.chemclipse.chromatogram.msd.filter.impl.settings.FilterSettingsZeroValueRemoval;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.Test;

public class ChromatogramFilterZeroValueRemoval_1_Test {

	@Test
	public void testDefaults() {

		FilterSettingsZeroValueRemoval settings = new FilterSettingsZeroValueRemoval();
		assertEquals(50, settings.getMinCoverage());
		assertEquals(CoverageReference.FIRST_SIGNAL, settings.getCoverageReference());
		assertEquals(2, settings.getMaxShortGap());
	}

	@Test
	public void testFilterWholeChromatogram() throws Exception {

		ChromatogramMSD chromatogram = IonChannelGapFiller_1_Test.chromatogram(1000, 0, 0, 0, 5000, 6000, 7000);
		IProcessingInfo<IChromatogramFilterResult> info = new ChromatogramFilterZeroValueRemoval().applyFilter(new ChromatogramSelectionMSD(chromatogram), new FilterSettingsZeroValueRemoval(), new NullProgressMonitor());
		assertFalse(info.hasErrorMessages());
		assertEquals(ResultStatus.OK, info.getProcessingResult().getResultStatus());
		assertEquals(3000.0f, IonChannelGapFiller_1_Test.abundance(chromatogram, 2, 29), 0.01f);
		assertTrue(chromatogram.isDirty());
	}

	@Test
	public void testFilterSelectionOnly() throws Exception {

		// Gaps at scans 2 and 6 (1-based); the selection covers scans 1-4 only (1000-4000 ms).
		ChromatogramMSD chromatogram = IonChannelGapFiller_1_Test.chromatogram(1000, 0, 3000, 4000, 5000, 0, 7000);
		ChromatogramSelectionMSD selection = new ChromatogramSelectionMSD(chromatogram);
		selection.setRangeRetentionTime(1000, 4000);
		new ChromatogramFilterZeroValueRemoval().applyFilter(selection, new FilterSettingsZeroValueRemoval(), new NullProgressMonitor());
		assertEquals(2000.0f, IonChannelGapFiller_1_Test.abundance(chromatogram, 1, 29), 0.01f);
		assertEquals(0.0f, IonChannelGapFiller_1_Test.abundance(chromatogram, 5, 29), 0.0f);
	}

	@Test
	public void testNothingFilledNotDirty() throws Exception {

		ChromatogramMSD chromatogram = IonChannelGapFiller_1_Test.chromatogram(1000, 2000, 3000);
		new ChromatogramFilterZeroValueRemoval().applyFilter(new ChromatogramSelectionMSD(chromatogram), new FilterSettingsZeroValueRemoval(), new NullProgressMonitor());
		assertFalse(chromatogram.isDirty());
	}
}
