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

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIon;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIons;
import org.eclipse.chemclipse.msd.model.xic.ITotalIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.TotalIonSignalExtractor;
import org.junit.Before;
import org.junit.Test;

/**
 * Add 100 scans and get an {@link ITotalScanSignals} object.
 */
public class Chromatogram_16_Test {

	private ChromatogramMSD chromatogram;
	private VendorMassSpectrum supplierMassSpectrum;
	private IIon ion;
	private ITotalScanSignals signals;
	private IMarkedIons excludedIons;
	private ITotalIonSignalExtractor totalIonSignalExtractor;

	@Before
	public void setUp() throws Exception {

		chromatogram = new ChromatogramMSD();
		// ------------------------------Scan 1-100
		for(int i = 1; i <= 100; i++) {
			supplierMassSpectrum = new VendorMassSpectrum();
			supplierMassSpectrum.setRetentionTime(i);
			for(int j = 1; j <= 50; j++) {
				ion = new Ion(j, j);
				supplierMassSpectrum.addIon(ion);
			}
			chromatogram.addScan(supplierMassSpectrum);
		}
		// ------------------------------Scan 1-100
		excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);

		totalIonSignalExtractor = new TotalIonSignalExtractor(chromatogram);
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
	public void testGetRange_1() {

		signals = totalIonSignalExtractor.getTotalScanSignals();
		assertEquals("Size", 100, signals.size());
		assertEquals("startScan", 1, signals.getStartScan());
		assertEquals("startScan", 100, signals.getStopScan());
	}

	@Test
	public void testGetTotalIonSignals_1() {

		excludedIons.add(1, 50);
		signals = totalIonSignalExtractor.getTotalIonSignals(excludedIons);
		assertEquals("TotalSignal", 0.0f, signals.getTotalScanSignal(1).getTotalSignal(), 0);
		assertEquals("TotalSignal", 0.0f, signals.getTotalScanSignal(20).getTotalSignal(), 0);
		assertEquals("TotalSignal", 0.0f, signals.getTotalScanSignal(100).getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignals_2() {

		excludedIons.add(26, 50);
		signals = totalIonSignalExtractor.getTotalIonSignals(excludedIons);
		assertEquals("TotalSignal", 325.0f, signals.getTotalScanSignal(1).getTotalSignal(), 0);
		assertEquals("TotalSignal", 325.0f, signals.getTotalScanSignal(20).getTotalSignal(), 0);
		assertEquals("TotalSignal", 325.0f, signals.getTotalScanSignal(100).getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignals_3() {

		excludedIons.add(new MarkedIon(26));
		signals = totalIonSignalExtractor.getTotalIonSignals(excludedIons);
		assertEquals("TotalSignal", 1249.0f, signals.getTotalScanSignal(1).getTotalSignal(), 0);
		assertEquals("TotalSignal", 1249.0f, signals.getTotalScanSignal(20).getTotalSignal(), 0);
		assertEquals("TotalSignal", 1249.0f, signals.getTotalScanSignal(100).getTotalSignal(), 0);
	}
}
