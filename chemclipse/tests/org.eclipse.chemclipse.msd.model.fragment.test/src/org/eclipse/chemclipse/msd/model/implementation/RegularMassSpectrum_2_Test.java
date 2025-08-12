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
import org.junit.Test;

public class RegularMassSpectrum_2_Test {

	private IRegularMassSpectrum massSpectrum = new RegularMassSpectrum();

	@Test
	public void test_1() {

		massSpectrum.setMassSpectrometer((short)1);
		assertEquals(1, massSpectrum.getMassSpectrometer());
	}

	@Test
	public void test_2() {

		massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
		assertEquals(MassSpectrumType.PROFILE, massSpectrum.getMassSpectrumType());
	}

	@Test
	public void test_3() {

		massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
		assertEquals("Profile", massSpectrum.getMassSpectrumType().label());
	}

	@Test
	public void test_4() {

		massSpectrum.setTimeSegmentId(2);
		assertEquals(2, massSpectrum.getTimeSegmentId());
	}

	@Test
	public void test_5() {

		massSpectrum.setCycleNumber(2);
		assertEquals(2, massSpectrum.getCycleNumber());
	}
}
