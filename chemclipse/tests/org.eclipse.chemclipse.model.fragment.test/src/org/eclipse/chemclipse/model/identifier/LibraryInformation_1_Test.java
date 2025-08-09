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
package org.eclipse.chemclipse.model.identifier;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LibraryInformation_1_Test {

	private ILibraryInformation libraryInformation = new LibraryInformation();

	@Test
	public void testGetName_1() {

		assertEquals("Name", "", libraryInformation.getName());
	}

	@Test
	public void testGetName_2() {

		libraryInformation.setName("Philip");
		assertEquals("Name", "Philip", libraryInformation.getName());
	}

	@Test
	public void testGetName_3() {

		libraryInformation.setName(null);
		assertEquals("Name", "", libraryInformation.getName());
	}

	@Test
	public void testGetComments_1() {

		assertEquals("Comments", "", libraryInformation.getComments());
	}

	@Test
	public void testGetComments_2() {

		libraryInformation.setComments("Here are the test comments");
		assertEquals("Comments", "Here are the test comments", libraryInformation.getComments());
	}

	@Test
	public void testGetComments_3() {

		libraryInformation.setComments(null);
		assertEquals("Comments", "", libraryInformation.getComments());
	}

	@Test
	public void testGetCasNumber_1() {

		assertEquals("CAS Number", "", libraryInformation.getCasNumber());
	}

	@Test
	public void testGetCasNumber_2() {

		libraryInformation.setCasNumber("56-38-XX");
		assertEquals("CAS Number", "56-38-XX", libraryInformation.getCasNumber());
	}

	@Test
	public void testGetCasNumber_3() {

		libraryInformation.setCasNumber(null);
		assertEquals("CAS Number", "", libraryInformation.getCasNumber());
	}
}
