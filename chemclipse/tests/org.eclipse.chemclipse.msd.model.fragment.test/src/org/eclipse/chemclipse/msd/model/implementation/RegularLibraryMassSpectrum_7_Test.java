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

import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class RegularLibraryMassSpectrum_7_Test {

	private IRegularLibraryMassSpectrum massSpectrum;

	@BeforeAll
	public void setUp() {

		massSpectrum = new RegularLibraryMassSpectrum();
		massSpectrum.setPrecursorType("[M+Na]+");
		massSpectrum.setPrecursorIon(728.3822d);
		massSpectrum.setNeutralMass(705.39243072d);
		massSpectrum.putProperty(IRegularLibraryMassSpectrum.PROPERTY_COLLISION_ENERGY, "30");
		massSpectrum.putProperty(IRegularLibraryMassSpectrum.PROPERTY_INSTRUMENT_NAME, "Agilent QTOF 6530");
	}

	@Test
	public void test2() {

		assertEquals(728.3822d, massSpectrum.getPrecursorIon(), 0);
	}

	@Test
	public void test3() {

		assertEquals(705.39243072d, massSpectrum.getNeutralMass(), 0);
	}

	@Test
	public void test4() {

		assertEquals(Polarity.POSITIVE, massSpectrum.getPolarity());
	}

	@Test
	public void test5() {

		assertEquals(2, massSpectrum.getPropertyKeySet().size());
	}

	@Test
	public void test6() {

		assertEquals("[M+Na]+", massSpectrum.getPrecursorType());
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