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
import static org.junit.Assert.assertNull;

import java.util.List;

import org.easymock.EasyMock;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramSelection_8_Test {

	private IChromatogramMSD chromatogram;
	private IChromatogramSelectionMSD selection;
	private IRegularMassSpectrum scan;
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

		// see additional check in selection.getSelectedPeak();
		List<IChromatogramPeakMSD> peaks = EasyMock.createNiceMock(List.class);
		EasyMock.expect(peaks.add(EasyMock.createNiceMock(IChromatogramPeakMSD.class))).andReturn(true);
		EasyMock.replay(peaks);
		EasyMock.expect(chromatogram.getPeaks()).andStubReturn(peaks);

		scan = new VendorMassSpectrum();
		scan.setRetentionTime(4500);
		scan.addIon(new Ion(45.0f, 2883.9f));
		peak = EasyMock.createNiceMock(IChromatogramPeakMSD.class);
		EasyMock.expect(peak.getIntegratedArea()).andStubReturn(893002.3d);
		EasyMock.replay(peak);
		EasyMock.replay(chromatogram);
		/*
		 * Default values from IChromatogram will be chosen.
		 */
		selection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Test
	public void testSetSelectedScan_1() {

		selection.setSelectedScan(null);
		assertNull(selection.getSelectedScan());
	}

	@Test
	public void testSetSelectedScan_2() {

		selection.setSelectedScan(scan);
		scan = selection.getSelectedScan();
		assertNotNull(selection.getSelectedScan());
		assertEquals("RetentionTime", 4500, scan.getRetentionTime());
	}

	@Test
	public void testSetSelectedPeak_1() {

		selection.setSelectedPeak(null);
		assertNull(selection.getSelectedPeak());
	}

	@Test
	public void testSetSelectedPeak_2() {

		selection.setSelectedPeak(peak);
		peak = (IChromatogramPeakMSD)selection.getSelectedPeak();
		assertNotNull(peak);
		assertEquals("IntegratedArea", 893002.3d, peak.getIntegratedArea(), 0);
	}
}
