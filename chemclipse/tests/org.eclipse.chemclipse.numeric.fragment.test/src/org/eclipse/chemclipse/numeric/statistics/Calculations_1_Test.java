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

public class Calculations_1_Test {

	private double[] values;

	@Before
	public void setUp() {

		values = new double[]{45.8, 48.4, 78.7, 86.7, 64.6};
	}

	@Test
	public void testGetMean_1() {

		double result = Calculations.getMean(values);
		assertEquals("getMean", 64.83999999999999, result, 0);
	}
}
