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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing median.
 * 
 * @author eselmeister
 */
public class Calculations_4_Test {

	private double[] values;

	@Before
	public void setUp() {

		values = new double[12];
		values[0] = 735.0d;
		values[1] = 504.0d;
		values[2] = 561.0d;
		values[3] = 504.0d;
		values[4] = 400.0d;
		values[5] = 501.0d;
		values[6] = 443.0d;
		values[7] = 430.0d;
		values[8] = 337.0d;
		values[9] = 345.0d;
		values[10] = 304.0d;
		values[11] = 381.0d;
	}

	@Test
	public void testGetMedian_1() {

		double result = Calculations.getMedian(values);
		assertEquals("getMedian", 436.5d, result, 0);
	}

	@Test
	public void testGetMedian_2() {

		List<Double> val = new ArrayList<Double>();
		for(double value : values) {
			val.add(value);
		}
		double result = Calculations.getMedian(val);
		assertEquals("getMedian", 436.5d, result, 0);
	}

	@Test
	public void testGetMedian_3() {

		List<Double> val = new ArrayList<Double>();
		/*
		 * Add null to test null values.<br/> To preserve the median, a higher
		 * value than the existing max value needs to be added too.<br/> Add in
		 * this case 780.0d.
		 */
		val.add(null);
		for(double value : values) {
			val.add(value);
		}
		val.add(780.0d);
		double result = Calculations.getMedian(val);
		assertEquals("getMedian", 436.5d, result, 0);
	}
}
