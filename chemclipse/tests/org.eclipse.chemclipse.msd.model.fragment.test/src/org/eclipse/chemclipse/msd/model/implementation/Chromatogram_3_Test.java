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

import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the interface IChromatogram.
 */
public class Chromatogram_3_Test {

	private ChromatogramMSD chromatogram;
	private Date date;
	private ITotalScanSignalExtractor totalIonSignalExtractor;
	private IExtractedIonSignalExtractor extractedIonSignalExtractor;

	@Before
	public void setUp() throws Exception {

		chromatogram = new ChromatogramMSD();
		date = new Date();

		totalIonSignalExtractor = new TotalScanSignalExtractor(chromatogram);
		extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
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
	public void testGetOperator_1() {

		assertEquals("operator", "", chromatogram.getOperator());
	}

	@Test
	public void testGetFile_1() {

		assertEquals("file", null, chromatogram.getFile());
	}

	@Test
	public void testGetName_1() {

		assertEquals("name", "Chromatogram", chromatogram.getName());
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
	public void testGetNumberOfScans_1() {

		assertEquals("numberOfScans", 0, chromatogram.getNumberOfScans());
	}

	@Test
	public void testGetNumberOfScanIons_1() {

		assertEquals("numberOfScanIons", 0, chromatogram.getNumberOfScanIons());
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
	public void testGetMinSignal_1() {

		assertEquals("minSignal", 0.0f, chromatogram.getMinSignal(), 0);
	}

	@Test
	public void testGetMaxSignal_1() {

		assertEquals("maxSignal", 0.0f, chromatogram.getMaxSignal(), 0);
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

		// Cast to int because float is not as precise as for example
		// java.lang.Math.
		assertEquals("totalIonSignal", 0, (int)chromatogram.getTotalSignal());
	}

	@Test
	public void testGetExtractedIonSignals_1() {

		IExtractedIonSignals signals = extractedIonSignalExtractor.getExtractedIonSignals();
		assertEquals("List<IExtractedIonSignal> size", 0, signals.size());
	}

	@Test
	public void testGetExtractedIonSignals_2() {

		IExtractedIonSignals signals = extractedIonSignalExtractor.getExtractedIonSignals(14.2f, 105.6f);
		assertEquals("List<IExtractedIonSignal> size", 0, signals.size());
	}
}
