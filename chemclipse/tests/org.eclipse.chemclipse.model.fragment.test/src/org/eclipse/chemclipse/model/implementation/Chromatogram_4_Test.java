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
package org.eclipse.chemclipse.model.implementation;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.junit.Test;

public class Chromatogram_4_Test {

	private IChromatogram chromatogram = new Chromatogram();

	@Test
	public void test_1() {

		addScan(chromatogram, 500, 2898.2f, 1);
		addScan(chromatogram, 1000, 7837.2f, 1);
		addScan(chromatogram, 1500, 128900.48f, 1);
		addScan(chromatogram, 2000, 289830.65f, 1);
		addScan(chromatogram, 2500, 3983.0f, 1);
		assertEquals(false, chromatogram.containsScanCycles());

		assertEquals(0, chromatogram.getScanCycleScans(0).size());
		assertEquals(5, chromatogram.getScanCycleScans(1).size());
	}

	@Test
	public void test_2() {

		addScan(chromatogram, 500, 2898.2f, 1);
		addScan(chromatogram, 1000, 7837.2f, 1);
		addScan(chromatogram, 1500, 128900.48f, 1);
		addScan(chromatogram, 2000, 289830.65f, 1);
		addScan(chromatogram, 2500, 3983.0f, 1);
		assertEquals(false, chromatogram.containsScanCycles());

		assertEquals(0.0f, getIntensity(chromatogram.getScanCycleScans(0)), 0);
		assertEquals(433449.53f, getIntensity(chromatogram.getScanCycleScans(1)), 0);
		assertEquals(0.0f, getIntensity(chromatogram.getScanCycleScans(2)), 0);
		assertEquals(0.0f, getIntensity(chromatogram.getScanCycleScans(3)), 0);
	}

	@Test
	public void test_3() {

		addScan(chromatogram, 500, 2898.2f, 1);
		addScan(chromatogram, 1000, 7837.2f, 1);
		addScan(chromatogram, 1500, 128900.48f, 2);
		addScan(chromatogram, 2000, 289830.65f, 2);
		addScan(chromatogram, 2500, 3983.0f, 3);
		assertEquals(true, chromatogram.containsScanCycles());

		assertEquals(0, chromatogram.getScanCycleScans(0).size());
		assertEquals(2, chromatogram.getScanCycleScans(1).size());
		assertEquals(2, chromatogram.getScanCycleScans(2).size());
		assertEquals(1, chromatogram.getScanCycleScans(3).size());
	}

	@Test
	public void test_4() {

		addScan(chromatogram, 500, 2898.2f, 1);
		addScan(chromatogram, 1000, 7837.2f, 1);
		addScan(chromatogram, 1500, 128900.48f, 2);
		addScan(chromatogram, 2000, 289830.65f, 2);
		addScan(chromatogram, 2500, 3983.0f, 3);
		assertEquals(true, chromatogram.containsScanCycles());

		assertEquals(0.0f, getIntensity(chromatogram.getScanCycleScans(0)), 0);
		assertEquals(10735.4f, getIntensity(chromatogram.getScanCycleScans(1)), 0);
		assertEquals(418731.13f, getIntensity(chromatogram.getScanCycleScans(2)), 0);
		assertEquals(3983.0f, getIntensity(chromatogram.getScanCycleScans(3)), 0);
	}

	private float getIntensity(List<IScan> scans) {

		float intensity = 0;
		for(IScan scan : scans) {
			intensity += scan.getTotalSignal();
		}
		return intensity;
	}

	private void addScan(IChromatogram chromatogram, int retentionTime, float intensity, int cycleNumber) {

		IScan scan = new Scan(intensity);
		scan.setRetentionTime(retentionTime);
		scan.setCycleNumber(cycleNumber);
		chromatogram.addScan(scan);
	}
}
