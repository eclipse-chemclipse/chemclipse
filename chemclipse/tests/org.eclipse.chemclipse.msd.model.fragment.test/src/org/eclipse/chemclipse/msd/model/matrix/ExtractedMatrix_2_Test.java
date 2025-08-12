/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.matrix;

import static org.junit.Assert.assertNotNull;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class ExtractedMatrix_2_Test {

	private IRegularMassSpectrum supplierMassSpectrum;
	private IIon defaultIon;
	private IChromatogramMSD chromatogram;
	private float[][] signalMatrix;

	@Before
	public void setUp() {

		int scans = 10;
		int ionStart = 25;
		int ionStop = 30;
		/*
		 * missing ion 28
		 */
		chromatogram = new ChromatogramMSD();
		for(int scan = 1; scan <= scans; scan++) {
			supplierMassSpectrum = new VendorMassSpectrum();
			supplierMassSpectrum.setRetentionTime(scan);
			supplierMassSpectrum.setRetentionIndex(scan / 60.0f);
			for(int ion = ionStart; ion <= ionStop; ion++) {
				defaultIon = new Ion(ion, ion * scan);
				if(ion != 28) {
					supplierMassSpectrum.addIon(defaultIon);
				}
			}
			chromatogram.addScan(supplierMassSpectrum);
		}
		signalMatrix = new float[10][6];
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 6; j++) {
				signalMatrix[i][j] = 10.1f;
			}
		}
	}

	@Test
	public void testConstructor_1() {

		ChromatogramSelectionMSD selection = new ChromatogramSelectionMSD(chromatogram);
		ExtractedMatrix extracted = new ExtractedMatrix(selection);
		assertNotNull(extracted);
	}

	@Test
	public void testUpdate_1() {

		ChromatogramSelectionMSD selection = new ChromatogramSelectionMSD(chromatogram);
		ExtractedMatrix extracted = new ExtractedMatrix(selection);
		extracted.updateSignal();
	}
}
