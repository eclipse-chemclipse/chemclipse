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

import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.junit.Test;

public class RegularLibraryMassSpectrum_1_Test {

	private IRegularLibraryMassSpectrum massSpectrum = new RegularLibraryMassSpectrum();

	@Test
	public void testGetName_1() {

		assertEquals("Name", "", massSpectrum.getLibraryInformation().getName());
	}

	@Test
	public void testGetName_2() {

		massSpectrum.getLibraryInformation().setName("Philip");
		assertEquals("Name", "Philip", massSpectrum.getLibraryInformation().getName());
	}

	@Test
	public void testGetName_3() {

		massSpectrum.getLibraryInformation().setName(null);
		assertEquals("Name", "", massSpectrum.getLibraryInformation().getName());
	}

	@Test
	public void testGetComments_1() {

		assertEquals("Comments", "", massSpectrum.getLibraryInformation().getComments());
	}

	@Test
	public void testGetComments_2() {

		massSpectrum.getLibraryInformation().setComments("Here are the test comments");
		assertEquals("Comments", "Here are the test comments", massSpectrum.getLibraryInformation().getComments());
	}

	@Test
	public void testGetComments_3() {

		massSpectrum.getLibraryInformation().setComments(null);
		assertEquals("Comments", "", massSpectrum.getLibraryInformation().getComments());
	}

	@Test
	public void testGetCasNumber_1() {

		assertEquals("CAS Number", "", massSpectrum.getLibraryInformation().getCasNumber());
	}

	@Test
	public void testGetCasNumber_2() {

		massSpectrum.getLibraryInformation().setCasNumber("56-38-XX");
		assertEquals("CAS Number", "56-38-XX", massSpectrum.getLibraryInformation().getCasNumber());
	}

	@Test
	public void testGetCasNumber_3() {

		massSpectrum.getLibraryInformation().setCasNumber(null);
		assertEquals("CAS Number", "", massSpectrum.getLibraryInformation().getCasNumber());
	}
}
