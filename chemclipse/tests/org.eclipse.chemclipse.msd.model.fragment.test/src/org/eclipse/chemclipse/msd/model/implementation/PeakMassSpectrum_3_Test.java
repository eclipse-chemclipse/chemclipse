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

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class PeakMassSpectrum_3_Test {

	private IIon ion;
	private IPeakMassSpectrum peakMassSpectrum;
	private IPeakMassSpectrum peakMassSpectrumShifted;

	@Before
	public void setUp() {

		peakMassSpectrum = new PeakMassSpectrum();
		ion = new Ion(45.5f, 64830.4f);
		peakMassSpectrum.addIon(ion);
		ion = new Ion(78.5f, 4440.4f);
		peakMassSpectrum.addIon(ion);
		ion = new Ion(104.5f, 6444830.4f);
		peakMassSpectrum.addIon(ion);
		ion = new Ion(14.5f, 40.4f);
		peakMassSpectrum.addIon(ion);
		peakMassSpectrumShifted = new PeakMassSpectrum(peakMassSpectrum, 50);
	}

	@Test
	public void testGetTotalSignal_1() {

		assertEquals("TotalSignal", 3257071.0f, peakMassSpectrumShifted.getTotalSignal(), 0);
	}
}
