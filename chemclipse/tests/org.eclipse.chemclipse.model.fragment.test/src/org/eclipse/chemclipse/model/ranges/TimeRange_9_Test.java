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

public class TimeRange_9_Test {

	private TimeRange timeRange = new TimeRange("Test", 500, 750, 1000);

	@Test
	public void test0() {

		assertEquals("Test", timeRange.getIdentifier());
	}

	@Test
	public void test1() {

		assertEquals(500, timeRange.getStart());
	}

	@Test
	public void test2() {

		assertEquals(750, timeRange.getMaximum());
	}

	@Test
	public void test3() {

		assertEquals(1000, timeRange.getStop());
	}

	@Test
	public void test4() {

		timeRange.updateStart(800);
		assertEquals(800, timeRange.getStart());
		assertEquals(900, timeRange.getMaximum());
		assertEquals(1000, timeRange.getStop());
	}

	@Test
	public void test5() {

		timeRange.updateStop(700);
		assertEquals(500, timeRange.getStart());
		assertEquals(600, timeRange.getMaximum());
		assertEquals(700, timeRange.getStop());
	}
}
