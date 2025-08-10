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

import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.junit.Before;
import org.junit.Test;

/**
 * Add 100 scans and remove number 50.
 */
public class Chromatogram_9_Test {

	private ChromatogramMSD chromatogram;
	private VendorMassSpectrum supplierMassSpectrum;
	private IIon ion;
	private ITotalScanSignalExtractor totalIonSignalExtractor;
	private IExtractedIonSignalExtractor extractedIonSignalExtractor;

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
		chromatogram.removeScans(50, 50);

		totalIonSignalExtractor = new TotalScanSignalExtractor(chromatogram);
		extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
	}

	@Test
	public void testGetNumberOfScans_1() {

		assertEquals("numberOfScans", 99, chromatogram.getNumberOfScans());
	}

	@Test
	public void testGetNumberOfScanIons_1() {

		// 100 - 20
		assertEquals("numberOfScanIons", 99, chromatogram.getNumberOfScanIons());
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
	public void testGetMinSignal_1() {

		// Cast to int because float is not as precise as for example
		// java.lang.Math.
		assertEquals("minSignal", 1, (int)chromatogram.getMinSignal(), 0);
	}

	@Test
	public void testGetMaxSignal_1() {

		// Cast to int because float is not as precise as for example
		// java.lang.Math.
		assertEquals("maxSignal", 100, (int)chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignals_1() {

		ITotalScanSignals signals = totalIonSignalExtractor.getTotalScanSignals();
		assertEquals("ITotalIonSignals size", 99, signals.size());
	}

	@Test
	public void testGetTotalIonSignal_1() {

		// Cast to int because float is not as precise as for example
		// java.lang.Math.
		assertEquals("totalIonSignal", 5000, (int)chromatogram.getTotalSignal(), 0);
	}

	@Test
	public void testGetExtractedIonSignals_1() {

		IExtractedIonSignals signals = extractedIonSignalExtractor.getExtractedIonSignals();
		assertEquals("IExtractedIonSignals size", 99, signals.size());
	}

	@Test
	public void testGetExtractedIonSignals_2() {

		IExtractedIonSignals signals = extractedIonSignalExtractor.getExtractedIonSignals(1.0f, 100.0f);
		assertEquals("IExtractedIonSignals size", 99, signals.size());
	}

	@Test
	public void testGetScan_1() {

		IScanMSD massSpectrum;
		massSpectrum = chromatogram.getSupplierScan(49);
		assertEquals("TotalSignal", 49.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetScan_2() {

		IScanMSD massSpectrum;
		massSpectrum = chromatogram.getSupplierScan(50);
		assertEquals("TotalSignal", 51.0f, massSpectrum.getTotalSignal(), 0);
	}
}
