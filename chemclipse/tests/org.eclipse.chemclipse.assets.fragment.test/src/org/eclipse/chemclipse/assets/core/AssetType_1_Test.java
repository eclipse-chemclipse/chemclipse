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
package org.eclipse.chemclipse.assets.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AssetType_1_Test {

	@Test
	public void test1() {

		AssetType assetType = AssetType.CONFIGURATION;
		assertEquals("Configuration", assetType.label());
		assertEquals(".cfg", assetType.extension());
		assertTrue(assetType.directory().exists());
		assertEquals("Service Configuration File", assetType.description());
	}

	@Test
	public void test2() {

		AssetType assetType = AssetType.METHOD;
		assertEquals("Process Method", assetType.label());
		assertEquals(".ocm", assetType.extension());
		assertTrue(assetType.directory().exists());
		assertEquals("Process Method File", assetType.description());
	}

	@Test
	public void test3() {

		AssetType assetType = AssetType.PLUGIN;
		assertEquals("Plugin", assetType.label());
		assertEquals(".jar", assetType.extension());
		assertTrue(assetType.directory().exists());
		assertEquals("Plugin Extension", assetType.description());
	}
}
