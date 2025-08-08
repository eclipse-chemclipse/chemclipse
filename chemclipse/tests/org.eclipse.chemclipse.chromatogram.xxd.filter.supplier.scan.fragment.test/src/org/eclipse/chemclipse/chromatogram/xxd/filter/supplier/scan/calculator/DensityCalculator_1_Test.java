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

import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.scan.core.DensityOperation;
import org.junit.Test;

public class DensityCalculator_1_Test {

	private static final int SCAN_INTERVAL = 1000;
	private DensityCalculator densityCalculator = new DensityCalculator();

	@Test
	public void test01() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(1, SCAN_INTERVAL);
		assertEquals(DensityOperation.NONE, densityOperation);
		assertEquals(0, densityOperation.getModifications());
	}

	@Test
	public void test02() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(2, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(1, densityOperation.getModifications());
	}

	@Test
	public void test03() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(3, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(2, densityOperation.getModifications());
	}

	@Test
	public void test04() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(4, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(3, densityOperation.getModifications());
	}

	@Test
	public void test05() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(5, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(4, densityOperation.getModifications());
	}

	@Test
	public void test06() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(6, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(5, densityOperation.getModifications());
	}

	@Test
	public void test07() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(7, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(6, densityOperation.getModifications());
	}

	@Test
	public void test08() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(8, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(7, densityOperation.getModifications());
	}

	@Test
	public void test09() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(9, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(8, densityOperation.getModifications());
	}

	@Test
	public void test10() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(10, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(9, densityOperation.getModifications());
	}

	@Test
	public void test11() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(11, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(10, densityOperation.getModifications());
	}

	@Test
	public void test12() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(998, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(997, densityOperation.getModifications());
	}

	@Test
	public void test13() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(999, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(998, densityOperation.getModifications());
	}

	@Test
	public void test14() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(1000, SCAN_INTERVAL);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(999, densityOperation.getModifications());
	}

	@Test
	public void test15() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(1001, SCAN_INTERVAL);
		assertEquals(DensityOperation.NONE, densityOperation);
		assertEquals(0, densityOperation.getModifications());
	}
}