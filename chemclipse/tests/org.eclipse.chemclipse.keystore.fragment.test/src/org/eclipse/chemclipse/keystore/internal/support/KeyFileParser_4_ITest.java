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

import java.util.Map;

import org.junit.Test;

public class KeyFileParser_4_ITest {

	private Map<String, String> keyStore = KeyFileParser.readKeysFromFile(null);

	@Test
	public void testAmount() {

		assertEquals(0, keyStore.size());
	}

	@Test
	public void testContainsKey_1() {

		assertFalse(keyStore.containsKey("mykeyid"));
	}

	@Test
	public void testGetKey_1() {

		assertNull(keyStore.get("mykeyid"));
	}
}
