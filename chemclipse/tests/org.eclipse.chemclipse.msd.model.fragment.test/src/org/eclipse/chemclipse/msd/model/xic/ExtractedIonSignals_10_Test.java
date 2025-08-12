/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
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
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.junit.Before;
import org.junit.Test;

/**
 * Test - scans with no signals.
 *
 */
public class ExtractedIonSignals_10_Test {

	private IRegularMassSpectrum supplierMassSpectrum;
	private IIon defaultIon;
	private IExtractedIonSignals extractedIonSignals;
	private IExtractedIonSignal extractedIonSignal;
	private IChromatogramMSD chromatogram;
	private IExtractedIonSignalExtractor extractedIonSignalExtractor;

	@Before
	public void setUp() {

		int scans = 120;
		int ionStart = 25;
		int ionStop = 30;
		chromatogram = new ChromatogramMSD();
		/*
		 * Add 100 scans with scans of 6 ions, 20 scans with no ions.
		 */
		for(int scan = 1; scan <= scans; scan++) {
			supplierMassSpectrum = new VendorMassSpectrum();
			supplierMassSpectrum.setRetentionTime(scan);
			supplierMassSpectrum.setRetentionIndex(scan / 60.0f);
			if(scan % 6 == 0) {
				// Scan without ions.
			} else {
				for(int ion = ionStart; ion <= ionStop; ion++) {
					defaultIon = new Ion(ion, ion * scan);
					supplierMassSpectrum.addIon(defaultIon);
				}
			}
			chromatogram.addScan(supplierMassSpectrum);
		}
		extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals();
	}

	@Test
	public void testSize_1() {

		/*
		 * It was 100 before, but 5 is correct as the extractor stops
		 * as soon as a zero scan is detected.
		 */
		assertEquals("Size", 5, extractedIonSignals.size());
	}

	@Test
	public void testSize_2() throws NoExtractedIonSignalStoredException {

		assertEquals("Size", 5, extractedIonSignals.size());
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(1);
		assertEquals("Abundance", 25.0f, extractedIonSignal.getAbundance(25), 0);
		extractedIonSignals.add(25, 250.0f, 1, true);
		assertEquals("Size", 5, extractedIonSignals.size());
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(1);
		assertEquals("Abundance", 250.0f, extractedIonSignal.getAbundance(25), 0);
	}

	@Test
	public void testSize_3() throws NoExtractedIonSignalStoredException {

		assertEquals("Size", 5, extractedIonSignals.size());
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(1);
		assertEquals("Abundance", 25.0f, extractedIonSignal.getAbundance(25), 0);
		extractedIonSignals.add(25, 250, 1, false);
		assertEquals("Size", 5, extractedIonSignals.size());
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(1);
		assertEquals("Abundance", 275.0f, extractedIonSignal.getAbundance(25), 0);
	}

	@Test
	public void testSize_4() throws NoExtractedIonSignalStoredException {

		assertEquals("Size", 5, extractedIonSignals.size());
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(1);
		assertEquals("Abundance", 25.0f, extractedIonSignal.getAbundance(25), 0);
		extractedIonSignals.add(31, 600.0f, 101, false);
		assertEquals("Size", 5, extractedIonSignals.size());
		assertThrows(NoExtractedIonSignalStoredException.class, () -> {
			extractedIonSignal = extractedIonSignals.getExtractedIonSignal(6);
		});
	}

	@Test
	public void testSize_5() {

		assertEquals(1, extractedIonSignals.getStartScan());
		assertEquals(5, extractedIonSignals.getStopScan());
	}

	@Test
	public void testSize_6() {

		assertEquals(25, extractedIonSignals.getStartIon());
		assertEquals(30, extractedIonSignals.getStopIon());
	}
}
