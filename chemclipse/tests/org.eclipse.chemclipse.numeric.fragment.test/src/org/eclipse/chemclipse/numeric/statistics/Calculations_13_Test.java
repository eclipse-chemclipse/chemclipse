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
public class Calculations_13_Test {

	private float[] values;

	@Before
	public void setUp() {

		values = new float[13];
		values[0] = 735.0f;
		values[1] = 504.0f;
		values[2] = 561.0f;
		values[3] = 504.0f;
		values[4] = 400.0f;
		values[5] = 420.0f;
		values[6] = 501.0f;
		values[7] = 443.0f;
		values[8] = 430.0f;
		values[9] = 337.0f;
		values[10] = 345.0f;
		values[11] = 304.0f;
		values[12] = 381.0f;
	}

	@Test
	public void testGetSum_1() {

		float result = Calculations.getSum(values);
		assertEquals("getSum", 5865.0f, result, 0);
	}
}
