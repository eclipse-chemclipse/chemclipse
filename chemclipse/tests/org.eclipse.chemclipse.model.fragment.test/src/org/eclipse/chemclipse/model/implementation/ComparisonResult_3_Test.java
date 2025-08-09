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

import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ComparisonResult_3_Test extends TestCase {

	private static final float DEFAULT_VALUE = 100.0f;
	private ComparisonResult comparisonResult;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		comparisonResult = new ComparisonResult(DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE);
		comparisonResult.setPenalty(22.0f);
	}

	@Test
	public void test1() {

		assertEquals(78.0f, comparisonResult.getMatchFactor());
	}

	@Test
	public void test2() {

		assertEquals(78.0f, comparisonResult.getReverseMatchFactor());
	}

	@Test
	public void test3() {

		assertEquals(78.0f, comparisonResult.getMatchFactorDirect());
	}

	@Test
	public void test4() {

		assertEquals(78.0f, comparisonResult.getReverseMatchFactorDirect());
	}

	@Test
	public void test5() {

		assertEquals(DEFAULT_VALUE, comparisonResult.getProbability());
	}

	@Test
	public void test6() {

		assertEquals(DEFAULT_VALUE, comparisonResult.getMatchFactorNotAdjusted());
	}

	@Test
	public void test7() {

		assertEquals(DEFAULT_VALUE, comparisonResult.getReverseMatchFactorNotAdjusted());
	}

	@Test
	public void test8() {

		assertEquals(DEFAULT_VALUE, comparisonResult.getMatchFactorDirectNotAdjusted());
	}

	@Test
	public void test9() {

		assertEquals(DEFAULT_VALUE, comparisonResult.getReverseMatchFactorDirectNotAdjusted());
	}
}
