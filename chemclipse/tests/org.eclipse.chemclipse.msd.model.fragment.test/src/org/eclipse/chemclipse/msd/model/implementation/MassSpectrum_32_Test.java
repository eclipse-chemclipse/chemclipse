/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.chemclipse.msd.model.core.IIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class MassSpectrum_32_Test {

	private IScanMSD massSpectrum;

	@BeforeAll
	public void setUp() {

		massSpectrum = new ScanMSD();
		massSpectrum.addIon(new Ion(45.5d, 78500.2f));
		massSpectrum.addIon(new Ion(85.4d, 3000.5f));
		massSpectrum.addIon(new Ion(104.1d, 120000.4f));
		IIonTransition ionTransition = new IonTransition(53.2d, 32, 10.0, 1.2d, 1.2d, 1);
		IIonMSn ion = new IonMSn(32.6d, 890520.4f, ionTransition);
		massSpectrum.addIon(ion);
		massSpectrum.addIon(new Ion(105.7d, 120000.4f));
		massSpectrum.addIon(new Ion(28.2d, 33000.5f));
	}

	@Test
	public void test_1() {

		assertTrue(massSpectrum.isTandemMS());
	}

	@Test
	public void test_2() {

		assertFalse(massSpectrum.isHighResolutionMS());
	}
}
