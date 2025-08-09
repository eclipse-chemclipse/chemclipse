/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.junit.Before;
import org.junit.Test;

public class ComparisonResult_7_Test {

	private static final float DEFAULT_VALUE = 100.0f;
	private ComparisonResult comparisonResult;

	@Before
	public void setUp() throws Exception {

		comparisonResult = new ComparisonResult(DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE);
		// comparisonResult.setPenalty(-22.0f);
	}

	@Test
	public void test1() {

		assertEquals(100.0f, comparisonResult.getMatchFactor(), 0);
	}

	@Test
	public void test2() {

		assertEquals(100.0f, comparisonResult.getReverseMatchFactor(), 0);
	}

	@Test
	public void test3() {

		assertEquals(100.0f, comparisonResult.getMatchFactorDirect(), 0);
	}

	@Test
	public void test4() {

		assertEquals(100.0f, comparisonResult.getReverseMatchFactorDirect(), 0);
	}

	@Test
	public void test5() {

		assertEquals(DEFAULT_VALUE, comparisonResult.getProbability(), 0);
	}

	@Test
	public void test6() {

		assertEquals(DEFAULT_VALUE, comparisonResult.getMatchFactorNotAdjusted(), 0);
	}

	@Test
	public void test7() {

		assertEquals(DEFAULT_VALUE, comparisonResult.getReverseMatchFactorNotAdjusted(), 0);
	}

	@Test
	public void test8() {

		assertEquals(DEFAULT_VALUE, comparisonResult.getMatchFactorDirectNotAdjusted(), 0);
	}

	@Test
	public void test9() {

		assertEquals(DEFAULT_VALUE, comparisonResult.getReverseMatchFactorDirectNotAdjusted(), 0);
	}

	@Test
	public void testSetPenalty01() {

		assertThrows(IllegalArgumentException.class, () -> {
			comparisonResult.setPenalty(-22.0f);
		});
	}

	@Test
	public void testSetPenalty02() {

		assertThrows(IllegalArgumentException.class, () -> {
			comparisonResult.setPenalty(100.1f);
		});
	}

	@Test
	public void testSetPenalty03() {

		comparisonResult.setPenalty(42);
	}

	@Test
	public void testAddPenalty01() {

		comparisonResult.setPenalty(42);
		comparisonResult.addPenalty(10);
		assertEquals(52f, comparisonResult.getPenalty(), 0);
	}

	@Test
	public void testAddPenalty02() {

		comparisonResult.setPenalty(10);
		comparisonResult.addPenalty(90);
		assertEquals(100f, comparisonResult.getPenalty(), 0);
	}

	@Test
	public void testAddPenalty03() {

		comparisonResult.setPenalty(60);
		comparisonResult.addPenalty(60);
		assertEquals(100f, comparisonResult.getPenalty(), 0);
	}

	@Test
	public void testAddPenalty04() {

		comparisonResult.setPenalty(60);
		comparisonResult.addPenalty(-120);
		assertEquals(0f, comparisonResult.getPenalty(), 0);
	}
}
