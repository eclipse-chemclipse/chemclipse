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
 * 
 * @author Philip Wenig
 */
public class Calculations_15_Test {

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

		Calculations.smooth(values, 3);
		double[] smoothed;
		smoothed = new double[13];
		smoothed[0] = 600.0d;
		smoothed[1] = 523.0d;
		smoothed[2] = 488.3333333333333d;
		smoothed[3] = 441.3333333333333d;
		smoothed[4] = 440.3333333333333d;
		smoothed[5] = 454.66666666666663d;
		smoothed[6] = 458.0d;
		smoothed[7] = 403.3333333333333d;
		smoothed[8] = 370.66666666666663d;
		smoothed[9] = 328.66666666666663d;
		smoothed[10] = 343.3333333333333d;
		smoothed[11] = 304.0d;
		smoothed[12] = 381.0d;
		for(int i = 0; i < values.length; i++) {
			assertEquals("ValuesSmoothed", smoothed[i], values[i], 0);
		}
	}
}
