/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.support.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ValueParserSupport_5_Test {

	private ValueParserSupport support = new ValueParserSupport();
	private String[] values;

	@Before
	public void setUp() {

		values = new String[]{Float.toString(28.3f)};
	}

	@Test
	public void test1() {

		assertEquals(1, values.length);
	}

	@Test
	public void test2() {

		assertEquals(false, support.parseBoolean(values, 0));
	}

	@Test
	public void test3() {

		assertEquals(false, support.parseBoolean(values, 0, false));
	}

	@Test
	public void test4() {

		assertEquals(true, support.parseBoolean(values, 0, true));
	}

	@Test
	public void test5() {

		assertEquals("28.3", support.parseString(values, 0));
	}

	@Test
	public void test6() {

		assertEquals("28.3", support.parseString(values, 0, ""));
	}

	@Test
	public void test7() {

		assertEquals("28.3", support.parseString(values, 0, "Test"));
	}

	@Test
	public void test8() {

		assertEquals(28.3f, support.parseFloat(values, 0), 0);
	}

	@Test
	public void test9() {

		assertEquals(28.3f, support.parseFloat(values, 0, 0.0f), 0);
	}

	@Test
	public void test10() {

		assertEquals(28.3f, support.parseFloat(values, 0, 1.0f), 0);
	}

	@Test
	public void test11() {

		assertEquals(28.3d, support.parseDouble(values, 0), 0);
	}

	@Test
	public void test12() {

		assertEquals(28.3d, support.parseDouble(values, 0, 0.0d), 0);
	}

	@Test
	public void test13() {

		assertEquals(28.3d, support.parseDouble(values, 0, 1.0d), 0);
	}
}
