/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.junit.Test;

public class LibraryInformation_2_Test {

	private ILibraryInformation libraryInformation = new LibraryInformation();

	@Test
	public void test1() {

		assertEquals(0, libraryInformation.getRetentionTime());
	}

	@Test
	public void test2() {

		assertEquals(0.0f, libraryInformation.getRetentionIndex(), 0);
	}

	@Test
	public void test3() {

		libraryInformation.setRetentionTime(1000);
		assertEquals(1000, libraryInformation.getRetentionTime());
	}

	@Test
	public void test4() {

		libraryInformation.setRetentionIndex(1293.5f);
		assertEquals(1293.5f, libraryInformation.getRetentionIndex(), 0);
	}
}
