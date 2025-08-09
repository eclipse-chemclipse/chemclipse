/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.support;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.implementation.Chromatogram;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.junit.Test;

public class ChromatogramSupport_1_Test {

	private IChromatogram chromatogram = new Chromatogram();

	@Test
	public void test1() {

		ChromatogramSupport.calculateScanIntervalAndDelay(chromatogram);

		assertEquals(0, chromatogram.getScanDelay());
		assertEquals(100, chromatogram.getScanInterval());
	}

	@Test
	public void test2() {

		chromatogram.addScan(getScan(0));

		ChromatogramSupport.calculateScanIntervalAndDelay(chromatogram);
		assertEquals(0, chromatogram.getScanDelay());
		assertEquals(100, chromatogram.getScanInterval());
	}

	@Test
	public void test3() {

		chromatogram.addScan(getScan(0));
		chromatogram.addScan(getScan(500));

		ChromatogramSupport.calculateScanIntervalAndDelay(chromatogram);
		assertEquals(0, chromatogram.getScanDelay());
		assertEquals(500, chromatogram.getScanInterval());
	}

	@Test
	public void test4() {

		chromatogram.addScan(getScan(0));
		chromatogram.addScan(getScan(500));
		chromatogram.addScan(getScan(1000));

		ChromatogramSupport.calculateScanIntervalAndDelay(chromatogram);
		assertEquals(0, chromatogram.getScanDelay());
		assertEquals(500, chromatogram.getScanInterval());
	}

	@Test
	public void test5() {

		chromatogram.addScan(getScan(500));
		chromatogram.addScan(getScan(1000));
		chromatogram.addScan(getScan(1500));

		ChromatogramSupport.calculateScanIntervalAndDelay(chromatogram);
		assertEquals(500, chromatogram.getScanDelay());
		assertEquals(500, chromatogram.getScanInterval());
	}

	@Test
	public void test6() {

		chromatogram.addScan(getScan(750));
		chromatogram.addScan(getScan(1000));
		chromatogram.addScan(getScan(1250));
		chromatogram.addScan(getScan(1500));

		ChromatogramSupport.calculateScanIntervalAndDelay(chromatogram);
		assertEquals(750, chromatogram.getScanDelay());
		assertEquals(250, chromatogram.getScanInterval());
	}

	private IScan getScan(int retentionTime) {

		/*
		 * Intensity is not important here.
		 */
		IScan scan = new Scan(2500);
		scan.setRetentionTime(retentionTime);

		return scan;
	}
}