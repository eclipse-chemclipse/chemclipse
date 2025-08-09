/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.identifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComparisonResult_1_Test {

	private IComparisonResult comparisonResult = new ComparisonResult(80.0f);

	@Test
	public void test1() {

		assertNotNull(comparisonResult.getRatingSupplier());
	}

	@Test
	public void test2() {

		assertNotNull(ComparisonResult.COMPARISON_RESULT_NO_MATCH);
	}

	@Test
	public void test3() {

		IComparisonResult comparisonResult = ComparisonResult.COMPARISON_RESULT_NO_MATCH;
		assertEquals(0.0f, comparisonResult.getMatchFactor(), 0);
		assertFalse(comparisonResult.isMatch());

		comparisonResult.setMatch(true);
		assertEquals(0.0f, comparisonResult.getMatchFactor(), 0);
		assertFalse(comparisonResult.isMatch());
	}

	@Test
	public void test4() {

		assertNotNull(ComparisonResult.COMPARISON_RESULT_BEST_MATCH);
	}

	@Test
	public void test5() {

		IComparisonResult comparisonResult = ComparisonResult.COMPARISON_RESULT_BEST_MATCH;
		assertEquals(100.0f, comparisonResult.getMatchFactor(), 0);
		assertTrue(comparisonResult.isMatch());

		comparisonResult.setPenalty(20.0f);
		comparisonResult.setMatch(false);
		assertEquals(100.0f, comparisonResult.getMatchFactor(), 0);
		assertTrue(comparisonResult.isMatch());
	}
}