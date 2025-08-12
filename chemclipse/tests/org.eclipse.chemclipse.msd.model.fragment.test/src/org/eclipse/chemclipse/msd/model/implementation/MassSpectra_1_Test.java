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

import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.Before;
import org.junit.Test;

public class MassSpectra_1_Test {

	private IMassSpectra massSpectra;
	private ScanMSD massSpectrum1;
	private ScanMSD massSpectrum2;
	private IScanMSD massSpectrum;

	@Before
	public void setUp() {

		massSpectra = new MassSpectra();
		massSpectrum1 = new ScanMSD();
		massSpectrum1.addIon(new Ion(45.4f, 730.4f));
		massSpectrum1.addIon(new Ion(76.4f, 7830.4f));
		massSpectrum1.addIon(new Ion(48.7f, 57330.4f));
		massSpectrum2 = new ScanMSD();
		massSpectrum2.addIon(new Ion(43.4f, 723830.4f));
		massSpectrum2.addIon(new Ion(78.4f, 3430.4f));
		massSpectrum2.addIon(new Ion(23.3f, 530.4f));
	}

	@Test
	public void testMassSpectra_1() {

		assertEquals("Size", 0, massSpectra.size());
	}

	@Test
	public void testMassSpectra_2() {

		massSpectra.addMassSpectrum(massSpectrum1);
		massSpectra.addMassSpectrum(massSpectrum2);
		assertEquals("Size", 2, massSpectra.size());
	}

	@Test
	public void testMassSpectra_3() {

		massSpectra.addMassSpectrum(massSpectrum1);
		massSpectra.addMassSpectrum(massSpectrum2);
		massSpectra.removeMassSpectrum(massSpectrum1);
		assertEquals("Size", 1, massSpectra.size());
	}

	@Test
	public void testMassSpectra_4() {

		massSpectra.addMassSpectrum(massSpectrum1);
		massSpectra.addMassSpectrum(massSpectrum2);
		massSpectra.removeMassSpectrum(massSpectrum1);
		massSpectrum = massSpectra.getMassSpectrum(1);
		assertEquals("TotalSignal", 727791.1f, massSpectrum.getTotalSignal(), 0);
	}

	public void testMassSpectra_5() {

		massSpectra.addMassSpectrum(massSpectrum1);
		massSpectra.addMassSpectrum(massSpectrum2);
		massSpectrum = massSpectra.getMassSpectrum(1);
		assertEquals("TotalSignal", 65891.195f, massSpectrum.getTotalSignal(), 0);
		massSpectrum = massSpectra.getMassSpectrum(2);
		assertEquals("TotalSignal", 727791.1f, massSpectrum.getTotalSignal(), 0);
	}

	public void testMassSpectra_6() {

		massSpectra.addMassSpectrum(massSpectrum1);
		massSpectra.addMassSpectrum(massSpectrum2);
		massSpectra.removeMassSpectrum(massSpectrum2);
		massSpectra.removeMassSpectrum(massSpectrum1);
		assertEquals("Size", 0, massSpectra.size());
	}

	public void testMassSpectra_7() {

		massSpectra.addMassSpectrum(null);
		assertEquals("Size", 0, massSpectra.size());
	}
}
