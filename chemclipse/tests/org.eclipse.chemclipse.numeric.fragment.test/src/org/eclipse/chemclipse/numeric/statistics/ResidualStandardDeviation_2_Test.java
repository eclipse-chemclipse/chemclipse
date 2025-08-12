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

public class ResidualStandardDeviation_2_Test {

	private ResidualStandardDeviationCalculator calculator;
	private double result;

	@Before
	public void setUp() {

		calculator = new ResidualStandardDeviationCalculator();
		// (y, x)
		double[][] data = {{0.0146511627906977, 0.0197044334975369}, {0.2474747474747470, 0.2955665024630540}, {1.8750000000000000, 2.2167487684729100}, {10.2666666666667000, 12.3152709359606000}, {22.7981651376147000, 27.0935960591133000}};
		result = calculator.calculate(data);
	}

	@Test
	public void testMeanSquareError() {

		assertEquals(0.04973861309857649d, result, 0);
	}
}
