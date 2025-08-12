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

import java.io.File;
import java.util.Date;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the interface IChromatogramOverview with 1 scan.
 */
public class ChromatogramOverview_2_Test {

	private ChromatogramMSD chrom;
	private IChromatogramOverview chromatogram;
	private VendorMassSpectrum supplierMassSpectrum;
	private IIon ion;
	private Date date;
	private ITotalScanSignalExtractor totalIonSignalExtractor;

	@Before
	public void setUp() throws Exception {

		chrom = new ChromatogramMSD();
		// ------------------------------Scan 1
		supplierMassSpectrum = new VendorMassSpectrum();
		supplierMassSpectrum.setRetentionTime(7896);
		ion = new Ion(IIon.TIC_ION, 470746.9f);
		supplierMassSpectrum.addIon(ion);
		chrom.addScan(supplierMassSpectrum);
		// ------------------------------Scan 1
		chromatogram = chrom;
		date = new Date();
		chromatogram.setDate(date);
		chromatogram.setFile(new File(""));
		chromatogram.setMiscInfo("This is a test chromatogram.");
		chromatogram.setOperator("eselmeister");
		chromatogram.setScanDelay(5500);
		chromatogram.setScanInterval(1500);

		totalIonSignalExtractor = new TotalScanSignalExtractor(chrom);
	}

	@Test
	public void testGetMinSignal_1() {

		assertEquals("minSignal", 470746, (int)chromatogram.getMinSignal(), 0);
	}

	@Test
	public void testGetMaxSignal_1() {

		assertEquals("maxSignal", 470746, (int)chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testGetStartRetentionTime_1() {

		assertEquals("startRetentionTime", 7896, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testGetStopRetentionTime_1() {

		assertEquals("stopRetentionTime", 7896, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testGetScanDelay_1() {

		assertEquals("scanDelay", 5500, chromatogram.getScanDelay());
	}

	@Test
	public void testGetScanInterval_1() {

		assertEquals("scanInterval", 1500, chromatogram.getScanInterval());
	}

	@Test
	public void testGetNumberOfScans_1() {

		assertEquals("numberOfScans", 1, chromatogram.getNumberOfScans());
	}

	@Test
	public void testGetName_1() {

		assertEquals("name", "", chromatogram.getName());
	}

	@Test
	public void testGetFile_1() {

		assertEquals("file", new File(""), chromatogram.getFile());
	}

	@Test
	public void testGetOperator_1() {

		assertEquals("operator", "eselmeister", chromatogram.getOperator());
	}

	@Test
	public void testGetDate_1() {

		assertTrue("date", chromatogram.getDate() != null);
	}

	@Test
	public void testGetDate_2() {

		assertEquals("date", date.toString(), chromatogram.getDate().toString());
	}

	@Test
	public void testGetMiscInfo_1() {

		assertEquals("miscInfo", "This is a test chromatogram.", chromatogram.getMiscInfo());
	}

	@Test
	public void testGetTotalIonSignals_1() {

		ITotalScanSignals signals = totalIonSignalExtractor.getTotalScanSignals();
		assertEquals("List<ITotalIonSignal> size", 1, signals.size());
	}

	@Test
	public void testGetTotalIonSignal_1() {

		assertEquals("totalIonSignal", 470746.9f, chromatogram.getTotalSignal(), 0);
	}
}
