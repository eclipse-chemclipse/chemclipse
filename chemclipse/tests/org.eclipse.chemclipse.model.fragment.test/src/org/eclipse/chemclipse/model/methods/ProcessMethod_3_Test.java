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
import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.chemclipse.processing.methods.ProcessMethod;
import org.junit.Before;
import org.junit.Test;

public class ProcessMethod_3_Test {

	private IProcessMethod processMethod;

	@Before
	public void setUp() throws Exception {

		Set<DataCategory> dataCategories = new HashSet<>();
		dataCategories.add(DataCategory.MSD);
		processMethod = new ProcessMethod(dataCategories);
	}

	@Test
	public void test1() {

		String profile = "Test";
		processMethod.setActiveProfile(profile);
		assertEquals(profile, processMethod.getActiveProfile());
		assertEquals(2, processMethod.getProfiles().size());

		IProcessMethod processMethod2 = new ProcessMethod(processMethod);
		assertEquals(profile, processMethod2.getActiveProfile());
		assertEquals(2, processMethod2.getProfiles().size());
	}
}
