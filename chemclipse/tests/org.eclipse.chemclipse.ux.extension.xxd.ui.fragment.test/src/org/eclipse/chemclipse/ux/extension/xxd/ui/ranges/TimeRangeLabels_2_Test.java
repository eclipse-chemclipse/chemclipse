/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.ranges;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TimeRangeLabels_2_Test {

	private TimeRangeLabels timeRangeLabels;

	@Before
	public void setUp() {

		timeRangeLabels = new TimeRangeLabels("Alkane", "C20");
	}

	@Test
	public void test1() {

		assertEquals("Alkane", timeRangeLabels.getTitle());
	}

	@Test
	public void test2() {

		assertEquals("C20", timeRangeLabels.getInitialValue());
	}

	@Test
	public void test3() {

		assertEquals("Add a new Alkane.", timeRangeLabels.getAddMessage());
	}

	@Test
	public void test4() {

		assertEquals("Please define a new Alkane.", timeRangeLabels.getAddError());
	}

	@Test
	public void test5() {

		assertEquals("The Alkane exists already.", timeRangeLabels.getAddExists());
	}

	@Test
	public void test6() {

		assertEquals("Create a new Alkane.", timeRangeLabels.getCreateMessage());
	}

	@Test
	public void test7() {

		assertEquals("C20 | 10.2 | 10.4 | 10.6", timeRangeLabels.getCreateInitialValue());
	}

	@Test
	public void test8() {

		assertEquals("Edit the selected Alkane.", timeRangeLabels.getEditMessage());
	}

	@Test
	public void test9() {

		assertEquals("Would you like to delete the selected Alkane?", timeRangeLabels.getDeleteMessage());
	}

	@Test
	public void test10() {

		assertEquals("Would you like to delete the all Alkanes?", timeRangeLabels.getClearMessage());
	}

	@Test
	public void test11() {

		assertEquals("The Alkane must not contain the following delimiter '|'.", timeRangeLabels.getErrorDelimiter());
	}

	@Test
	public void test12() {

		assertEquals(1, timeRangeLabels.getProposals().length);
	}

	@Test
	public void test13() {

		assertEquals("C20", timeRangeLabels.getProposals()[0]);
	}
}