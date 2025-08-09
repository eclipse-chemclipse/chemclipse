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
package org.eclipse.chemclipse.model.ranges;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TimeRange_10_Test {

	@Test
	public void test0a() {

		assertThrows(IllegalArgumentException.class, () -> {
			new TimeRange("Test", -1, -1);
		});
	}

	@Test
	public void test0b() {

		assertThrows(IllegalArgumentException.class, () -> {
			new TimeRange("Test", -1, -1, -1);
		});
	}

	@Test
	public void test1a() {

		TimeRange timeRange = new TimeRange("Test", 0, 0);
		assertEquals(0, timeRange.getStart());
		assertEquals(0, timeRange.getMaximum());
		assertEquals(0, timeRange.getStop());
	}

	@Test
	public void test1b() {

		TimeRange timeRange = new TimeRange("Test", 0, 0, 0);
		assertEquals(0, timeRange.getStart());
		assertEquals(0, timeRange.getMaximum());
		assertEquals(0, timeRange.getStop());
	}

	@Test
	public void test2a() {

		TimeRange timeRange = new TimeRange("Test", 1, 1);
		assertEquals(1, timeRange.getStart());
		assertEquals(1, timeRange.getMaximum());
		assertEquals(1, timeRange.getStop());
	}

	@Test
	public void test2b() {

		TimeRange timeRange = new TimeRange("Test", 1, 1, 1);
		assertEquals(1, timeRange.getStart());
		assertEquals(1, timeRange.getMaximum());
		assertEquals(1, timeRange.getStop());
	}
}
