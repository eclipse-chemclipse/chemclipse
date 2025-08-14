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
package org.eclipse.chemclipse.support.text;

import static org.junit.Assert.assertEquals;

import java.text.NumberFormat;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

public class ValueFormat_4_Test {

	private NumberFormat numberFormat;

	@Before
	public void setUp() {

		numberFormat = ValueFormat.getNumberFormatEnglish();
	}

	@Test
	public void test1() throws ParseException {

		assertEquals(0.0d, numberFormat.parse("0.0").doubleValue(), 0);
	}

	@Test
	public void test2() throws ParseException {

		assertEquals(0.1d, numberFormat.parse("0.1").doubleValue(), 0);
	}

	@Test
	public void test3() throws ParseException {

		assertEquals(1.0d, numberFormat.parse("1.0").doubleValue(), 0);
	}

	@Test
	public void test4() throws ParseException {

		assertEquals(1.1d, numberFormat.parse("1.1").doubleValue(), 0);
	}
}
