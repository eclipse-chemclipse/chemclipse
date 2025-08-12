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
public class Calculations_14_Test {

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
	public void testGetWindowReducedLength_1() {

		int result = Calculations.getWindowReducedLength(values, 3);
		assertEquals("getWindowReducedLength", 11, result);
	}

	@Test
	public void testGetWindowReducedLength_2() {

		int result = Calculations.getWindowReducedLength(null, 3);
		assertEquals("getWindowReducedLength", 0, result);
	}

	@Test
	public void testGetWindowReducedLength_3() {

		int result = Calculations.getWindowReducedLength(values, 1);
		assertEquals("getWindowReducedLength", 13, result);
	}

	@Test
	public void testGetWindowReducedLength_4() {

		int result = Calculations.getWindowReducedLength(values, 0);
		assertEquals("getWindowReducedLength", 13, result);
	}

	@Test
	public void testGetWindowReducedLength_5() {

		int result = Calculations.getWindowReducedLength(values, 13);
		assertEquals("getWindowReducedLength", 1, result);
	}

	@Test
	public void testGetWindowReducedLength_6() {

		int result = Calculations.getWindowReducedLength(values, 12);
		assertEquals("getWindowReducedLength", 2, result);
	}

	@Test
	public void testGetWindowReducedLength_7() {

		int result = Calculations.getWindowReducedLength(values, 14);
		assertEquals("getWindowReducedLength", 13, result);
	}
}
