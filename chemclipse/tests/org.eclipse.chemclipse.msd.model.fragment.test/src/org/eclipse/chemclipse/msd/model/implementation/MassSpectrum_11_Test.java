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

import org.junit.Before;
import org.junit.Test;

/**
 * Tests if all ions will be removed by the method
 * removeAllMassFragements.
 */
public class MassSpectrum_11_Test {

	private ScanMSD massSpectrum;
	private Ion ion;

	@Before
	public void setUp() {

		massSpectrum = new ScanMSD();
		ion = new Ion(45.5f, 78500.2f);
		massSpectrum.addIon(ion);
		ion = new Ion(85.4f, 3000.5f);
		massSpectrum.addIon(ion);
		ion = new Ion(104.1f, 120000.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(32.6f, 890520.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(105.7f, 120000.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(28.2f, 33000.5f);
		massSpectrum.addIon(ion);
		ion = new Ion(85.4f, 3000.5f);
		massSpectrum.removeIon(ion);
	}

	@Test
	public void testRemoveAllIons_1() {

		assertEquals("getIons", 5, massSpectrum.getIons().size());
		massSpectrum.removeAllIons();
		assertEquals("getIons", 0, massSpectrum.getIons().size());
	}
}
