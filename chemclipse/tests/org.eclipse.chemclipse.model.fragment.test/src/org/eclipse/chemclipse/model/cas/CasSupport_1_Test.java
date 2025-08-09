/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/*
 * https://en.wikipedia.org/wiki/CAS_Registry_Number
 */
public class CasSupport_1_Test {

	/*
	 * Correct CAS#: 7732-18-5
	 */

	@Test
	public void test1a() {

		assertTrue(CasSupport.verifyChecksum("7732-18-5"));
	}

	@Test
	public void test1b() {

		assertTrue(CasSupport.verifyChecksum(7732185));
	}

	@Test
	public void test1c() {

		assertFalse(CasSupport.verifyChecksum("7732-18-4"));
	}

	@Test
	public void test1d() {

		assertFalse(CasSupport.verifyChecksum(7732184));
	}

	@Test
	public void test1e() {

		assertFalse(CasSupport.verifyChecksum("7732-185"));
	}

	@Test
	public void test1f() {

		assertFalse(CasSupport.verifyChecksum("773218-5"));
	}

	@Test
	public void test1g() {

		assertFalse(CasSupport.verifyChecksum("7732185"));
	}

	@Test
	public void test1h() {

		assertFalse(CasSupport.verifyChecksum("7732-18-6"));
	}

	@Test
	public void test1i() {

		assertFalse(CasSupport.verifyChecksum(7732186));
	}

	@Test
	public void test2a() {

		assertTrue(CasSupport.verifyChecksum("0-00-0"));
	}

	@Test
	public void test2b() {

		assertTrue(CasSupport.verifyChecksum(0));
	}

	@Test
	public void test2c() {

		assertFalse(CasSupport.verifyChecksum("0-00-1"));
	}

	/*
	 * Out of range test cases.
	 */

	@Test
	public void test3a() {

		assertFalse(CasSupport.verifyChecksum(-1));
	}

	@Test
	public void test3b() {

		assertFalse(CasSupport.verifyChecksum(1000000001));
	}

	@Test
	public void test3c() {

		assertFalse(CasSupport.verifyChecksum("0-00"));
	}

	@Test
	public void test3d() {

		assertFalse(CasSupport.verifyChecksum("0-00-0-"));
	}

	@Test
	public void test3e() {

		assertFalse(CasSupport.verifyChecksum("-0-00-0"));
	}

	@Test
	public void test3f() {

		assertFalse(CasSupport.verifyChecksum(null));
	}

	/*
	 * Existing CAS#
	 */

	@Test
	public void test4() {

		assertTrue(CasSupport.verifyChecksum("100-42-5"));
	}

	@Test
	public void test5() {

		assertTrue(CasSupport.verifyChecksum("71-43-2"));
	}

	@Test
	public void test6() {

		assertTrue(CasSupport.verifyChecksum("108-88-3"));
	}

	@Test
	public void test7() {

		assertTrue(CasSupport.verifyChecksum("5989-27-5"));
	}

	@Test
	public void test8() {

		assertTrue(CasSupport.verifyChecksum("65996-98-7"));
	}
}
