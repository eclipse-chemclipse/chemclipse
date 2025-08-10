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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the interface IChromatogramOverview with 1 scan.
 */
public class ChromatogramOverview_4_Test {

	private ChromatogramMSD chrom;
	private IChromatogramOverview chromatogram;

	@Before
	public void setUp() throws Exception {

		chrom = new ChromatogramMSD();
		chromatogram = chrom;
		// There is no mass spectrum added to the chromatogram.
		chromatogram.setScanDelay(5500);
		chromatogram.setScanInterval(1500);
		chromatogram.recalculateRetentionTimes();
	}

	@Test
	public void testGetScanNumber_1() {

		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(12736));
	}

	@Test
	public void testGetScanNumber_2() {

		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(5501));
	}

	@Test
	public void testGetScanNumber_3() {

		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(5500));
	}

	@Test
	public void testGetScanNumber_4() {

		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(5499));
	}

	@Test
	public void testGetScanNumber_5() {

		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(0));
	}

	@Test
	public void testGetScanNumber_6() {

		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(-1));
	}
}
