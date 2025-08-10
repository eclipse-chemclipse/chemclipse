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

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonBounds;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the method normalize() and normalize(float base) of mass spectrum.
 */
public class MassSpectrum_15_Test {

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
		massSpectrum.normalize(1000.0f);
	}

	@Test
	public void testGetNormalizationBase_1() {

		assertEquals("normalizationBase", 1000.0f, massSpectrum.getNormalizationBase(), 0);
	}

	@Test
	public void testIsNormalized_1() {

		assertTrue("isNormalized", massSpectrum.isNormalized());
	}

	@Test
	public void testGetIons_1() {

		assertEquals("normalizationBase", 1000.0f, massSpectrum.getNormalizationBase(), 0);
		assertTrue("isNormalized", massSpectrum.isNormalized());
		assertEquals("getIons", 5, massSpectrum.getIons().size());
	}

	@Test
	public void testGetTotalSignal_1() {

		assertEquals("getTotalSignal", 1394.7147f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetExtractedIonSignal_1() {

		assertEquals("getExtractedIonSignal", 0.0f, massSpectrum.getExtractedIonSignal().getAbundance(0), 0);
	}

	@Test
	public void testGetExtractedIonSignal_2() {

		assertEquals("getExtractedIonSignal", 134.75312f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(104), 0);
	}

	@Test
	public void testGetExtractedIonSignal_3() {

		assertEquals("getExtractedIonSignal", 1000.0f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(33), 0);
	}

	@Test
	public void testGetExtractedIonSignal_4() {

		assertEquals("getExtractedIonSignal", 134.75312f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(106), 0);
	}

	@Test
	public void testGetBasePeak_1() {

		assertEquals("getBasePeak", 32.599998474121094d, massSpectrum.getBasePeak(), 0);
	}

	@Test
	public void testGetBasePeakAbundance_1() {

		assertEquals("getBasePeakAbundance", 1000.0f, massSpectrum.getBasePeakAbundance(), 0);
	}

	@Test
	public void testGetHighestAbundance_1() {

		assertEquals("getHighestAbundance", 1000.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
	}

	@Test
	public void testGetHighestAbundance_2() {

		assertEquals("getHighestAbundance", 32.599998474121094d, massSpectrum.getHighestAbundance().getIon(), 0);
	}

	@Test
	public void testGetHighestIon_1() {

		assertEquals("getHighestIon", 134.75311f, massSpectrum.getHighestIon().getAbundance(), 0);
	}

	@Test
	public void testGetHighestIon_2() {

		assertEquals("getHighestIon", 105.69999694824219d, massSpectrum.getHighestIon().getIon(), 0);
	}

	@Test
	public void testGetLowestAbundance_1() {

		assertEquals("getLowestAbundance", 37.057545f, massSpectrum.getLowestAbundance().getAbundance(), 0);
	}

	@Test
	public void testGetLowestAbundance_2() {

		assertEquals("getLowestAbundance", 28.200000762939453d, massSpectrum.getLowestAbundance().getIon(), 0);
	}

	@Test
	public void testGetLowestIon_1() {

		assertEquals("getLowestIon", 37.057545f, massSpectrum.getLowestIon().getAbundance(), 0);
	}

	@Test
	public void testGetLowestIon_2() {

		assertEquals("getLowestIon", 28.200000762939453d, massSpectrum.getLowestIon().getIon(), 0);
	}

	@Test
	public void testGetIonBounds_1() {

		IIonBounds bounds = massSpectrum.getIonBounds();
		assertEquals("getLowestIon().getAbundance()", 37.057545f, bounds.getLowestIon().getAbundance(), 0);
		assertEquals("getLowestIon().getIon()", 28.200000762939453d, bounds.getLowestIon().getIon(), 0);
		assertEquals("getHighestIon().getAbundance()", 134.75312f, bounds.getHighestIon().getAbundance(), 0);
		assertEquals("getHighestIon().getIon()", 105.69999694824219d, bounds.getHighestIon().getIon(), 0);
	}

	@Test
	public void testGetNumberOfIons_1() {

		assertEquals("getNumberOfIons", 5, massSpectrum.getNumberOfIons());
	}

	@Test
	public void testGetIon_1() {

		IIon ion;
		ion = massSpectrum.getIon(5);
		assertEquals("getIon", null, ion);
		ion = massSpectrum.getIon(46);
		assertTrue("getIon", ion != null);
		assertEquals("getIon(46) abundance", 88.15093f, ion.getAbundance(), 0);
		assertEquals("getIon(46) ion", 46.0d, ion.getIon(), 0);
	}
}
