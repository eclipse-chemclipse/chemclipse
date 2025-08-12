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

public class MassSpectrum_18_Test {

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
		massSpectrum.adjustIons(0.3f);
	}

	@Test
	public void testGetIons_1() {

		assertEquals("getIons", 5, massSpectrum.getIons().size());
	}

	@Test
	public void testGetTotalSignal_1() {

		assertEquals("getTotalSignal", 1614628.4f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetExtractedIonSignal_1() {

		assertEquals("getExtractedIonSignal", 0.0f, massSpectrum.getExtractedIonSignal().getAbundance(0), 0);
	}

	@Test
	public void testGetExtractedIonSignal_2() {

		assertEquals("getExtractedIonSignal", 156000.52f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(104), 0);
	}

	@Test
	public void testGetExtractedIonSignal_3() {

		assertEquals("getExtractedIonSignal", 1157676.5f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(33), 0);
	}

	@Test
	public void testGetExtractedIonSignal_4() {

		assertEquals("getExtractedIonSignal", 156000.52f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(106), 0);
	}
}
