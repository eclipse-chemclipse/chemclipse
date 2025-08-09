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

import java.util.Date;

import org.eclipse.chemclipse.support.history.EditInformation;
import org.eclipse.chemclipse.support.history.IEditInformation;
import org.junit.Before;
import org.junit.Test;

public class EditInformation_2_Test {

	private IEditInformation editInformation;
	private Date date = new Date();
	private final String entry = "I have modified the chromatogram.";

	@Before
	public void setUp() throws Exception {

		editInformation = new EditInformation(entry);
	}

	@Test
	public void testGetEditHistory_1() {

		long millisecondsEdit = editInformation.getDate().getTime();
		long millisecondsCheck = date.getTime();
		assertEquals("getDate", millisecondsCheck, millisecondsEdit, 100L); // Sometimes the time deviates in the check by 100 ms
	}

	@Test
	public void testGetEditHistory_2() {

		assertEquals("getDescription", entry, editInformation.getDescription());
	}
}
