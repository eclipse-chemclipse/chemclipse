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
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.chemclipse.processing.methods.ProcessEntryContainer;
import org.eclipse.chemclipse.processing.methods.ProcessMethod;
import org.junit.Before;
import org.junit.Test;

public class ProcessMethod_1_Test {

	private IProcessMethod processMethod;

	@Before
	public void setUp() throws Exception {

		Set<DataCategory> dataCategories = new HashSet<>();
		dataCategories.add(DataCategory.MSD);
		processMethod = new ProcessMethod(dataCategories);
	}

	@Test
	public void test1() {

		assertEquals(ProcessEntryContainer.DEFAULT_PROFILE, processMethod.getActiveProfile());
		Set<String> profiles = processMethod.getProfiles();
		assertEquals(1, profiles.size());
		assertTrue(profiles.contains(ProcessEntryContainer.DEFAULT_PROFILE));
	}

	@Test
	public void test2() {

		assertEquals(ProcessEntryContainer.DEFAULT_PROFILE, processMethod.getActiveProfile());
		processMethod.addProfile("Test");
		assertEquals(ProcessEntryContainer.DEFAULT_PROFILE, processMethod.getActiveProfile());
		Set<String> profiles = processMethod.getProfiles();
		assertEquals(2, profiles.size());
		assertTrue(profiles.contains(ProcessEntryContainer.DEFAULT_PROFILE));
		assertTrue(profiles.contains("Test"));
	}

	@Test
	public void test3() {

		processMethod.setActiveProfile("Test");
		assertEquals("Test", processMethod.getActiveProfile());
		Set<String> profiles = processMethod.getProfiles();
		assertEquals(2, profiles.size());
		assertTrue(profiles.contains(ProcessEntryContainer.DEFAULT_PROFILE));
		assertTrue(profiles.contains("Test"));
	}

	@Test
	public void test4() {

		processMethod.setActiveProfile(null);
		assertEquals(ProcessEntryContainer.DEFAULT_PROFILE, processMethod.getActiveProfile());
		Set<String> profiles = processMethod.getProfiles();
		assertEquals(1, profiles.size());
		assertTrue(profiles.contains(ProcessEntryContainer.DEFAULT_PROFILE));
	}

	@Test
	public void test5() {

		processMethod.setActiveProfile("Test");
		assertEquals("Test", processMethod.getActiveProfile());
		Set<String> profiles = processMethod.getProfiles();
		assertEquals(2, profiles.size());
		assertTrue(profiles.contains(ProcessEntryContainer.DEFAULT_PROFILE));
		assertTrue(profiles.contains("Test"));
		processMethod.deleteProfile("Test");
		assertEquals(1, processMethod.getProfiles().size());
		assertEquals(ProcessEntryContainer.DEFAULT_PROFILE, processMethod.getActiveProfile());
	}
}
