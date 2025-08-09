/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
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

public class LibraryInformation_5_Test {

	private ILibraryInformation libraryInformation = new LibraryInformation();

	@Test
	public void test_1() {

		assertEquals("", libraryInformation.getCasNumber());
	}

	@Test
	public void test_2() {

		assertEquals(0, libraryInformation.getCasNumbers().size());
	}

	@Test
	public void test_3() {

		libraryInformation.setCasNumber("100–52-7");
		assertEquals("100–52-7", libraryInformation.getCasNumber());
	}

	@Test
	public void test_4() {

		libraryInformation.addCasNumber("100–52-7");
		assertEquals("100–52-7", libraryInformation.getCasNumber());
	}

	@Test
	public void test_5() {

		libraryInformation.setCasNumber("100–52-7");
		libraryInformation.addCasNumber("103-36-6");
		libraryInformation.addCasNumber("103-45-7");
		assertEquals("100–52-7", libraryInformation.getCasNumber());
	}

	@Test
	public void test_6() {

		libraryInformation.setCasNumber("100–52-7");
		libraryInformation.addCasNumber("103-36-6");
		libraryInformation.addCasNumber("103-45-7");
		libraryInformation.deleteCasNumber("100–52-7");
		assertEquals("103-36-6", libraryInformation.getCasNumber());
	}

	@Test
	public void test_7() {

		libraryInformation.addCasNumber("103-36-6");
		libraryInformation.addCasNumber("103-45-7");
		libraryInformation.setCasNumber("100–52-7");
		assertEquals("100–52-7", libraryInformation.getCasNumber());
	}

	@Test
	public void test_8() {

		libraryInformation.addCasNumber(null);
		libraryInformation.addCasNumber("100–52-7");
		libraryInformation.addCasNumber("103-36-6");
		libraryInformation.addCasNumber("103-36-6"); // duplicate
		libraryInformation.addCasNumber("103-45-7");
		assertEquals(3, libraryInformation.getCasNumbers().size());
		libraryInformation.deleteCasNumber(null);
		assertEquals(3, libraryInformation.getCasNumbers().size());
		libraryInformation.deleteCasNumber("100–52-7");
		assertEquals(2, libraryInformation.getCasNumbers().size());
		libraryInformation.clearCasNumbers();
		assertEquals(0, libraryInformation.getCasNumbers().size());
		assertEquals("", libraryInformation.getCasNumber());
	}
}