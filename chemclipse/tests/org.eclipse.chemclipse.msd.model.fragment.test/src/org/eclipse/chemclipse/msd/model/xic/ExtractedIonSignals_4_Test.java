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
package org.eclipse.chemclipse.msd.model.xic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.junit.Before;
import org.junit.Test;

public class ExtractedIonSignals_4_Test {

	private IExtractedIonSignals extractedIonSignals;
	private IExtractedIonSignal extractedIonSignal;
	private IChromatogramMSD chromatogram;
	private ITotalScanSignals totalIonSignals;
	private ITotalScanSignal totalIonSignal;

	@Before
	public void setUp() {

		int scans = 100;
		int ionStart = 25;
		int ionStop = 30;
		chromatogram = new ChromatogramMSD();
		extractedIonSignals = new ExtractedIonSignals(scans, chromatogram);
		/*
		 * Add 100 scans with scans of 6 ions.
		 */
		for(int scan = 1; scan <= scans; scan++) {
			extractedIonSignal = new ExtractedIonSignal(ionStart, ionStop);
			extractedIonSignal.setRetentionTime(scan);
			extractedIonSignal.setRetentionIndex(scan / 60.0f);
			for(int ion = ionStart; ion <= ionStop; ion++) {
				extractedIonSignal.setAbundance(ion, ion * scan);
			}
			extractedIonSignals.add(extractedIonSignal);
		}
	}

	@Test
	public void testGetTotalIonSignals_1() {

		totalIonSignals = extractedIonSignals.getTotalIonSignals((int)IIon.TIC_ION);
		totalIonSignal = totalIonSignals.getTotalScanSignal(1);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 165.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(100);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 16500.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(26);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 4290.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(87);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 14355.0f, totalIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignals_2() {

		totalIonSignals = extractedIonSignals.getTotalIonSignals(25);
		totalIonSignal = totalIonSignals.getTotalScanSignal(1);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 25.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(100);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 2500.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(26);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 650.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(87);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 2175.0f, totalIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignals_3() {

		totalIonSignals = extractedIonSignals.getTotalIonSignals(30);
		totalIonSignal = totalIonSignals.getTotalScanSignal(1);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 30.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(100);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 3000.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(26);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 780.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(87);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 2610.0f, totalIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignals_4() {

		totalIonSignals = extractedIonSignals.getTotalIonSignals(24);
		totalIonSignal = totalIonSignals.getTotalScanSignal(1);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 0.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(100);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 0.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(26);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 0.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(87);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 0.0f, totalIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignals_5() {

		totalIonSignals = extractedIonSignals.getTotalIonSignals(31);
		totalIonSignal = totalIonSignals.getTotalScanSignal(1);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 0.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(100);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 0.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(26);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 0.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(87);
		assertNotNull(totalIonSignal);
		assertEquals("TotalSignal", 0.0f, totalIonSignal.getTotalSignal(), 0);
	}
}
