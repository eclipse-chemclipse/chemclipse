/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.mzdata.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.IVendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.IVendorScan;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorScan;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramImportConverterMyoDta104_ITest {

	private IVendorChromatogram chromatogram;

	@Before
	public void setUp() throws Exception {

		File importFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_MYO_DTA_104));
		ChromatogramImportConverter converter = new ChromatogramImportConverter();
		IProcessingInfo<IChromatogramMSD> processingInfo = converter.convert(importFile, new NullProgressMonitor());
		chromatogram = (VendorChromatogram)processingInfo.getProcessingResult();
	}

	@Test
	public void testSample() {

		assertEquals("Horse Myoglobin", chromatogram.getSampleName());
	}

	@Test
	public void testOperator() {

		assertEquals("Randy Julian, Eli Lilly, rkj@lilly.com", chromatogram.getOperator());
	}

	@Test
	public void testInstrument() {

		assertEquals("LCQ Deca XP", chromatogram.getInstrument());
	}

	@Test
	public void testEditHistory() {

		assertEquals("deisotoped", chromatogram.getEditHistory().get(0).getDescription());
		assertEquals("chargeDeconvolved", chromatogram.getEditHistory().get(1).getDescription());
		assertEquals("peakProcessing", chromatogram.getEditHistory().get(2).getDescription());
		assertEquals("PSI-MS XCalibur RAW converter 1.04", chromatogram.getEditHistory().get(2).getEditor());
	}

	@Test
	public void testNumberOfScans() {

		assertEquals("NumberOfScans", 6, chromatogram.getNumberOfScans());
	}

	@Test
	public void testTotalSignal() {

		assertEquals("Total Signal", 4.4378344E7f, chromatogram.getTotalSignal(), 0);
	}

	@Test
	public void testMaxIonAbundance() {

		assertEquals("Max Signal", 2383616.0f, chromatogram.getMaxIonAbundance(), 0);
	}

	@Test
	public void testFirstScan() {

		IVendorScan massSpectrum = (VendorScan)chromatogram.getScan(1);
		assertEquals("Ions", 331, massSpectrum.getNumberOfIons());
		assertEquals(Polarity.POSITIVE, massSpectrum.getPolarity());
		assertEquals(661.65d, massSpectrum.getPrecursorIon(), 0);
		assertEquals(28d, massSpectrum.getIons().get(0).getIonTransition().getCollisionEnergy(), 0);
	}
}
