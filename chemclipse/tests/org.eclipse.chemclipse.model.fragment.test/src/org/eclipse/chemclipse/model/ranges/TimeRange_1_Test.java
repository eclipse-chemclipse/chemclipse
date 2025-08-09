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

public class TimeRange_1_Test {

	private TimeRange timeRange = new TimeRange("", 0, 0);

	@Test
	public void test0() {

		assertEquals("", timeRange.getIdentifier());
	}

	@Test
	public void test1() {

		assertEquals(0, timeRange.getStart());
	}

	@Test
	public void test2() {

		assertEquals(0, timeRange.getMaximum());
	}

	@Test
	public void test3() {

		assertEquals(0, timeRange.getStop());
	}
}
