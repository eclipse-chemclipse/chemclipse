/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
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
public class CasSupport_3_Test {

	@Test
	public void test1() {

		assertEquals("", CasSupport.calculateChecksum(null));
	}

	@Test
	public void test2() {

		assertEquals("", CasSupport.calculateChecksum(""));
	}

	@Test
	public void test3() {

		assertEquals("", CasSupport.calculateChecksum("a"));
	}

	@Test
	public void test4() {

		assertEquals("", CasSupport.calculateChecksum("a-"));
	}

	@Test
	public void test5() {

		assertEquals("0", CasSupport.calculateChecksum("0-00-"));
	}

	@Test
	public void test6() {

		assertEquals("5", CasSupport.calculateChecksum("7732-18-"));
	}

	@Test
	public void test7() {

		assertEquals("5", CasSupport.calculateChecksum("100-42-"));
	}

	@Test
	public void test8() {

		assertEquals("2", CasSupport.calculateChecksum("71-43-"));
	}

	@Test
	public void test9() {

		assertEquals("3", CasSupport.calculateChecksum("108-88-"));
	}

	@Test
	public void test10() {

		assertEquals("5", CasSupport.calculateChecksum("5989-27-"));
	}

	@Test
	public void test11() {

		assertEquals("7", CasSupport.calculateChecksum("65996-98-"));
	}
}