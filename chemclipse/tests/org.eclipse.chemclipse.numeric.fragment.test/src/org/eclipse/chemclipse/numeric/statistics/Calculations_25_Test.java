/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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

public class Calculations_25_Test {

	private int[] values;

	@Before
	public void setUp() {

		values = new int[]{-1, 1};
	}

	@Test
	public void testGetMean_1() {

		int min = Calculations.getMin(values);
		assertEquals(-1, min);
	}

	@Test
	public void testGetMean_2() {

		int max = Calculations.getMax(values);
		assertEquals(1, max);
	}
}
