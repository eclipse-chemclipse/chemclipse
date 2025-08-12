/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class ExtractedIonSignals_11_Test {

	private IRegularMassSpectrum supplierMassSpectrum;
	private IIon defaultIon;
	private IExtractedIonSignals extractedIonSignals;
	private IChromatogramMSD chromatogram;
	private IExtractedIonSignalExtractor extractedIonSignalExtractor;

	@Before
	public void setUp() {

		int scans = 120;
		int ionStart = 25;
		int ionStop = 30;

		chromatogram = new ChromatogramMSD();
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

		((IScanMSD)chromatogram.getScan(1)).removeAllIons();

		extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals();
	}

	@Test
	public void testSize_1() {

		assertEquals("Size", 4, extractedIonSignals.size());
	}

	@Test
	public void testSize_2() {

		assertEquals(2, extractedIonSignals.getStartScan());
		assertEquals(5, extractedIonSignals.getStopScan());
	}

	@Test
	public void testSize_3() {

		assertEquals(25, extractedIonSignals.getStartIon());
		assertEquals(30, extractedIonSignals.getStopIon());
	}
}
