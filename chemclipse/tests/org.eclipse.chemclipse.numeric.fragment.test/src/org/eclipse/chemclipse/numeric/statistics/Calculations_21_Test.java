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
public class Calculations_21_Test {

	private float[] values;

	@Before
	public void setUp() {

		values = new float[0];
	}

	@Test
	public void testGetMean_1() {

		float min = Calculations.getMin(values);
		assertEquals("min", 0.0f, min, 0);
	}

	@Test
	public void testGetMean_2() {

		float max = Calculations.getMax(values);
		assertEquals("max", 0.0f, max, 0);
	}
}
