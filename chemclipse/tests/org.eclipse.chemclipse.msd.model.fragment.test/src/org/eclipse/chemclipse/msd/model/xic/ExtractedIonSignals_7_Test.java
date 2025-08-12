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
import static org.junit.Assert.assertNotSame;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.junit.Before;
import org.junit.Test;

public class ExtractedIonSignals_7_Test {

	private IExtractedIonSignals extractedIonSignals;
	private IExtractedIonSignal extractedIonSignal;
	private IChromatogramMSD chromatogram;

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
	public void testMakeDeepCopyWithoutSignals_1() {

		IExtractedIonSignals signals = extractedIonSignals.makeDeepCopyWithoutSignals();
		assertEquals("Size", 100, signals.size());
	}

	@Test
	public void testMakeDeepCopyWithoutSignals_2() {

		IExtractedIonSignals signals = extractedIonSignals.makeDeepCopyWithoutSignals();
		assertNotSame(signals, extractedIonSignals);
	}

	@Test
	public void testMakeDeepCopyWithoutSignals_3() throws NoExtractedIonSignalStoredException {

		float sum = 0.0f;
		IExtractedIonSignal signal;
		for(int scan = 1; scan <= 100; scan++) {
			signal = extractedIonSignals.getExtractedIonSignal(scan);
			sum += signal.getTotalSignal();
		}
		assertEquals("TotalSignals", 833250.0f, sum, 0);
		// Make a deep copy.
		IExtractedIonSignals signals = extractedIonSignals.makeDeepCopyWithoutSignals();
		sum = 0.0f;
		for(int scan = 1; scan <= 100; scan++) {
			signal = signals.getExtractedIonSignal(scan);
			sum += signal.getTotalSignal();
		}
		assertEquals("TotalSignals", 0.0f, sum, 0);
	}
}
