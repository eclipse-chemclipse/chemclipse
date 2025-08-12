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
package org.eclipse.chemclipse.numeric.miscellaneous;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the valuesAreIncreasing(double[] values) method.
 */
public class Evaluation_1_Test {

	@Test
	public void testValuesAreaIncreasing_1() {

		double[] values = new double[3];
		values[0] = 4.5d;
		values[1] = 5.4d;
		values[2] = 5.8d;
		assertTrue(Evaluation.valuesAreIncreasing(values));
	}

	@Test
	public void testValuesAreaIncreasing_2() {

		double[] values = new double[2];
		values[0] = 4.5d;
		values[1] = 5.4d;
		assertTrue(Evaluation.valuesAreIncreasing(values));
	}

	@Test
	public void testValuesAreaIncreasing_3() {

		double[] values = new double[3];
		values[0] = 4.5d;
		values[1] = 5.4d;
		assertFalse(Evaluation.valuesAreIncreasing(values));
	}

	@Test
	public void testValuesAreaIncreasing_4() {

		double[] values = new double[1];
		values[0] = 4.5d;
		assertFalse(Evaluation.valuesAreIncreasing(values));
	}

	@Test
	public void testValuesAreaIncreasing_5() {

		double[] values = new double[3];
		values[0] = 4.5d;
		values[1] = 4.4d;
		values[2] = 5.8d;
		assertFalse(Evaluation.valuesAreIncreasing(values));
	}
}
