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

import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.junit.Before;
import org.junit.Test;

/**
 * Add 100 scans and get an {@link ITotalScanSignals} object.
 */
public class Chromatogram_18_Test {

	private IChromatogramMSD chromatogram;
	private IRegularMassSpectrum supplierMassSpectrum;
	private IIon ion;
	private IExtractedIonSignals extractedIonSignals;
	private IExtractedIonSignal extractedIonSignal;
	private ChromatogramSelectionMSD chromatogramSelection;
	private IExtractedIonSignalExtractor extractedIonSignalExtractor;

	@Before
	public void setUp() throws Exception {

		chromatogram = new ChromatogramMSD();
		// ------------------------------Scan 1-100
		for(int i = 1; i <= 100; i++) {
			supplierMassSpectrum = new VendorMassSpectrum();
			supplierMassSpectrum.setRetentionTime(i);
			for(int j = 1; j <= 50; j++) {
				ion = new Ion(j, i * j);
				supplierMassSpectrum.addIon(ion);
			}
			chromatogram.addScan(supplierMassSpectrum);
		}
		// ------------------------------Scan 1-100

		extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
	}

	@Test
	public void testGetNumberOfScans_1() {

		assertEquals("numberOfScans", 100, chromatogram.getNumberOfScans());
	}

	@Test
	public void testGetStartRetentionTime_1() {

		assertEquals("startRetentionTime", 1, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testGetStopRetentionTime_1() {

		assertEquals("stopRetentionTime", 100, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testGetExtractedIonSignals_1() throws NoExtractedIonSignalStoredException {

		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(20, 50);
		assertEquals("StartScan", 20, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 50, extractedIonSignals.getStopScan());
		int scan;
		scan = extractedIonSignals.getStartScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
		assertEquals("TotalSignal", 25500.0f, extractedIonSignal.getTotalSignal(), 0);
		scan = extractedIonSignals.getStopScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
		assertEquals("TotalSignal", 63750.0f, extractedIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testGetExtractedIonSignals_2() throws NoExtractedIonSignalStoredException {

		/*
		 * Retention time and scan # are identical in this special case.
		 */
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
		chromatogramSelection.setStartRetentionTime(20);
		chromatogramSelection.setStopRetentionTime(50);
		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(chromatogramSelection);
		assertEquals("StartScan", 20, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 50, extractedIonSignals.getStopScan());
		int scan;
		scan = extractedIonSignals.getStartScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
		assertEquals("TotalSignal", 25500.0f, extractedIonSignal.getTotalSignal(), 0);
		scan = extractedIonSignals.getStopScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
		assertEquals("TotalSignal", 63750.0f, extractedIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testGetExtractedIonSignals_3() throws NoExtractedIonSignalStoredException {

		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(50, 20);
		assertEquals("StartScan", 20, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 50, extractedIonSignals.getStopScan());
		int scan;
		scan = extractedIonSignals.getStartScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
		assertEquals("TotalSignal", 25500.0f, extractedIonSignal.getTotalSignal(), 0);
		scan = extractedIonSignals.getStopScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
		assertEquals("TotalSignal", 63750.0f, extractedIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testGetExtractedIonSignals_4() throws NoExtractedIonSignalStoredException {

		/*
		 * Retention time and scan # are identical in this special case.
		 */
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
		chromatogramSelection.setStartRetentionTime(50);
		chromatogramSelection.setStopRetentionTime(20);
		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(chromatogramSelection);
		assertEquals("StartScan", 50, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 100, extractedIonSignals.getStopScan());
		int scan;
		scan = extractedIonSignals.getStartScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
		assertEquals("TotalSignal", 63750.0f, extractedIonSignal.getTotalSignal(), 0);
		scan = extractedIonSignals.getStopScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
		assertEquals("TotalSignal", 127500.0f, extractedIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testGetExtractedIonSignals_5() throws NoExtractedIonSignalStoredException {

		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(0, 180);
		assertEquals("StartScan", 1, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 100, extractedIonSignals.getStopScan());
		int startScan = extractedIonSignals.getStartScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(startScan);
		int stopScan = extractedIonSignals.getStopScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(stopScan);
	}

	@Test
	public void testGetExtractedIonSignals_6() throws NoExtractedIonSignalStoredException {

		/*
		 * Retention time and scan # are identical in this special case.
		 */
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
		chromatogramSelection.setStartRetentionTime(0);
		chromatogramSelection.setStopRetentionTime(180);
		assertEquals("StartRetentionTime", 1, chromatogramSelection.getStartRetentionTime());
		assertEquals("StopRetentionTime", 100, chromatogramSelection.getStopRetentionTime());
		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(chromatogramSelection);
		assertEquals("StartScan", 1, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 100, extractedIonSignals.getStopScan());
		int scan;
		scan = extractedIonSignals.getStartScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
		assertEquals("TotalSignal", 1275.0f, extractedIonSignal.getTotalSignal(), 0);
		scan = extractedIonSignals.getStopScan();
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
		assertEquals("TotalSignal", 127500.0f, extractedIonSignal.getTotalSignal(), 0);
	}
}
