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
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the interface IChromatogramOverview.
 */
public class ChromatogramOverview_1_Test {

	private ChromatogramMSD chrom;
	private IChromatogramOverview chromatogram;
	private Date date;
	private ITotalScanSignalExtractor totalIonSignalExtractor;

	@Before
	public void setUp() {

		chrom = new ChromatogramMSD();
		date = new Date();
		chromatogram = chrom;

		totalIonSignalExtractor = new TotalScanSignalExtractor(chrom);
	}

	@Test
	public void testGetMinSignal_1() {

		assertEquals("minSignal", 0, chromatogram.getMinSignal(), 0);
	}

	@Test
	public void testGetMaxSignal_1() {

		assertEquals("maxSignal", 0, chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testGetStartRetentionTime_1() {

		assertEquals("startRetentionTime", 0, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testGetStopRetentionTime_1() {

		assertEquals("stopRetentionTime", 0, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testGetScanDelay_1() {

		assertEquals("scanDelay", 4500, chromatogram.getScanDelay());
	}

	@Test
	public void testGetScanInterval_1() {

		assertEquals("scanInterval", 1000, chromatogram.getScanInterval());
	}

	@Test
	public void testGetNumberOfScans_1() {

		assertEquals("numberOfScans", 0, chromatogram.getNumberOfScans());
	}

	@Test
	public void testGetName_1() {

		assertEquals("name", "Chromatogram", chromatogram.getName());
	}

	@Test
	public void testGetFile_1() {

		assertEquals("file", null, chromatogram.getFile());
	}

	@Test
	public void testGetOperator_1() {

		assertEquals("operator", "", chromatogram.getOperator());
	}

	@Test
	public void testGetDate_1() {

		assertTrue("date", chromatogram.getDate() != null);
	}

	@Test
	public void testGetDate_2() {

		chromatogram.setDate(date);
		assertEquals("date", date.toString(), chromatogram.getDate().toString());
	}

	@Test
	public void testGetMiscInfo_1() {

		assertEquals("miscInfo", "", chromatogram.getMiscInfo());
	}

	@Test
	public void testGetTotalIonSignals_1() {

		ITotalScanSignals signals = totalIonSignalExtractor.getTotalScanSignals();
		assertEquals("List<ITotalIonSignal> size", 0, signals.size());
	}

	@Test
	public void testGetTotalIonSignal_1() {

		assertEquals("totalIonSignal", 0, chromatogram.getTotalSignal(), 0);
	}
}
