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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the methods equals and hashCode.
 */
public class Chromatogram_2_Test {

	private ChromatogramMSD chromatogram1;
	private ChromatogramMSD chromatogram2;
	private VendorMassSpectrum supplierMassSpectrum;
	private IIon ion;

	@Before
	public void setUp() throws Exception {

		chromatogram1 = new ChromatogramMSD();
		chromatogram2 = new ChromatogramMSD();
		supplierMassSpectrum = new VendorMassSpectrum();
		ion = new Ion(25.5f, 45862.3f);
		supplierMassSpectrum.addIon(ion);
		chromatogram2.addScan(supplierMassSpectrum);
	}

	@Test
	public void testEquals_1() {

		assertFalse(chromatogram1.equals(chromatogram2));
	}

	@Test
	public void testEquals_2() {

		assertFalse(chromatogram2.equals(chromatogram1));
	}

	@Test
	public void testHashCode_1() {

		assertTrue(chromatogram1.hashCode() != chromatogram2.hashCode());
	}

	@Test
	public void testHashCode_2() {

		assertTrue(chromatogram2.hashCode() != chromatogram1.hashCode());
	}
}
