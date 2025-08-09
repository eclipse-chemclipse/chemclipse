/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.keystore.internal.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Map;

import org.eclipse.chemclipse.keystore.TestPathHelper;
import org.junit.Before;
import org.junit.Test;

public class KeyFileParser_2_ITest {

	private Map<String, String> keyStore;

	@Before
	public void setUp() throws Exception {

		File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_KEYSTORE_II_TEST));
		keyStore = KeyFileParser.readKeysFromFile(file);
	}

	@Test
	public void testAmount() {

		assertEquals(2, keyStore.size());
	}

	@Test
	public void testContainsKey_1() {

		assertFalse(keyStore.containsKey("mykeyid.test"));
	}

	@Test
	public void testContainsKey_2() {

		assertFalse(keyStore.containsKey("mykeyid.hello"));
	}

	@Test
	public void testContainsKey_3() {

		assertTrue(keyStore.containsKey("mykeyid.converter"));
	}

	@Test
	public void testContainsKey_4() {

		assertTrue(keyStore.containsKey("mykeyid.converter.text"));
	}

	@Test
	public void testGetKey_1() {

		assertNull(keyStore.get("mykeyid.test"));
	}

	@Test
	public void testGetKey_2() {

		assertNull(keyStore.get("mykeyid.hello"));
	}

	@Test
	public void testGetKey_3() {

		assertEquals("Hhdjss-237Rhs-378", keyStore.get("mykeyid.converter"));
	}

	@Test
	public void testGetKey_4() {

		assertEquals("Hs893jss-237Rhs-xxx", keyStore.get("mykeyid.converter.text"));
	}
}
