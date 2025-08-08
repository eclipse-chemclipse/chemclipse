/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.scan.calculator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DensityCalculator_5_Test {

	private static final int SCAN_INTERVAL = 978;
	private DensityCalculator densityCalculator = new DensityCalculator();

	@Test
	public void test01() {

		assertEquals(0, densityCalculator.adjustScanInterval(SCAN_INTERVAL, 0));
	}

	@Test
	public void test02() {

		assertEquals(1000, densityCalculator.adjustScanInterval(SCAN_INTERVAL, 250));
	}

	@Test
	public void test03() {

		assertEquals(1000, densityCalculator.adjustScanInterval(SCAN_INTERVAL, 500));
	}

	@Test
	public void test04() {

		assertEquals(1000, densityCalculator.adjustScanInterval(SCAN_INTERVAL, 1000));
	}

	@Test
	public void test05() {

		assertEquals(1500, densityCalculator.adjustScanInterval(SCAN_INTERVAL, 1500));
	}
}