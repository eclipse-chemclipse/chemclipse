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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.internal.core;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class SimpleChromatogramTestCase {

	protected IChromatogramMSD chromatogram;
	protected IRegularMassSpectrum supplierMassSpectrum;
	protected IIon ion;
	protected IChromatogramSelectionMSD chromatogramSelection;

	@Before
	public void setUp() throws Exception {

		// ------------------------------Chromatogram
		chromatogram = new ChromatogramMSD();
		chromatogram.setScanDelay(4500);
		chromatogram.setScanInterval(1000);
		supplierMassSpectrum = new VendorMassSpectrum();
		ion = new Ion(43, 1000.0f);
		supplierMassSpectrum.addIon(ion);
		chromatogram.addScan(supplierMassSpectrum);
		supplierMassSpectrum = new VendorMassSpectrum();
		ion = new Ion(43, 2000.0f);
		supplierMassSpectrum.addIon(ion);
		chromatogram.addScan(supplierMassSpectrum);
		supplierMassSpectrum = new VendorMassSpectrum();
		ion = new Ion(43, 1000.0f);
		supplierMassSpectrum.addIon(ion);
		chromatogram.addScan(supplierMassSpectrum);
		chromatogram.recalculateRetentionTimes();
		// ------------------------------Chromatogram
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}
}
