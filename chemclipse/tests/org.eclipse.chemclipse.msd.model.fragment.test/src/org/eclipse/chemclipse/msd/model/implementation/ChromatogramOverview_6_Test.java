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
public class ChromatogramOverview_6_Test {

	private ChromatogramMSD chrom;
	private IChromatogramOverview chromatogram;
	private float RT_FACTOR = 1000.0f * 60.0f;

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

		float min = 12736 / RT_FACTOR;
		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(min));
	}

	@Test
	public void testGetScanNumber_2() {

		float min = 5501 / RT_FACTOR;
		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(min));
	}

	@Test
	public void testGetScanNumber_3() {

		float min = 5500 / RT_FACTOR;
		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(min));
	}

	@Test
	public void testGetScanNumber_4() {

		float min = 5499 / RT_FACTOR;
		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(min));
	}

	@Test
	public void testGetScanNumber_5() {

		float min = 0 / RT_FACTOR;
		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(min));
	}

	@Test
	public void testGetScanNumber_6() {

		float min = -1 / RT_FACTOR;
		assertEquals("ScanNumber", 0, chromatogram.getScanNumber(min));
	}
}
