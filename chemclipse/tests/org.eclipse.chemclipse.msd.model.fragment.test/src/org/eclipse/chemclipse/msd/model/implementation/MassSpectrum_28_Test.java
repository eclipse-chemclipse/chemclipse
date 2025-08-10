/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.Test;

public class MassSpectrum_28_Test {

	private IScanMSD massSpectrum = new ScanMSD();

	@Test
	public void test1() {

		assertFalse(massSpectrum.isMeasurementSIM());
	}

	@Test
	public void test2() {

		addIons(massSpectrum, 1);
		assertTrue(massSpectrum.isMeasurementSIM());
	}

	@Test
	public void test3() {

		addIons(massSpectrum, 10);
		assertTrue(massSpectrum.isMeasurementSIM());
	}

	@Test
	public void test4() {

		addIons(massSpectrum, 11);
		assertFalse(massSpectrum.isMeasurementSIM());
	}

	private void addIons(IScanMSD massSpectrum, int amount) {

		for(int i = 1; i <= amount; i++) {
			IIon nominalIon = new Ion(i, 1000);
			massSpectrum.addIon(nominalIon, false);
		}
	}
}
