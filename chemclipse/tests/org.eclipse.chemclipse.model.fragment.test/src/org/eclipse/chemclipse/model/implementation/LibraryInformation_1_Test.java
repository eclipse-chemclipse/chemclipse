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
package org.eclipse.chemclipse.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.junit.Test;

public class LibraryInformation_1_Test {

	private ILibraryInformation libraryInformation = new LibraryInformation();

	@Test
	public void test1() {

		assertEquals("", libraryInformation.getReferenceIdentifier());
	}

	@Test
	public void test2() {

		libraryInformation.setReferenceIdentifier(null);
		assertEquals("", libraryInformation.getReferenceIdentifier());
	}

	@Test
	public void test3() {

		libraryInformation.setReferenceIdentifier("ChemClipse");
		assertEquals("ChemClipse", libraryInformation.getReferenceIdentifier());
	}
}
