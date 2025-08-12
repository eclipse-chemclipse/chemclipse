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

import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class PeakMassSpectrum_4_Test {

	private IPeakMassSpectrum peakMassSpectrum;

	@Before
	public void setUp() {

		peakMassSpectrum = new PeakMassSpectrum();
		for(int i = 0; i < 5; i++) {
			peakMassSpectrum.addIon(new Ion(i, 100));
		}
	}

	@Test
	public void testGetNumberOfIons_1() {

		assertEquals(5, peakMassSpectrum.getNumberOfIons());
	}
}
