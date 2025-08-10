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

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramSelection_9_Test {

	private IChromatogramMSD chromatogram;
	private IChromatogramSelectionMSD selection;
	private IRegularMassSpectrum massSpectrum;
	private IIon ion;

	@Before
	public void setUp() throws Exception {

		chromatogram = new ChromatogramMSD();
		chromatogram.setScanDelay(500);
		chromatogram.setScanInterval(1000);
		for(int scan = 1; scan <= 10; scan++) {
			massSpectrum = new VendorMassSpectrum();
			ion = new Ion(IIon.TIC_ION, 45528.3f);
			massSpectrum.addIon(ion);
			chromatogram.addScan(massSpectrum);
		}
		chromatogram.recalculateRetentionTimes();
		/*
		 * Default values from IChromatogram will be chosen.
		 */
		selection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Test
	public void testGetSelectedScan_1() {

		IRegularMassSpectrum selectedScan = selection.getSelectedScan();
		assertNotNull(selectedScan);
		assertEquals("RetentionTime", 500, selectedScan.getRetentionTime());
	}
}
