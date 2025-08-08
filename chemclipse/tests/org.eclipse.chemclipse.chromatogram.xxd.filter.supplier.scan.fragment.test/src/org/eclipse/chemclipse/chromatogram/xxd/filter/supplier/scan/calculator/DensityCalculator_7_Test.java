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

public class DensityCalculator_7_Test {

	private DensityCalculator densityCalculator = new DensityCalculator();
	private int[] retentionTimes = new int[]{0, 1070, 2010, 2950, 3890, 4860};

	@Test
	public void test00() {

		int scanInterval = densityCalculator.calculateScanInterval(retentionTimes);
		assertEquals(940, scanInterval);
		assertEquals(1000, densityCalculator.adjustScanInterval(scanInterval, 1000));
	}

	@Test
	public void test01() {

		DensityOperation densityOperation = densityCalculator.calculateScanModification(2, 1000);
		assertEquals(DensityOperation.INCREASE_ADD, densityOperation);
		assertEquals(1, densityOperation.getModifications());
	}

	@Test
	public void test02() {

		int[] retentionTimesAdjusted = densityCalculator.adjustRetentionTimes(retentionTimes, 2);
		assertEquals(6, retentionTimesAdjusted.length);
		assertEquals(0, retentionTimesAdjusted[0]);
		assertEquals(1000, retentionTimesAdjusted[1]);
		assertEquals(2000, retentionTimesAdjusted[2]);
		assertEquals(3000, retentionTimesAdjusted[3]);
		assertEquals(4000, retentionTimesAdjusted[4]);
		assertEquals(5000, retentionTimesAdjusted[5]);
	}
}