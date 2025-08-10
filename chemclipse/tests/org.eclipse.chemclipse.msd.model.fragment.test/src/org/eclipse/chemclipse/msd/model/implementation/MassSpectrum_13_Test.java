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
 * Test the method makeDeepCopy() of mass spectrum.
 */
public class MassSpectrum_13_Test {

	private ScanMSD massSpectrum;
	private Ion ion;
	private ScanMSD templateMassSpectrum;

	@Before
	public void setUp() throws CloneNotSupportedException {

		templateMassSpectrum = new ScanMSD();
		templateMassSpectrum.setDirty(true);
		ion = new Ion(45.5f, 78500.2f);
		templateMassSpectrum.addIon(ion);
		ion = new Ion(85.4f, 3000.5f);
		templateMassSpectrum.addIon(ion);
		ion = new Ion(104.1f, 120000.4f);
		templateMassSpectrum.addIon(ion);
		ion = new Ion(32.6f, 890520.4f);
		templateMassSpectrum.addIon(ion);
		ion = new Ion(105.7f, 120000.4f);
		templateMassSpectrum.addIon(ion);
		ion = new Ion(28.2f, 33000.5f);
		templateMassSpectrum.addIon(ion);
		ion = new Ion(85.4f, 3000.5f);
		templateMassSpectrum.removeIon(ion);
		massSpectrum = (ScanMSD)templateMassSpectrum.makeDeepCopy();
	}

	@Test
	public void testGetIons_1() {

		assertEquals("getIons", 5, massSpectrum.getIons().size());
	}

	@Test
	public void getTotalSignal_1() {

		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetExtractedIonSignal_1() {

		assertEquals("getExtractedIonSignal", 0.0f, massSpectrum.getExtractedIonSignal().getAbundance(0), 0);
	}

	@Test
	public void testGetExtractedIonSignal_2() {

		assertEquals("getExtractedIonSignal", 120000.4f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(104), 0);
	}

	@Test
	public void testGetExtractedIonSignal_3() {

		assertEquals("getExtractedIonSignal", 890520.4f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(33), 0);
	}

	@Test
	public void testGetExtractedIonSignal_4() {

		assertEquals("getExtractedIonSignal", 120000.4f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(106), 0);
	}

	@Test
	public void testGetBasePeak_1() {

		assertEquals("getBasePeak", 32.599998474121094d, massSpectrum.getBasePeak(), 0);
	}

	@Test
	public void testGetBasePeakAbundance_1() {

		assertEquals("getBasePeakAbundance", 890520.4f, massSpectrum.getBasePeakAbundance(), 0);
	}

	@Test
	public void testGetHighestAbundance_1() {

		assertEquals("getHighestAbundance", 890520.4f, massSpectrum.getHighestAbundance().getAbundance(), 0);
	}

	@Test
	public void testGetHighestAbundance_2() {

		assertEquals("getHighestAbundance", 32.599998474121094d, massSpectrum.getHighestAbundance().getIon(), 0);
	}

	@Test
	public void testGetHighestIon_1() {

		assertEquals("getHighestIon", 120000.4f, massSpectrum.getHighestIon().getAbundance(), 0);
	}

	@Test
	public void testGetHighestIon_2() {

		assertEquals("getHighestIon", 105.69999694824219d, massSpectrum.getHighestIon().getIon(), 0);
	}

	@Test
	public void testGetLowestAbundance_1() {

		assertEquals("getLowestAbundance", 33000.5f, massSpectrum.getLowestAbundance().getAbundance(), 0);
	}

	@Test
	public void testGetLowestAbundance_2() {

		assertEquals("getLowestAbundance", 28.200000762939453d, massSpectrum.getLowestAbundance().getIon(), 0);
	}

	@Test
	public void testGetLowestIon_1() {

		assertEquals("getLowestIon", 33000.5f, massSpectrum.getLowestIon().getAbundance(), 0);
	}

	@Test
	public void testGetLowestIon_2() {

		assertEquals("getLowestIon", 28.200000762939453d, massSpectrum.getLowestIon().getIon(), 0);
	}

	@Test
	public void testGetIonBounds_1() {

		IIonBounds bounds = massSpectrum.getIonBounds();
		assertEquals("getLowestIon().getAbundance()", 33000.5f, bounds.getLowestIon().getAbundance(), 0);
		assertEquals("getLowestIon().getIon()", 28.200000762939453d, bounds.getLowestIon().getIon(), 0);
		assertEquals("getHighestIon().getAbundance()", 120000.4f, bounds.getHighestIon().getAbundance(), 0);
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
		assertEquals("getIon(46) abundance", 78500.2f, ion.getAbundance(), 0);
		assertEquals("getIon(46) ion", 46.0d, ion.getIon(), 0);
	}

	@Test
	public void testIsDirty_1() {

		assertEquals("isDirty", true, massSpectrum.isDirty());
	}

	@Test
	public void testEquals_1() {

		assertEquals("equals", true, massSpectrum.equals(templateMassSpectrum));
	}

	@Test
	public void testEquals_2() {

		assertEquals("equals", true, templateMassSpectrum.equals(massSpectrum));
	}

	@Test
	public void testReferenceEquality_1() {

		assertTrue("reference equality", massSpectrum != templateMassSpectrum);
	}

	@Test
	public void testReferenceEquality_2() {

		assertTrue("reference equality getLowestIon()", massSpectrum.getLowestIon() != templateMassSpectrum.getLowestIon());
		assertTrue("reference equality getHighestIon()", massSpectrum.getHighestIon() != templateMassSpectrum.getHighestIon());
	}

	@Test
	public void testReferenceEquality_3() {

		assertTrue("reference equality parentChromatogram", massSpectrum.getParentChromatogram() == templateMassSpectrum.getParentChromatogram());
	}

	@Test
	public void testReferenceEquality_4() {

		assertTrue("reference equality isDirty", massSpectrum.isDirty() == templateMassSpectrum.isDirty());
	}
}
