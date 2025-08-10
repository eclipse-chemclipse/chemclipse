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

import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.junit.Before;
import org.junit.Test;

/**
 * Add 100 scans and get an {@link ITotalScanSignals} object.
 */
public class Chromatogram_14_Test {

	private ChromatogramMSD chromatogram;
	private VendorMassSpectrum supplierMassSpectrum;
	private IIon ion;
	private ITotalScanSignalExtractor totalIonSignalExtractor;

	@Before
	public void setUp() throws Exception {

		chromatogram = new ChromatogramMSD();
		// ------------------------------Scan 1-100
		for(int i = 1; i <= 100; i++) {
			supplierMassSpectrum = new VendorMassSpectrum();
			supplierMassSpectrum.setRetentionTime(i);
			ion = new Ion(IIon.TIC_ION, i);
			supplierMassSpectrum.addIon(ion);
			chromatogram.addScan(supplierMassSpectrum);
		}
		// ------------------------------Scan 1-100

		totalIonSignalExtractor = new TotalScanSignalExtractor(chromatogram);
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
	public void testGetTotalIonSignals_1() {

		ITotalScanSignal signal;
		ITotalScanSignals signals = totalIonSignalExtractor.getTotalScanSignals(20, 50);
		assertEquals("Size", 31, signals.size());
		assertEquals("startScan", 20, signals.getStartScan());
		assertEquals("startScan", 50, signals.getStopScan());
		signal = signals.getTotalScanSignal(20);
		assertEquals("Scan", 20, signal.getRetentionTime());
		signal = signals.getTotalScanSignal(50);
		assertEquals("Scan", 50, signal.getRetentionTime());
	}

	@Test
	public void testGetTotalIonSignals_2() {

		ITotalScanSignal signal;
		ITotalScanSignals signals = totalIonSignalExtractor.getTotalScanSignals(50, 20);
		assertEquals("Size", 31, signals.size());
		assertEquals("startScan", 20, signals.getStartScan());
		assertEquals("startScan", 50, signals.getStopScan());
		signal = signals.getTotalScanSignal(20);
		assertEquals("Scan", 20, signal.getRetentionTime());
		signal = signals.getTotalScanSignal(50);
		assertEquals("Scan", 50, signal.getRetentionTime());
	}
}
