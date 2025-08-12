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

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Before;
import org.junit.Test;

public class SimpleRegression_2_Test {

	private SimpleRegression simpleRegression;

	@Before
	public void setUp() {

		simpleRegression = new SimpleRegression(true);

		simpleRegression.addData(0.0197044334975369, 0.0146511627906977);
		simpleRegression.addData(0.2955665024630540, 0.2474747474747470);
		simpleRegression.addData(2.2167487684729100, 1.8750000000000000);
		simpleRegression.addData(12.3152709359606000, 10.2666666666667000);
		simpleRegression.addData(27.0935960591133000, 22.7981651376147000);
	}

	@Test
	public void testMeanSquareError() {

		assertEquals(0.0024739296329698846d, simpleRegression.getMeanSquareError(), 0);
	}

	@Test
	public void testSlope() {

		assertEquals(0.8406964541842654d, simpleRegression.getSlope(), 0);
	}

	@Test
	public void testIntercept() {

		assertEquals(-0.01151940381461003d, simpleRegression.getIntercept(), 0);
	}

	@Test
	public void testRegressionCoefficient() {

		assertEquals(0.9999805152424263d, simpleRegression.getRSquare(), 0);
	}

	@Test
	public void testInterceptStdErr() {

		assertEquals(0.028596869868268208d, simpleRegression.getInterceptStdErr(), 0);
	}

	@Test
	public void testN() {

		assertEquals(5, simpleRegression.getN());
	}

	@Test
	public void testR() {

		assertEquals(0.9999902575737557d, simpleRegression.getR(), 0);
	}

	@Test
	public void testRegressionSumSquares() {

		assertEquals(380.8948743173564d, simpleRegression.getRegressionSumSquares(), 0);
	}

	@Test
	public void testSignificance() {

		assertEquals(3.65034500404704E-8d, simpleRegression.getSignificance(), 0);
	}

	@Test
	public void testSlopeConfidenceInterval() {

		assertEquals(0.006818540236764045d, simpleRegression.getSlopeConfidenceInterval(), 0);
	}

	@Test
	public void testSlopeStdErr() {

		assertEquals(0.0021425468283177863d, simpleRegression.getSlopeStdErr(), 0);
	}

	@Test
	public void testSumOfCrossProducts() {

		assertEquals(453.0706325946644d, simpleRegression.getSumOfCrossProducts(), 0);
	}

	@Test
	public void testSumSquaredErrors() {

		assertEquals(0.007421788898909654d, simpleRegression.getSumSquaredErrors(), 0);
	}

	@Test
	public void testTotalSumSquares() {

		assertEquals(380.90229610625533d, simpleRegression.getTotalSumSquares(), 0);
	}

	@Test
	public void testXSumSquares() {

		assertEquals(538.9229731369362d, simpleRegression.getXSumSquares(), 0);
	}
}
