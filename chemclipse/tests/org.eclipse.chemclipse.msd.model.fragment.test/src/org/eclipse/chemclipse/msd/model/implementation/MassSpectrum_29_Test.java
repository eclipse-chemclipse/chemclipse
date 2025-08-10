/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertFalse;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.Before;
import org.junit.Test;

public class MassSpectrum_29_Test {

	private IScanMSD massSpectrum;

	@Before
	public void setUp() {

		massSpectrum = new ScanMSD();
		massSpectrum.addIon(new Ion(45.5d, 78500.2f));
		massSpectrum.addIon(new Ion(85.4d, 3000.5f));
		massSpectrum.addIon(new Ion(104.1d, 120000.4f));
		massSpectrum.addIon(new Ion(32.6d, 890520.4f));
		massSpectrum.addIon(new Ion(105.7d, 120000.4f));
		massSpectrum.addIon(new Ion(28.2d, 33000.5f));
	}

	@Test
	public void test_1() {

		assertFalse(massSpectrum.isTandemMS());
	}

	@Test
	public void test_2() {

		assertFalse(massSpectrum.isHighResolutionMS());
	}
}
