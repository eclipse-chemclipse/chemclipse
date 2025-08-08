/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.rtshifter.core.internal.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.rtshifter.exceptions.FilterException;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.rtshifter.settings.FilterSettingsStretch;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramFilterStretch_1_Test extends ChromatogramTestCase {

	private IChromatogramMSD chromatogram;
	private IChromatogramSelectionMSD chromatogramSelection;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		chromatogram = getChromatogram();
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Test
	public void test_1() {

		IRegularMassSpectrum scan;
		/*
		 * PRE TESTS
		 */
		assertEquals(10, chromatogram.getNumberOfScans());
		assertEquals(1500, chromatogram.getScanDelay());
		scan = chromatogram.getSupplierScan(1);
		assertEquals(1500, scan.getRetentionTime());
		scan = chromatogram.getSupplierScan(10);
		assertEquals(10500, scan.getRetentionTime());
	}

	@Test
	public void test_2() {

		IRegularMassSpectrum scan;
		try {
			FilterSettingsStretch settings = new FilterSettingsStretch();
			settings.setScanDelay(3200);
			settings.setChromatogramLength(60000);
			//
			RetentionTimeStretcher.stretchChromatogram(chromatogramSelection, settings);
			assertEquals(10, chromatogram.getNumberOfScans());
			assertEquals(3200, chromatogram.getScanDelay());
			scan = chromatogram.getSupplierScan(1);
			assertEquals(3200, scan.getRetentionTime());
			scan = chromatogram.getSupplierScan(10);
			assertEquals(59999, scan.getRetentionTime());
		} catch(FilterException e) {
			assertTrue("FilterException", false);
		}
	}
}
