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

public class Calculations_2_Test {

	private double[] values;

	@Before
	public void setUp() {

		values = new double[13];
		values[0] = 735;
		values[1] = 504;
		values[2] = 561;
		values[3] = 504;
		values[4] = 400;
		values[5] = 420;
		values[6] = 501;
		values[7] = 443;
		values[8] = 430;
		values[9] = 337;
		values[10] = 345;
		values[11] = 304;
		values[12] = 381;
	}

	@Test
	public void testGetMean_1() {

		double result = Calculations.getMean(values);
		assertEquals("getMean", 451.15384615384613d, result, 0);
	}

	@Test
	public void testGetVariance_1() {

		double result = Calculations.getVariance(values);
		assertEquals("getVariance", 12973.474358974356d, result, 0);
	}

	@Test
	public void testGetStandardDeviation_1() {

		double result = Calculations.getStandardDeviation(values);
		assertEquals("getStandardDeviation", 113.90116048124513d, result, 0);
	}
}
