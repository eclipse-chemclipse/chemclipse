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

public class ProcessMethod_2_Test {

	private ProcessMethod processMethod;
	private IProcessEntry processEntry;

	@Before
	public void setUp() throws Exception {

		Set<DataCategory> dataCategories = new HashSet<>();
		dataCategories.add(DataCategory.MSD);
		processMethod = new ProcessMethod(dataCategories);
		processEntry = new ProcessEntry(processMethod);
		processMethod.getEntries().add(processEntry);
	}

	@Test
	public void test1() {

		String profile = "Test";
		processMethod.setActiveProfile(profile);
		assertEquals(profile, processMethod.getActiveProfile());
		assertEquals(profile, processEntry.getActiveProfile());
		assertEquals(2, processMethod.getProfiles().size());
	}

	@Test
	public void test2() {

		String profile = "Test";
		processMethod.setActiveProfile(profile);
		assertEquals(profile, processMethod.getActiveProfile());
		assertEquals(profile, processEntry.getActiveProfile());
		assertEquals(2, processMethod.getProfiles().size());

		processMethod.deleteProfile(profile);
		assertEquals(ProcessEntryContainer.DEFAULT_PROFILE, processMethod.getActiveProfile());
		assertEquals(ProcessEntryContainer.DEFAULT_PROFILE, processEntry.getActiveProfile());
		assertEquals(1, processMethod.getProfiles().size());
	}
}
