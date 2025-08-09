/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.signals;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.implementation.Chromatogram;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.junit.Test;

public class TotalScanSignalExtractor_1_Test {

	private IChromatogram chromatogram = new Chromatogram();

	@Test
	public void test_1() {

		boolean condenseCycleNumberScans = false;
		addScan(chromatogram, 500, 2898.2f, 1);
		addScan(chromatogram, 1000, 7837.2f, 1);
		addScan(chromatogram, 1500, 128900.48f, 1);
		addScan(chromatogram, 2000, 289830.65f, 1);
		addScan(chromatogram, 2500, 3983.0f, 1);
		assertEquals(false, chromatogram.containsScanCycles());

		try {
			ITotalScanSignalExtractor totalIonSignalExtractor = new TotalScanSignalExtractor(chromatogram);
			ITotalScanSignals signals = totalIonSignalExtractor.getTotalScanSignals(1, 5, true, condenseCycleNumberScans);
			assertEquals(2898.2f, signals.getMinSignal(), 0);
			assertEquals(289830.65f, signals.getMaxSignal(), 0);
			assertEquals(5, signals.size());
		} catch(ChromatogramIsNullException e) {
			assertTrue(false);
		}
	}

	@Test
	public void test_2() {

		boolean condenseCycleNumberScans = true;
		addScan(chromatogram, 500, 2898.2f, 1);
		addScan(chromatogram, 1000, 7837.2f, 1);
		addScan(chromatogram, 1500, 128900.48f, 1);
		addScan(chromatogram, 2000, 289830.65f, 1);
		addScan(chromatogram, 2500, 3983.0f, 1);
		assertEquals(false, chromatogram.containsScanCycles());

		try {
			ITotalScanSignalExtractor totalIonSignalExtractor = new TotalScanSignalExtractor(chromatogram);
			ITotalScanSignals signals = totalIonSignalExtractor.getTotalScanSignals(1, 5, true, condenseCycleNumberScans);
			assertEquals(433449.53f, signals.getMinSignal(), 0);
			assertEquals(433449.53f, signals.getMaxSignal(), 0);
			assertEquals(1, signals.size());
		} catch(ChromatogramIsNullException e) {
			assertTrue(false);
		}
	}

	@Test
	public void test_3() {

		boolean condenseCycleNumberScans = false;
		addScan(chromatogram, 500, 2898.2f, 1);
		addScan(chromatogram, 1000, 7837.2f, 1);
		addScan(chromatogram, 1500, 128900.48f, 2);
		addScan(chromatogram, 2000, 289830.65f, 2);
		addScan(chromatogram, 2500, 3983.0f, 3);
		assertEquals(true, chromatogram.containsScanCycles());

		try {
			ITotalScanSignalExtractor totalIonSignalExtractor = new TotalScanSignalExtractor(chromatogram);
			ITotalScanSignals signals = totalIonSignalExtractor.getTotalScanSignals(1, 5, true, condenseCycleNumberScans);
			assertEquals(2898.2f, signals.getMinSignal(), 0);
			assertEquals(289830.65f, signals.getMaxSignal(), 0);
			assertEquals(5, signals.size());
		} catch(ChromatogramIsNullException e) {
			assertTrue(false);
		}
	}

	@Test
	public void test_4() {

		boolean condenseCycleNumberScans = true;
		addScan(chromatogram, 500, 2898.2f, 1);
		addScan(chromatogram, 1000, 7837.2f, 1);
		addScan(chromatogram, 1500, 128900.48f, 2);
		addScan(chromatogram, 2000, 289830.65f, 2);
		addScan(chromatogram, 2500, 3983.0f, 3);
		assertEquals(true, chromatogram.containsScanCycles());

		ITotalScanSignalExtractor totalIonSignalExtractor = new TotalScanSignalExtractor(chromatogram);
		ITotalScanSignals signals = totalIonSignalExtractor.getTotalScanSignals(1, 5, true, condenseCycleNumberScans);
		assertEquals(3983.0f, signals.getMinSignal(), 0);
		assertEquals(418731.12f, signals.getMaxSignal(), 0);
		assertEquals(3, signals.size());
	}

	private void addScan(IChromatogram chromatogram, int retentionTime, float intensity, int cycleNumber) {

		IScan scan = new Scan(intensity);
		scan.setRetentionTime(retentionTime);
		scan.setCycleNumber(cycleNumber);
		chromatogram.addScan(scan);
	}
}
