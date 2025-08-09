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
package org.eclipse.chemclipse.model.ranges;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeRange_8_Test {

	private TimeRange timeRange = new TimeRange("Test", 500, 350, 200);

	@Test
	public void test0() {

		assertEquals("Test", timeRange.getIdentifier());
	}

	@Test
	public void test1() {

		assertEquals(200, timeRange.getStart());
	}

	@Test
	public void test2() {

		assertEquals(350, timeRange.getMaximum());
	}

	@Test
	public void test3() {

		assertEquals(500, timeRange.getStop());
	}

	@Test
	public void test4() {

		timeRange.update(600, 800);
		assertEquals(600, timeRange.getStart());
		assertEquals(700, timeRange.getMaximum());
		assertEquals(800, timeRange.getStop());
	}

	@Test
	public void test5() {

		timeRange.update(800, 600);
		assertEquals(600, timeRange.getStart());
		assertEquals(700, timeRange.getMaximum());
		assertEquals(800, timeRange.getStop());
	}
}
