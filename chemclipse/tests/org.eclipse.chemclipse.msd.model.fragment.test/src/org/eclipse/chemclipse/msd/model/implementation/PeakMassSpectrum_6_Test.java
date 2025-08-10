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
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class PeakMassSpectrum_6_Test {

	private IIon ion;
	private IPeakMassSpectrum peakMassSpectrum;
	private IPeakMassSpectrum peakMassSpectrumShifted;

	@Before
	public void setUp() {

		/*
		 * The total signal of the mass spectrum is 6514141.6f.
		 */
		peakMassSpectrum = new PeakMassSpectrum();
		ion = new Ion(45.5f, 64830.4f);
		peakMassSpectrum.addIon(ion);
		ion = new Ion(78.5f, 4440.4f);
		peakMassSpectrum.addIon(ion);
		ion = new Ion(104.5f, 6444830.4f);
		peakMassSpectrum.addIon(ion);
		ion = new Ion(14.5f, 40.4f);
		peakMassSpectrum.addIon(ion);
	}

	@Test
	public void testGetShiftedMassSpectrum_1() {

		/*
		 * 50% of 6514141.6f = 3257071.0f
		 */
		peakMassSpectrumShifted = new PeakMassSpectrum(peakMassSpectrum, 50);
		assertEquals("TotalSignal", 3257071.0f, peakMassSpectrumShifted.getTotalSignal(), 0);
	}

	@Test
	public void testGetShiftedMassSpectrum_2() {

		/*
		 * 100% of 6514141.6f = 6514141.6f
		 */
		peakMassSpectrumShifted = new PeakMassSpectrum(peakMassSpectrum, 100);
		assertEquals("TotalSignal", 6514142.0f, peakMassSpectrumShifted.getTotalSignal(), 0);
	}

	@Test
	public void testGetShiftedMassSpectrum_3() {

		/*
		 * 101% of 6514141.6f = 3257071.0f
		 */
		assertThrows(IllegalArgumentException.class, () -> peakMassSpectrumShifted = new PeakMassSpectrum(peakMassSpectrum, 101));
	}

	@Test
	public void testGetShiftedMassSpectrum_4() {

		/*
		 * 0% of 6514141.6f = 3257071.0f
		 */
		peakMassSpectrumShifted = new PeakMassSpectrum(peakMassSpectrum, 0);
		assertEquals("TotalSignal", 0.0f, peakMassSpectrumShifted.getTotalSignal(), 0);
	}

	@Test
	public void testGetShiftedMassSpectrum_5() {

		/*
		 * 30% of 6514141.6f = 1954242.48f
		 */
		peakMassSpectrumShifted = new PeakMassSpectrum(peakMassSpectrum, 30);
		assertEquals("TotalSignal", 1954242.48f, peakMassSpectrumShifted.getTotalSignal(), 0);
	}
}
