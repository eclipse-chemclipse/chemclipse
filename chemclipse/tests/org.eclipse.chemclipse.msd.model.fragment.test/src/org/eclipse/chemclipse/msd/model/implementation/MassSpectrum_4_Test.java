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
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonBounds;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.Before;
import org.junit.Test;

public class MassSpectrum_4_Test {

	private IChromatogramMSD chromatogram;
	private IScanMSD massSpectrum;
	private IIon ion;

	@Before
	public void setUp() {

		chromatogram = new ChromatogramMSD();
		massSpectrum = new ScanMSD();
		ion = new Ion(45.5f, 3000.5f);
		massSpectrum.addIon(ion);
		chromatogram.addScan(massSpectrum);
	}

	@Test
	public void testGetIons_1() {

		assertEquals("getIons", 1, massSpectrum.getIons().size());
	}

	@Test
	public void testGetParentChromatogram_1() {

		assertEquals("getParentChromatogram", chromatogram, massSpectrum.getParentChromatogram());
	}

	@Test
	public void getTotalSignal_1() {

		assertEquals("getTotalSignal", 3000.5f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void getExtractedIonSignal_1() {

		assertEquals("getExtractedIonSignal", 0, massSpectrum.getExtractedIonSignal().getAbundance(0), 0);
	}

	@Test
	public void getExtractedIonSignal_2() {

		assertEquals("getExtractedIonSignal", 3000.5f, massSpectrum.getExtractedIonSignal(1, 50).getAbundance(46), 0);
	}

	@Test
	public void testGetBasePeak_1() {

		assertEquals("getBasePeak", 45.5d, massSpectrum.getBasePeak(), 0);
	}

	@Test
	public void testGetBasePeakAbundance_1() {

		assertEquals("getBasePeakAbundance", 3000.5f, massSpectrum.getBasePeakAbundance(), 0);
	}

	@Test
	public void testGetHighestAbundance_1() {

		assertEquals("getHighestAbundance", 3000.5f, massSpectrum.getHighestAbundance().getAbundance(), 0);
	}

	@Test
	public void testGetHighestAbundance_2() {

		assertEquals("getHighestAbundance", 45.5d, massSpectrum.getHighestAbundance().getIon(), 0);
	}

	@Test
	public void testGetHighestIon_1() {

		assertEquals("getHighestIon", 3000.5f, massSpectrum.getHighestIon().getAbundance(), 0);
	}

	@Test
	public void testGetHighestIon_2() {

		assertEquals("getHighestIon", 45.5d, massSpectrum.getHighestIon().getIon(), 0);
	}

	@Test
	public void testGetLowestAbundance_1() {

		assertEquals("getLowestAbundance", 3000.5f, massSpectrum.getLowestAbundance().getAbundance(), 0);
	}

	@Test
	public void testGetLowestAbundance_2() {

		assertEquals("getLowestAbundance", 45.5d, massSpectrum.getLowestAbundance().getIon(), 0);
	}

	@Test
	public void testGetLowestIon_1() {

		assertEquals("getLowestIon", 3000.5f, massSpectrum.getLowestIon().getAbundance(), 0);
	}

	@Test
	public void testGetLowestIon_2() {

		assertEquals("getLowestIon", 45.5d, massSpectrum.getLowestIon().getIon(), 0);
	}

	@Test
	public void testGetIonBounds_1() {

		IIonBounds bounds = massSpectrum.getIonBounds();
		assertEquals("getIonBounds", 3000.5f, bounds.getLowestIon().getAbundance(), 0);
		assertEquals("getIonBounds", 45.5d, bounds.getLowestIon().getIon(), 0);
		assertEquals("getIonBounds", 3000.5f, bounds.getHighestIon().getAbundance(), 0);
		assertEquals("getIonBounds", 45.5d, bounds.getHighestIon().getIon(), 0);
	}

	@Test
	public void testGetNumberOfIons_1() {

		assertEquals("getNumberOfIons", 1, massSpectrum.getNumberOfIons());
	}

	@Test
	public void testGetIon_1() {

		IIon ion = massSpectrum.getIon(5);
		assertEquals("getIon", null, ion);
		ion = massSpectrum.getIon(46);
		assertTrue("getIon", ion != null);
		assertEquals("getIon(46) abundance", 3000.5f, ion.getAbundance(), 0);
		assertEquals("getIon(46) ion", 46.0d, ion.getIon(), 0);
	}

	@Test
	public void testIsDirty_1() {

		assertEquals("isDirty", true, massSpectrum.isDirty());
	}
}
