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
import org.eclipse.chemclipse.processing.methods.ProcessEntry;
import org.eclipse.chemclipse.processing.methods.ProcessEntryContainer;
import org.eclipse.chemclipse.processing.methods.ProcessMethod;
import org.junit.Before;
import org.junit.Test;

public class ProcessEntry_3_Test {

	private ProcessEntry processEntry;

	@Before
	public void setUp() throws Exception {

		Set<DataCategory> dataCategories = new HashSet<>();
		dataCategories.add(DataCategory.MSD);
		ProcessEntryContainer processEntryContainer = new ProcessMethod(dataCategories);
		processEntry = new ProcessEntry(processEntryContainer);
	}

	@Test
	public void test1() {

		processEntry.setActiveProfile("Test");
		processEntry.setSettings("Hello World");
		processEntry.setActiveProfile("My");
		processEntry.setActiveProfile("Entry");

		processEntry.setActiveProfile(ProcessEntryContainer.DEFAULT_PROFILE);
		assertEquals("", processEntry.getSettings());
		assertEquals("Hello World", processEntry.getSettings("Test"));
		assertEquals("", processEntry.getSettings("My"));
		assertEquals("", processEntry.getSettings("Entry"));
	}

	@Test
	public void test2() {

		processEntry.setActiveProfile("Test");
		processEntry.setSettings("Hello World");
		processEntry.setActiveProfile("My");
		processEntry.copySettings(null);
		processEntry.setActiveProfile("Entry");
		processEntry.copySettings("");
		processEntry.setActiveProfile("Extended");
		processEntry.copySettings("Testx");

		processEntry.setActiveProfile(ProcessEntryContainer.DEFAULT_PROFILE);
		assertEquals("", processEntry.getSettings());
		assertEquals("Hello World", processEntry.getSettings("Test"));
		assertEquals("", processEntry.getSettings("My"));
		assertEquals("", processEntry.getSettings("Entry"));
		assertEquals("", processEntry.getSettings("Extended"));
	}

	@Test
	public void test3() {

		processEntry.setActiveProfile("Test");
		processEntry.setSettings("Hello World");
		processEntry.setActiveProfile("My");
		processEntry.copySettings("Test");
		processEntry.setActiveProfile("Entry");
		processEntry.copySettings("Test");

		processEntry.setActiveProfile(ProcessEntryContainer.DEFAULT_PROFILE);
		assertEquals("", processEntry.getSettings());
		assertEquals("Hello World", processEntry.getSettings("Test"));
		assertEquals("Hello World", processEntry.getSettings("My"));
		assertEquals("Hello World", processEntry.getSettings("Entry"));
	}
}
