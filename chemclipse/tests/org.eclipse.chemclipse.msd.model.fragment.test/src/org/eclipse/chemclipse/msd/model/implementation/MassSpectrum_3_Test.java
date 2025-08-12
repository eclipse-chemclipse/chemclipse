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
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.Test;

public class MassSpectrum_3_Test {

	private IScanMSD massSpectrum = new ScanMSD();

	@Test
	public void testGetIons_1() {

		assertEquals("getIons", 0, massSpectrum.getIons().size());
	}

	@Test
	public void testGetParentChromatogram_1() {

		assertEquals("getParentChromatogram", null, massSpectrum.getParentChromatogram());
	}

	@Test
	public void getTotalSignal_1() {

		assertEquals("getTotalSignal", 0, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void getExtractedIonSignal_1() {

		assertEquals("getExtractedIonSignal", 0, massSpectrum.getExtractedIonSignal().getAbundance(0), 0);
	}

	@Test
	public void getExtractedIonSignal_2() {

		assertEquals("getExtractedIonSignal", 0, massSpectrum.getExtractedIonSignal(1, 50).getAbundance(1), 0);
	}

	@Test
	public void testGetBasePeak_1() {

		assertEquals("getBasePeak", 0.0d, massSpectrum.getBasePeak(), 0);
	}

	@Test
	public void testGetBasePeakAbundance_1() {

		assertEquals("getBasePeakAbundance", 0.0f, massSpectrum.getBasePeakAbundance(), 0);
	}

	@Test
	public void testGetHighestAbundance_1a() {

		assertEquals("getHighestAbundance", 0.0d, massSpectrum.getHighestAbundance().getIon(), 0);
	}

	@Test
	public void testGetHighestAbundance_1b() {

		assertEquals("getHighestAbundance", 0.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
	}

	@Test
	public void testGetHighestIon_1a() {

		assertEquals("getHighestIon", 0.0d, massSpectrum.getHighestIon().getIon(), 0);
	}

	@Test
	public void testGetHighestIon_1b() {

		assertEquals("getHighestIon", 0.0f, massSpectrum.getHighestIon().getAbundance(), 0);
	}

	@Test
	public void testGetLowestAbundance_1a() {

		assertEquals("getLowestAbundance", 0.0d, massSpectrum.getLowestAbundance().getIon(), 0);
	}

	@Test
	public void testGetLowestAbundance_1b() {

		assertEquals("getLowestAbundance", 0.0f, massSpectrum.getLowestAbundance().getAbundance(), 0);
	}

	@Test
	public void testGetLowestIon_1a() {

		assertEquals("getLowestIon", 0.0d, massSpectrum.getLowestIon().getIon(), 0);
	}

	@Test
	public void testGetLowestIon_1b() {

		assertEquals("getLowestIon", 0.0f, massSpectrum.getLowestIon().getAbundance(), 0);
	}

	@Test
	public void testGetIonBounds_1() {

		assertEquals("getIonBounds", null, massSpectrum.getIonBounds());
	}

	@Test
	public void testGetNumberOfIons_1() {

		assertEquals("getNumberOfIons", 0, massSpectrum.getNumberOfIons());
	}

	@Test
	public void testGetIon_1() {

		IIon ion;
		ion = massSpectrum.getIon(5);
		assertEquals("getIon", null, ion);
	}

	@Test
	public void testIsDirty_1() {

		assertEquals("isDirty", false, massSpectrum.isDirty());
	}
}
