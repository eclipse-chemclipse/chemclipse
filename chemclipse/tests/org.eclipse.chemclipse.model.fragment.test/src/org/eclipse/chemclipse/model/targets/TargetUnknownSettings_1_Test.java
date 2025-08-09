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
package org.eclipse.chemclipse.model.targets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TargetUnknownSettings_1_Test {

	private TargetUnknownSettings settings = new TargetUnknownSettings();

	@Test
	public void test1() {

		assertEquals("[", settings.getMarkerStart());
	}

	@Test
	public void test2() {

		assertEquals("]", settings.getMarkerStop());
	}

	@Test
	public void test3() {

		assertEquals(5, settings.getNumberTraces());
	}

	@Test
	public void test4() {

		assertEquals("Unknown", settings.getTargetName());
	}

	@Test
	public void test5() {

		assertEquals(false, settings.isIncludeIntensityPercent());
	}

	@Test
	public void test6() {

		assertEquals(false, settings.isIncludeRetentionIndex());
	}

	@Test
	public void test7() {

		assertEquals(false, settings.isIncludeRetentionTime());
	}
}
