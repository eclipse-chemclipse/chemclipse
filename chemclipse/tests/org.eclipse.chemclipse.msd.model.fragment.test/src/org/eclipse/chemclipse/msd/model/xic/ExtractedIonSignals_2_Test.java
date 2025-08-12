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
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.junit.Test;

public class ExtractedIonSignals_2_Test {

	@Test
	public void testConstructor_1() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(10);
		for(int i = 1; i <= 10; i++) {
			IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(i * 10, i * 20);
			extractedIonSignals.add(extractedIonSignal);
		}
		assertEquals("StartScan", 1, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 10, extractedIonSignals.getStopScan());
		assertEquals("StartIon", 10, extractedIonSignals.getStartIon());
		assertEquals("StopIon", 200, extractedIonSignals.getStopIon());
	}

	@Test
	public void testConstructor_2() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(-1, -1);
		assertEquals("StartScan", 0, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 0, extractedIonSignals.getStopScan());
		int startScan = extractedIonSignals.getStartScan();
		assertThrows(NoExtractedIonSignalStoredException.class, () -> {
			extractedIonSignals.getExtractedIonSignal(startScan);
		});
		int stopScan = extractedIonSignals.getStopScan();
		assertThrows(NoExtractedIonSignalStoredException.class, () -> {
			extractedIonSignals.getExtractedIonSignal(stopScan);
		});
		assertThrows(NoExtractedIonSignalStoredException.class, () -> {
			extractedIonSignals.getExtractedIonSignal(0);
		});
		assertThrows(NoExtractedIonSignalStoredException.class, () -> {
			extractedIonSignals.getExtractedIonSignal(2);
		});
	}

	@Test
	public void testGetExtractedIonSignal_1() throws NoExtractedIonSignalStoredException {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(10);
		for(int i = 1; i <= 10; i++) {
			IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(i * 10, i * 20);
			extractedIonSignals.add(extractedIonSignal);
		}
		int scan = 5;
		IExtractedIonSignal extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
		assertEquals("StartIon", 50, extractedIonSignal.getStartIon());
		assertEquals("StopIon", 100, extractedIonSignal.getStopIon());
	}
}
