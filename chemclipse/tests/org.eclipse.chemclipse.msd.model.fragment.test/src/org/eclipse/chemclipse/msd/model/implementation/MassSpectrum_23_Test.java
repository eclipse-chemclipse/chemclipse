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
 * Tests adjustTotalSignal(float totalSignal).
 */
public class MassSpectrum_23_Test {

	private ScanMSD massSpectrum;
	private Ion ion;

	@Before
	public void setUp() throws Exception {

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

		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_2() {

		/*
		 * There could be small calculation differences in the given and the
		 * result total signal.
		 */
		massSpectrum.adjustTotalSignal(1000000.0f);
		assertEquals("getTotalSignal", 1000000.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_3() {

		/*
		 * There could be small calculation differences in the given and the
		 * result total signal.
		 */
		massSpectrum.adjustTotalSignal(0.0f);
		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_4() {

		/*
		 * There could be small calculation differences in the given and the
		 * result total signal.
		 */
		massSpectrum.adjustTotalSignal(Float.NaN);
		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_5() {

		/*
		 * There could be small calculation differences in the given and the
		 * result total signal.
		 */
		massSpectrum.adjustTotalSignal(Float.POSITIVE_INFINITY);
		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_6() {

		/*
		 * There could be small calculation differences in the given and the
		 * result total signal.
		 */
		massSpectrum.adjustTotalSignal(10.0f);
		assertEquals("getTotalSignal", 9.999999f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_7() {

		/*
		 * There could be small calculation differences in the given and the
		 * result total signal.
		 */
		massSpectrum.adjustTotalSignal(5000000.0f);
		assertEquals("getTotalSignal", 5000000.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalSignal_8() {

		/*
		 * There could be small calculation differences in the given and the
		 * result total signal.
		 */
		massSpectrum.adjustTotalSignal(-1.0f);
		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(), 0);
	}
}
