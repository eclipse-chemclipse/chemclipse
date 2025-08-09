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
package org.eclipse.chemclipse.model.cas;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/*
 * https://en.wikipedia.org/wiki/CAS_Registry_Number
 */
public class CasSupport_2_Test {

	@Test
	public void test1() {

		assertEquals("", CasSupport.format(""));
	}

	@Test
	public void test2() {

		assertEquals("a", CasSupport.format("a"));
	}

	@Test
	public void test3() {

		assertEquals("a1", CasSupport.format("a1"));
	}

	@Test
	public void test4() {

		assertEquals("123", CasSupport.format("123"));
	}

	@Test
	public void test5() {

		assertEquals("1a234", CasSupport.format("1a234"));
	}

	@Test
	public void test6() {

		assertEquals("0-00-0", CasSupport.format(null));
	}

	@Test
	public void test7() {

		assertEquals("0-00-0", CasSupport.format("0"));
	}

	@Test
	public void test8() {

		assertEquals("0-00-0", CasSupport.format("0-00-0"));
	}

	@Test
	public void test9() {

		assertEquals("0-00-0", CasSupport.format("0000"));
	}

	@Test
	public void test10() {

		assertEquals("7732-18-5", CasSupport.format("7732185"));
	}

	@Test
	public void test11() {

		assertEquals("100-42-5", CasSupport.format("100425"));
	}

	@Test
	public void test12() {

		assertEquals("71-43-2", CasSupport.format("71432"));
	}

	@Test
	public void test13() {

		assertEquals("108-88-3", CasSupport.format("108883"));
	}

	@Test
	public void test14() {

		assertEquals("5989-27-5", CasSupport.format("5989275"));
	}

	@Test
	public void test15() {

		assertEquals("65996-98-7", CasSupport.format("65996987"));
	}
}
