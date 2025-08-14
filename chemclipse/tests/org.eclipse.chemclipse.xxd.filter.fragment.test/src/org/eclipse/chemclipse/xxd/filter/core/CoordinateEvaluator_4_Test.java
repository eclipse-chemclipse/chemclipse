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
package org.eclipse.chemclipse.xxd.filter.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.xxd.filter.model.CoordinateOption;
import org.eclipse.chemclipse.xxd.filter.model.RangeOption;
import org.junit.Test;

public class CoordinateEvaluator_4_Test extends CoordinateEvaluatorTestCase {

	private double coordinateValue = 500;
	private CoordinateOption coordinateOption = CoordinateOption.RETENTION_INDEX;

	@Test
	public void test1a() {

		IScan scan = createScan(0, 499);
		assertFalse(CoordinateEvaluator.isMatchScan(scan, coordinateOption, RangeOption.EQUALS, coordinateValue));
	}

	@Test
	public void test1b() {

		IScan scan = createScan(0, 500);
		assertTrue(CoordinateEvaluator.isMatchScan(scan, coordinateOption, RangeOption.EQUALS, coordinateValue));
	}

	@Test
	public void test1c() {

		IScan scan = createScan(0, 501);
		assertFalse(CoordinateEvaluator.isMatchScan(scan, coordinateOption, RangeOption.EQUALS, coordinateValue));
	}

	@Test
	public void test2() {

		IScan scan = createScan(0, 500);
		assertFalse(CoordinateEvaluator.isMatchScan(scan, coordinateOption, RangeOption.LOWER, coordinateValue));
	}

	@Test
	public void test3() {

		IScan scan = createScan(0, 500);
		assertTrue(CoordinateEvaluator.isMatchScan(scan, coordinateOption, RangeOption.LOWER_EQUALS, coordinateValue));
	}

	@Test
	public void test4() {

		IScan scan = createScan(0, 500);
		assertTrue(CoordinateEvaluator.isMatchScan(scan, coordinateOption, RangeOption.HIGHER_EQUALS, coordinateValue));
	}

	@Test
	public void test5() {

		IScan scan = createScan(0, 500);
		assertFalse(CoordinateEvaluator.isMatchScan(scan, coordinateOption, RangeOption.HIGHER, coordinateValue));
	}
}