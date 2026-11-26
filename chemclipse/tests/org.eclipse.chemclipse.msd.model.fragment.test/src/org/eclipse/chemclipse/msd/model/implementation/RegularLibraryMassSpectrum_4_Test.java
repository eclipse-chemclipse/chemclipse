/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class RegularLibraryMassSpectrum_4_Test {

	private IRegularLibraryMassSpectrum massSpectrum;

	@BeforeAll
	public void setUp() {

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

		assertNull(massSpectrum.getPrecursorType());
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
