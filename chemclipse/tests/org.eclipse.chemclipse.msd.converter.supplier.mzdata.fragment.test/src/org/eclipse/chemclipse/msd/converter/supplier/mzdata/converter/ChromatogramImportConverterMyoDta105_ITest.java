/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.IVendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.IVendorScan;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorScan;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIonMSn;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ChromatogramImportConverterMyoDta105_ITest {

	private IVendorChromatogram chromatogram;

	@BeforeAll
	public void setUp() {

		File importFile = new File("testData/files/import/myo_dta_1.05cv.mzData");
		ChromatogramImportConverter converter = new ChromatogramImportConverter();
		IProcessingInfo<IChromatogramMSD> processingInfo = converter.convert(importFile, new NullProgressMonitor());
		chromatogram = (VendorChromatogram)processingInfo.getProcessingResult();
	}

	@Test
	public void testSample() {

		assertEquals("myo 7/22 1/50", chromatogram.getSampleName());
	}

	@Test
	public void testOperator() {

		assertEquals("Investigator, HUPO, http://psidev.sf.net", chromatogram.getOperator());
	}

	@Test
	public void testInstrument() {

		assertEquals("LCQ Deca XP", chromatogram.getInstrument());
	}

	@Test
	public void testEditHistory() {

		assertEquals("Deisotoping", chromatogram.getEditHistory().get(0).getDescription());
		assertEquals("ChargeDeconvolution", chromatogram.getEditHistory().get(1).getDescription());
		assertEquals("PeakProcessing", chromatogram.getEditHistory().get(2).getDescription());
		assertEquals("PSI-MS XCalibur RAW converter 1.05", chromatogram.getEditHistory().get(2).getEditor());
	}

	@Test
	public void testNumberOfScans() {

		assertEquals(6, chromatogram.getNumberOfScans());
	}

	@Test
	public void testTotalSignal() {

		assertEquals(4.4378344E7f, chromatogram.getTotalSignal(), 0);
	}

	@Test
	public void testMaxIonAbundance() {

		assertEquals(2383616.0f, chromatogram.getMaxIonAbundance(), 0);
	}

	@Test
	public void testFirstScan() {

		IVendorScan massSpectrum = (VendorScan)chromatogram.getScan(1);
		assertEquals(331, massSpectrum.getNumberOfIons(), "Ions");
		assertEquals(Polarity.POSITIVE, massSpectrum.getPolarity());
		assertEquals(661.65d, massSpectrum.getPrecursorIon(), 0);
		IIonMSn ionMSn = (IIonMSn)massSpectrum.getIons().get(0);
		assertEquals(28d, ionMSn.getIonTransition().getCollisionEnergy(), 0);
		assertEquals(MassSpectrumType.CENTROID, massSpectrum.getMassSpectrumType());
	}
}
