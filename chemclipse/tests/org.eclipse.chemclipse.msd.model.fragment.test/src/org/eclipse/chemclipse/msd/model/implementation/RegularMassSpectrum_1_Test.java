/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.junit.Test;

public class RegularMassSpectrum_1_Test {

	private IRegularMassSpectrum massSpectrum = new RegularMassSpectrum();

	@Test
	public void test_1() {

		assertEquals(1, massSpectrum.getMassSpectrometer());
	}

	@Test
	public void test_2() {

		assertEquals(MassSpectrumType.CENTROID, massSpectrum.getMassSpectrumType());
	}

	@Test
	public void test_3() {

		assertEquals("Centroid", massSpectrum.getMassSpectrumType().label());
	}

	@Test
	public void test_4() {

		assertEquals(1, massSpectrum.getTimeSegmentId());
	}

	@Test
	public void test_5() {

		assertEquals(1, massSpectrum.getCycleNumber());
	}

	@Test
	public void test6() {

		assertEquals(Polarity.NONE, massSpectrum.getPolarity());
	}
}
