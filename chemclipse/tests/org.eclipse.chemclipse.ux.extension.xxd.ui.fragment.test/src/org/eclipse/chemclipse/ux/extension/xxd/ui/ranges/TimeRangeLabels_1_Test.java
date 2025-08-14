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

import org.junit.Test;

public class TimeRangeLabels_1_Test {

	private TimeRangeLabels timeRangeLabels = new TimeRangeLabels();

	@Test
	public void test1() {

		assertEquals("Time Range", timeRangeLabels.getTitle());
	}

	@Test
	public void test2() {

		assertEquals("C10", timeRangeLabels.getInitialValue());
	}

	@Test
	public void test3() {

		assertEquals("Add a new Time Range.", timeRangeLabels.getAddMessage());
	}

	@Test
	public void test4() {

		assertEquals("Please define a new Time Range.", timeRangeLabels.getAddError());
	}

	@Test
	public void test5() {

		assertEquals("The Time Range exists already.", timeRangeLabels.getAddExists());
	}

	@Test
	public void test6() {

		assertEquals("Create a new Time Range.", timeRangeLabels.getCreateMessage());
	}

	@Test
	public void test7() {

		assertEquals("C10 | 10.2 | 10.4 | 10.6", timeRangeLabels.getCreateInitialValue());
	}

	@Test
	public void test8() {

		assertEquals("Edit the selected Time Range.", timeRangeLabels.getEditMessage());
	}

	@Test
	public void test9() {

		assertEquals("Would you like to delete the selected Time Range?", timeRangeLabels.getDeleteMessage());
	}

	@Test
	public void test10() {

		assertEquals("Would you like to delete the all Time Ranges?", timeRangeLabels.getClearMessage());
	}

	@Test
	public void test11() {

		assertEquals("The Time Range must not contain the following delimiter '|'.", timeRangeLabels.getErrorDelimiter());
	}

	@Test
	public void test12() {

		assertEquals(3, timeRangeLabels.getProposals().length);
	}

	@Test
	public void test13() {

		assertEquals("C10", timeRangeLabels.getProposals()[0]);
	}

	@Test
	public void test14() {

		assertEquals("C11", timeRangeLabels.getProposals()[1]);
	}

	@Test
	public void test15() {

		assertEquals("C12", timeRangeLabels.getProposals()[2]);
	}
}