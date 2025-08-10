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

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.junit.Before;
import org.junit.Test;

public class Chromatogram_20_Test {

	private IChromatogramMSD chromatogram;
	private VendorMassSpectrum supplierMassSpectrum;
	private IIon ion;

	@Before
	public void setUp() throws Exception {

		chromatogram = new ChromatogramMSD();
		// ------------------------------Scan 1
		supplierMassSpectrum = new VendorMassSpectrum();
		supplierMassSpectrum.setRetentionTime(7500);
		ion = new Ion(45.4f, 65883.3f);
		supplierMassSpectrum.addIon(ion);
		ion = new Ion(104.3f, 102453.3f);
		supplierMassSpectrum.addIon(ion);
		ion = new Ion(86.2f, 302410.3f);
		supplierMassSpectrum.addIon(ion);
		chromatogram.addScan(supplierMassSpectrum);
		// ------------------------------Scan 1
		// ------------------------------Scan 2
		supplierMassSpectrum = new VendorMassSpectrum();
		supplierMassSpectrum.setRetentionTime(10500);
		ion = new Ion(18.1f, 883.3f);
		supplierMassSpectrum.addIon(ion);
		ion = new Ion(146.3f, 2453.3f);
		supplierMassSpectrum.addIon(ion);
		ion = new Ion(48.2f, 3021.3f);
		supplierMassSpectrum.addIon(ion);
		chromatogram.addScan(supplierMassSpectrum);
		// ------------------------------Scan 2
		// ------------------------------Scan 3
		supplierMassSpectrum = new VendorMassSpectrum();
		supplierMassSpectrum.setRetentionTime(13500);
		ion = new Ion(22.1f, 6883.3f);
		supplierMassSpectrum.addIon(ion);
		ion = new Ion(80.3f, 1023.3f);
		supplierMassSpectrum.addIon(ion);
		ion = new Ion(190.2f, 2410.3f);
		supplierMassSpectrum.addIon(ion);
		chromatogram.addScan(supplierMassSpectrum);
		// ------------------------------Scan 3
	}

	@Test
	public void testGetStartIon_1() {

		double startIon = chromatogram.getStartIon();
		assertEquals("startIon", 18.100000381469727d, startIon, 0);
	}

	@Test
	public void testGetStopIon_1() {

		double stopIon = chromatogram.getStopIon();
		assertEquals("stopIon", 190.1999969482422d, stopIon, 0);
	}
}
