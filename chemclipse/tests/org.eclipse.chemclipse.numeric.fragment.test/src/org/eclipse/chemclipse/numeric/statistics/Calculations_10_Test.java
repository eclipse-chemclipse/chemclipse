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
public class Calculations_10_Test {

	private double[] values;

	@Before
	public void setUp() {

		values = new double[13];
		values[0] = 0.0d;
		values[1] = 0.0d;
		values[2] = 0.0d;
		values[3] = 0.0d;
		values[4] = 0.0d;
		values[5] = 0.0d;
		values[6] = 0.0d;
		values[7] = 0.0d;
		values[8] = 0.0d;
		values[9] = 0.0d;
		values[10] = 0.0d;
		values[11] = 0.0d;
		values[12] = 0.0d;
	}

	@Test
	public void testNormalize_1() {

		Calculations.normalize(values);
		assertEquals(0.0d, values[0], 0);
		assertEquals(0.0d, values[12], 0);
		assertEquals(0.0d, values[6], 0);
		assertEquals(0.0d, values[10], 0);
	}
}
