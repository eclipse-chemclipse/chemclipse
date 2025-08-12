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
package org.eclipse.chemclipse.numeric.statistics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing median.
 */
public class Calculations_5_Test {

	private double[] values;

	@Before
	public void setUp() {

		values = new double[13];
		values[0] = 735.0d;
		values[1] = 504.0d;
		values[2] = 561.0d;
		values[3] = 504.0d;
		values[4] = 400.0d;
		values[5] = 420.0d;
		values[6] = 501.0d;
		values[7] = 443.0d;
		values[8] = 430.0d;
		values[9] = 337.0d;
		values[10] = 345.0d;
		values[11] = 304.0d;
		values[12] = 381.0d;
	}

	@Test
	public void testGetMedianDeviationFromMedian_1() {

		double result = Calculations.getMedianDeviationFromMedian(values);
		assertEquals("getMedianDeviationFromMedian", 74.0d, result, 0);
	}

	@Test
	public void testGetMedianDeviationFromMean_1() {

		double result = Calculations.getMedianDeviationFromMean(values);
		assertEquals("getMedianDeviationFromMean", 52.84615384615387d, result, 0);
	}
}
