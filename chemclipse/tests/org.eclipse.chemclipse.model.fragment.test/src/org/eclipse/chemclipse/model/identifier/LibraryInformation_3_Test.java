/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.identifier;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LibraryInformation_3_Test {

	private ILibraryInformation libraryInformation = new LibraryInformation();

	@Test
	public void test_1() {

		assertEquals("", libraryInformation.getDatabase());
	}

	@Test
	public void test_2() {

		libraryInformation.setDatabase(null);
		assertEquals("", libraryInformation.getDatabase());
	}

	@Test
	public void test_3() {

		libraryInformation.setDatabase("ChemClipse");
		assertEquals("ChemClipse", libraryInformation.getDatabase());
	}
}
