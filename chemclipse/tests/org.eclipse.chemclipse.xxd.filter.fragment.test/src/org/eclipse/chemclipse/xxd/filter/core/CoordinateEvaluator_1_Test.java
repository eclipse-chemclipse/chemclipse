/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.filter.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.xxd.filter.model.RangeOption;
import org.junit.Test;

public class CoordinateEvaluator_1_Test {

	@Test
	public void test1() {

		assertFalse(CoordinateEvaluator.isMatch(0, RangeOption.NONE, 0));
	}

	@Test
	public void test2() {

		assertTrue(CoordinateEvaluator.isMatch(2, RangeOption.EQUALS, 2));
	}

	@Test
	public void test3() {

		assertFalse(CoordinateEvaluator.isMatch(2, RangeOption.LOWER, 2));
	}

	@Test
	public void test4() {

		assertTrue(CoordinateEvaluator.isMatch(2, RangeOption.LOWER_EQUALS, 2));
	}

	@Test
	public void test5() {

		assertTrue(CoordinateEvaluator.isMatch(2, RangeOption.HIGHER_EQUALS, 2));
	}

	@Test
	public void test6() {

		assertFalse(CoordinateEvaluator.isMatch(2, RangeOption.HIGHER, 2));
	}
}