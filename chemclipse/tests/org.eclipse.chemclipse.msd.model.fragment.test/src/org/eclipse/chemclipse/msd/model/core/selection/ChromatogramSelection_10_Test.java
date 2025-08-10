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
package org.eclipse.chemclipse.msd.model.core.selection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramSelection_10_Test {

	private IChromatogramMSD chromatogram;
	private IChromatogramSelectionMSD selection;
	private IChromatogramPeakMSD peak;

	@Before
	public void setUp() throws Exception {

		/*
		 * Use createNiceMock if you use void methods that are not important to
		 * test.
		 */
		chromatogram = EasyMock.createNiceMock(IChromatogramMSD.class);
		EasyMock.expect(chromatogram.getStartRetentionTime()).andStubReturn(1);
		EasyMock.expect(chromatogram.getStopRetentionTime()).andStubReturn(100);
		EasyMock.expect(chromatogram.getMaxSignal()).andStubReturn(127500.0f);
		peak = EasyMock.createNiceMock(IChromatogramPeakMSD.class);
		EasyMock.expect(peak.getIntegratedArea()).andStubReturn(893002.3d);
		EasyMock.replay(peak);
		List<IChromatogramPeakMSD> peaks = new ArrayList<>();
		peaks.add(peak);
		/*
		 * When the chromatogram selection will initialized, the first peak will
		 * be taken if available.
		 */
		EasyMock.expect(chromatogram.getPeaks()).andStubReturn(peaks);
		EasyMock.replay(chromatogram);
		/*
		 * Default values from IChromatogram will be chosen.
		 */
		selection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Test
	public void testGetSelectedPeak_1() {

		IChromatogramPeakMSD selectedPeak = (IChromatogramPeakMSD)selection.getSelectedPeak();
		assertNotNull(selectedPeak);
		assertEquals("IntegratedArea", 893002.3d, selectedPeak.getIntegratedArea(), 0);
	}
}
