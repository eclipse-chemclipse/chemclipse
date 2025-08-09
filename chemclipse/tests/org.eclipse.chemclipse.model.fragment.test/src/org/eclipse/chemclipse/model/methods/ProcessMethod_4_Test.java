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
package org.eclipse.chemclipse.model.methods;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.methods.IProcessEntry;
import org.eclipse.chemclipse.processing.methods.ProcessEntry;
import org.eclipse.chemclipse.processing.methods.ProcessEntryContainer;
import org.eclipse.chemclipse.processing.methods.ProcessMethod;
import org.junit.Before;
import org.junit.Test;

public class ProcessMethod_4_Test {

	private ProcessMethod processMethod;
	private IProcessEntry processEntry;

	@Before
	public void setUp() throws Exception {

		Set<DataCategory> dataCategories = new HashSet<>();
		dataCategories.add(DataCategory.MSD);
		processMethod = new ProcessMethod(dataCategories);
		processMethod.addProfile("Test");
		processMethod.setActiveProfile("Test");

		processEntry = new ProcessEntry(processMethod);
		processEntry.setActiveProfile(ProcessEntryContainer.DEFAULT_PROFILE);
		processEntry.setSettings("Hello World");
		processEntry.setActiveProfile("Test");
		processEntry.setSettings("This is another setting");
		processEntry.setActiveProfile(ProcessEntryContainer.DEFAULT_PROFILE);
		processMethod.getEntries().add(processEntry);

		processMethod.setActiveProfile("Test");
	}

	@Test
	public void test1() {

		assertEquals("Test", processMethod.getActiveProfile());
		assertEquals("This is another setting", processEntry.getSettings());
	}

	@Test
	public void test2() {

		ProcessMethod processMethodNew = new ProcessMethod(processMethod);
		IProcessEntry processEntryNew = processMethodNew.getEntries().get(0);
		assertEquals("Test", processMethodNew.getActiveProfile());
		assertEquals("This is another setting", processEntryNew.getSettings());
	}
}
