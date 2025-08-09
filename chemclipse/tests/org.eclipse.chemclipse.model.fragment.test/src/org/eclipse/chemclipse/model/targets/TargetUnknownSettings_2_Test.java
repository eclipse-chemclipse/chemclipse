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

public class TargetUnknownSettings_2_Test {

	private TargetUnknownSettings settings = new TargetUnknownSettings();

	@Test
	public void test1() {

		settings.setMarkerStart(null);
		assertEquals("", settings.getMarkerStart());
		settings.setMarkerStart("");
		assertEquals("", settings.getMarkerStart());
		settings.setMarkerStart("(");
		assertEquals("(", settings.getMarkerStart());
	}

	@Test
	public void test2() {

		settings.setMarkerStop(null);
		assertEquals("", settings.getMarkerStop());
		settings.setMarkerStop("");
		assertEquals("", settings.getMarkerStop());
		settings.setMarkerStop(")");
		assertEquals(")", settings.getMarkerStop());
	}

	@Test
	public void test3() {

		settings.setNumberTraces(-1);
		assertEquals(0, settings.getNumberTraces());
		settings.setNumberTraces(0);
		assertEquals(0, settings.getNumberTraces());
		settings.setNumberTraces(1);
		assertEquals(1, settings.getNumberTraces());
	}

	@Test
	public void test4() {

		settings.setTargetName(null);
		assertEquals("", settings.getTargetName());
		settings.setTargetName("");
		assertEquals("", settings.getTargetName());
		settings.setTargetName("unknown");
		assertEquals("unknown", settings.getTargetName());
	}

	@Test
	public void test5() {

		settings.setIncludeIntensityPercent(true);
		assertEquals(true, settings.isIncludeIntensityPercent());
		settings.setIncludeIntensityPercent(false);
		assertEquals(false, settings.isIncludeIntensityPercent());
	}

	@Test
	public void test6() {

		settings.setIncludeRetentionIndex(true);
		assertEquals(true, settings.isIncludeRetentionIndex());
		settings.setIncludeRetentionIndex(false);
		assertEquals(false, settings.isIncludeRetentionIndex());
	}

	@Test
	public void test7() {

		settings.setIncludeRetentionTime(true);
		assertEquals(true, settings.isIncludeRetentionTime());
		settings.setIncludeRetentionTime(false);
		assertEquals(false, settings.isIncludeRetentionTime());
	}
}
