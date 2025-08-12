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
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.junit.Before;
import org.junit.Test;

public class RegularLibraryMassSpectrum_8_Test {

	private IRegularLibraryMassSpectrum massSpectrum;

	@Before
	public void setUp() {

		massSpectrum = new RegularLibraryMassSpectrum();
		massSpectrum.putProperty(IRegularLibraryMassSpectrum.PROPERTY_PRECURSOR_TYPE, "[M-H2O+H]+");
		massSpectrum.setPrecursorIon(365.1589d);
		massSpectrum.setNeutralMass(382.16624d);
		massSpectrum.putProperty(IRegularLibraryMassSpectrum.PROPERTY_COLLISION_ENERGY, "30");
		massSpectrum.putProperty(IRegularLibraryMassSpectrum.PROPERTY_INSTRUMENT_NAME, "Agilent QTOF 6530");
	}

	@Test
	public void test1() {

		assertNull(massSpectrum.getPrecursorType());
	}

	@Test
	public void test2() {

		assertEquals(365.1589d, massSpectrum.getPrecursorIon(), 0);
	}

	@Test
	public void test3() {

		assertEquals(382.16624d, massSpectrum.getNeutralMass(), 0);
	}

	@Test
	public void test4() {

		assertEquals(Polarity.POSITIVE, massSpectrum.getPolarity());
	}

	@Test
	public void test5() {

		assertEquals(3, massSpectrum.getPropertyKeySet().size());
	}

	@Test
	public void test6() {

		assertEquals("[M-H2O+H]+", massSpectrum.getProperty(IRegularLibraryMassSpectrum.PROPERTY_PRECURSOR_TYPE));
	}

	@Test
	public void test7() {

		assertEquals("30", massSpectrum.getProperty(IRegularLibraryMassSpectrum.PROPERTY_COLLISION_ENERGY));
	}

	@Test
	public void test8() {

		assertEquals("Agilent QTOF 6530", massSpectrum.getProperty(IRegularLibraryMassSpectrum.PROPERTY_INSTRUMENT_NAME));
	}
}