/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IonRoundMethod_MINUS_00_1_Test2 {

	private IonRoundMethod ionRoundMethod = IonRoundMethod.MINUS_00;

	@Test
	public void test1() {

		assertEquals(0, ionRoundMethod.round(Double.NaN));
	}

	@Test
	public void test2() {

		assertEquals(0, ionRoundMethod.round(Double.NEGATIVE_INFINITY));
	}

	@Test
	public void test3() {

		assertEquals(0, ionRoundMethod.round(Double.POSITIVE_INFINITY));
	}

	@Test
	public void test4() {

		assertEquals(0, ionRoundMethod.round(-Double.MIN_NORMAL));
	}

	@Test
	public void test5() {

		assertEquals(17, ionRoundMethod.round(17.9d));
	}

	@Test
	public void test6() {

		assertEquals(18, ionRoundMethod.round(18.0d));
	}

	@Test
	public void test7() {

		assertEquals(18, ionRoundMethod.round(18.1d));
	}

	@Test
	public void test8() {

		assertEquals(18, ionRoundMethod.round(18.9d));
	}

	@Test
	public void test9() {

		assertEquals(19, ionRoundMethod.round(19.0d));
	}

	@Test
	public void test10() {

		assertEquals("Round m/z from -0.0 (incl.) to +1.0 (excl.)", ionRoundMethod.label());
	}
}