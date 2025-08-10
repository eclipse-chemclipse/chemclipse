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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class RegularLibraryMassSpectrum_3_Test {

	private IRegularLibraryMassSpectrum massSpectrum;
	private ILibraryInformation libraryInformation;

	@Before
	public void setUp() {

		massSpectrum = new RegularLibraryMassSpectrum();
		libraryInformation = new LibraryInformation();
		libraryInformation.setCasNumber("01-33-XX");
		libraryInformation.setComments("test substance comment");
		libraryInformation.setName("Test Substance");
		massSpectrum.setLibraryInformation(null);
	}

	@Test
	public void testGetName_1() {

		assertEquals("Name", "", massSpectrum.getLibraryInformation().getName());
	}

	@Test
	public void testGetComments_1() {

		assertEquals("Comments", "", massSpectrum.getLibraryInformation().getComments());
	}

	@Test
	public void testGetCasNumber_1() {

		assertEquals("CAS Number", "", massSpectrum.getLibraryInformation().getCasNumber());
	}
}
