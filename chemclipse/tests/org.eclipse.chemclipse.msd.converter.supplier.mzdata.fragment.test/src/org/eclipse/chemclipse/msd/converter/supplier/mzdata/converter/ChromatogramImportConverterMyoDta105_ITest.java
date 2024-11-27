/*******************************************************************************
 * Copyright (c) 2023, 2024 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.converter;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.mzdata.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.IVendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorChromatogram;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

import junit.framework.TestCase;

public class ChromatogramImportConverterMyoDta105_ITest extends TestCase {

	private IVendorChromatogram chromatogram;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		File importFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_MYO_DTA_105));
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

		assertEquals("NumberOfScans", 6, chromatogram.getNumberOfScans());
	}

	@Test
	public void testTotalSignal() {

		assertEquals("Total Signal", 4.4378344E7f, chromatogram.getTotalSignal());
	}

	@Test
	public void testMaxIonAbundance() {

		assertEquals("Max Signal", 2383616.0f, chromatogram.getMaxIonAbundance());
	}

	@Test
	public void testFirstScan() {

		IScanMSD massSpectrum = (IScanMSD)chromatogram.getScan(1);
		assertEquals("Ions", 331, massSpectrum.getNumberOfIons());
	}
}
