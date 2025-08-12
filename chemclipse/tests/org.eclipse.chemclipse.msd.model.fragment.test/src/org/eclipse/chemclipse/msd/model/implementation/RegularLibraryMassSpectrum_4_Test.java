/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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
import static org.junit.Assert.assertNull;

import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class RegularLibraryMassSpectrum_4_Test {

	private IRegularLibraryMassSpectrum massSpectrum;

	@Before
	public void setUp() throws Exception {

		massSpectrum = new RegularLibraryMassSpectrum();
		massSpectrum.setPrecursorIon(127.764d);
		massSpectrum.setNeutralMass(127.764d);
	}

	@Test
	public void test1() {

		assertNull(massSpectrum.getPrecursorType());
	}

	@Test
	public void test2() {

		assertEquals(127.764d, massSpectrum.getPrecursorIon(), 0);
	}

	@Test
	public void test3() {

		assertEquals(127.764d, massSpectrum.getNeutralMass(), 0);
	}

	@Test
	public void test4() {

		assertEquals(0, massSpectrum.getPropertyKeySet().size());
	}

	@Test
	public void test5() {

		assertEquals("", massSpectrum.getProperty(IRegularLibraryMassSpectrum.PROPERTY_PRECURSOR_TYPE));
	}

	@Test
	public void test6() {

		assertEquals("", massSpectrum.getProperty(IRegularLibraryMassSpectrum.PROPERTY_COLLISION_ENERGY));
	}

	@Test
	public void test7() {

		assertEquals("", massSpectrum.getProperty(IRegularLibraryMassSpectrum.PROPERTY_INSTRUMENT_NAME));
	}
}
