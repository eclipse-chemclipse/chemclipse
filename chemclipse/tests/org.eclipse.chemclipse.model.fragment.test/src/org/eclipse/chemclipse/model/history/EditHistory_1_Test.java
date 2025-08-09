/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.history;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.support.history.EditHistory;
import org.eclipse.chemclipse.support.history.EditHistorySortOrder;
import org.eclipse.chemclipse.support.history.EditInformation;
import org.eclipse.chemclipse.support.history.EditInformationComparator;
import org.eclipse.chemclipse.support.history.IEditHistory;
import org.eclipse.chemclipse.support.history.IEditInformation;
import org.junit.Before;
import org.junit.Test;

public class EditHistory_1_Test {

	private IEditHistory editHistory;
	private IEditInformation editInformation;
	private final String entry1 = "I have modified the chromatogram.";
	private final String entry2 = "Me too.";
	private final String entry3 = "What are we doing now?";
	private int sleepTime = 100;

	@Before
	public void setUp() throws Exception {

		editHistory = new EditHistory();
		/*
		 * Why do we need a Thread.sleep() here? If the EditInformation objects
		 * are created too fast, the getDate().getTime() will be the same in all
		 * the three cases. If the date is the same, the objects can't be sorted
		 * by date.
		 */
		editInformation = new EditInformation(entry1);
		editHistory.add(editInformation);
		Thread.sleep(sleepTime);
		editInformation = new EditInformation(entry2);
		editHistory.add(editInformation);
		Thread.sleep(sleepTime);
		editInformation = new EditInformation(entry3);
		editHistory.add(editInformation);
		Thread.sleep(sleepTime);
	}

	@Test
	public void testGetEditHistory_1() {

		List<IEditInformation> history = new ArrayList<>(editHistory);
		assertEquals("1st entry", entry1, history.get(0).getDescription());
		assertEquals("2nd entry", entry2, history.get(1).getDescription());
		assertEquals("3rd entry", entry3, history.get(2).getDescription());
	}

	@Test
	public void testGetEditHistory_2() {

		List<IEditInformation> history = new ArrayList<>(editHistory);
		Collections.sort(history, new EditInformationComparator(EditHistorySortOrder.DATE_ASC));
		assertEquals("1st entry", entry1, history.get(0).getDescription());
		assertEquals("2nd entry", entry2, history.get(1).getDescription());
		assertEquals("3rd entry", entry3, history.get(2).getDescription());
	}

	@Test
	public void testGetEditHistory_3() {

		List<IEditInformation> history = new ArrayList<>(editHistory);
		Collections.sort(history, new EditInformationComparator(EditHistorySortOrder.DATE_DESC));
		assertEquals("1st entry", entry3, history.get(0).getDescription());
		assertEquals("2nd entry", entry2, history.get(1).getDescription());
		assertEquals("3rd entry", entry1, history.get(2).getDescription());
	}
}
