/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
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

public class ResidualStandardDeviation_1_Test {

	/*
	 * http://office.microsoft.com/de-de/excel-help/stfehleryx-HP005209284.aspx
	 */
	private ResidualStandardDeviationCalculator calculator;
	private double result;

	@Before
	public void setUp() {

		calculator = new ResidualStandardDeviationCalculator();
		// (y, x)
		double[][] data = {{2, 6}, {3, 5}, {9, 11}, {1, 7}, {8, 5}, {7, 4}, {5, 4}};
		result = calculator.calculate(data);
	}

	@Test
	public void testMeanSquareError() {

		assertEquals(3.305718950210041d, result, 0);
	}
}
