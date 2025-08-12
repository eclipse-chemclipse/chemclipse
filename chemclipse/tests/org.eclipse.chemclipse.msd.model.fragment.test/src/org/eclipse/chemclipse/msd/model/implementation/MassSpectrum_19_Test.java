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

public class MassSpectrum_19_Test {

	private ScanMSD massSpectrum;
	private Ion ion;

	@Before
	public void setUp() {

		massSpectrum = new ScanMSD();
		ion = new Ion(45.5f, 78500.2f);
		massSpectrum.addIon(ion);
		ion = new Ion(104.1f, 120000.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(32.6f, 890520.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(105.7f, 120000.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(28.2f, 33000.5f);
		massSpectrum.addIon(ion);
	}

	@Test
	public void testGetTotalSignal_1() {

		massSpectrum.adjustIons(-1.1f);
		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_2() {

		massSpectrum.adjustIons(1.1f);
		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_3() {

		massSpectrum.adjustIons(0.0f);
		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_4() {

		// adjust +20%
		massSpectrum.adjustIons(0.2f);
		assertEquals("getTotalSignal", 1490426.4f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_5() {

		// adjust -20%
		massSpectrum.adjustIons(-0.2f);
		assertEquals("getTotalSignal", 993617.5f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_6() {

		// adjust -100%
		massSpectrum.adjustIons(-1.0f);
		assertEquals("getTotalSignal", 0.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_7() {

		// adjust +100%
		massSpectrum.adjustIons(1.0f);
		assertEquals("getTotalSignal", 2484043.8f, massSpectrum.getTotalSignal(), 0);
	}
}
