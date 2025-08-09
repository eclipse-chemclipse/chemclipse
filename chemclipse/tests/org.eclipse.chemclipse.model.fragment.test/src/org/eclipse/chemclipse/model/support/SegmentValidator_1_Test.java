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
package org.eclipse.chemclipse.model.support;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SegmentValidator_1_Test {

	private SegmentValidatorClassic validator = new SegmentValidatorClassic();

	@Test
	public void test1() {

		/*
		 * 13 values
		 * Start above mean
		 * 6 crossings
		 * false
		 */
		double[] values = new double[]{70, 70, 70, 80, 70, 100, 20, 70, 40, 80, 13, 90, 100};
		double mean = 58;
		assertFalse(validator.acceptSegment(values, mean));
	}

	@Test
	public void test2() {

		/*
		 * 13 values
		 * Start above mean
		 * 7 crossings
		 * true
		 */
		double[] values = new double[]{70, 70, 70, 80, 20, 100, 20, 70, 40, 80, 13, 90, 100};
		double mean = 58;
		assertTrue(validator.acceptSegment(values, mean));
	}

	@Test
	public void test3() {

		/*
		 * 13 values
		 * Start under mean
		 * 6 crossings
		 * false
		 */
		double[] values = new double[]{40, 40, 40, 80, 20, 40, 20, 30, 40, 80, 13, 90, 40};
		double mean = 58;
		assertFalse(validator.acceptSegment(values, mean));
	}

	@Test
	public void test4() {

		/*
		 * 13 values
		 * Start under mean
		 * 7 crossings
		 * true
		 */
		double[] values = new double[]{40, 40, 40, 80, 20, 80, 20, 30, 40, 80, 13, 90, 40};
		double mean = 58;
		assertTrue(validator.acceptSegment(values, mean));
	}
}