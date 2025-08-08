/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.TestPathHelper;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.model.IVendorChromatogram;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.model.VendorChromatogram;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramImportConverterHandCrafted110_ITest {

	private IVendorChromatogram chromatogram;

	@Before
	public void setUp() throws Exception {

		File importFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_PDA_HANDCRAFTED));
		ChromatogramImportConverter converter = new ChromatogramImportConverter();
		IProcessingInfo<IChromatogramWSD> processingInfo = converter.convert(importFile, new NullProgressMonitor());
		chromatogram = (VendorChromatogram)processingInfo.getProcessingResult();
	}

	@Test
	public void testSample() {

		assertEquals("Sample1", chromatogram.getSampleName());
	}

	@Test
	public void testOperator() {

		assertEquals("William Pennington, Higglesworth University, 12 Higglesworth Avenue, 12045, HI, USA, http://www.higglesworth.edu/, wpennington@higglesworth.edu, dort, Drek'Thar, da", chromatogram.getOperator());
	}

	@Test
	public void testEditHistory() {

		assertEquals("Conversion to mzML", chromatogram.getEditHistory().get(0).getDescription());
	}

	@Test
	public void testNumberOfScans() {

		assertEquals("NumberOfScans", 15, chromatogram.getNumberOfScans());
	}

	@Test
	public void testStartRetentionTime() {

		assertEquals("Start RT", 0, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testStopRetentionTime() {

		assertEquals("Stop RT", 14000, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testTotalSignal() {

		assertEquals("Total Signal", 225.0f, chromatogram.getTotalSignal(), 0);
	}

	@Test
	public void testMaxAbsorption() {

		assertEquals("Max Signal", 120.0f, chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testScans() {

		IScanWSD pdaSpectrum = (IScanWSD)chromatogram.getScan(1);
		assertEquals("Scans", 15, pdaSpectrum.getNumberOfScanSignals());
		assertEquals(15f, pdaSpectrum.getScanSignal(0).getAbsorbance(), 0);
		assertEquals(0f, pdaSpectrum.getScanSignal(0).getWavelength(), 0);
		assertEquals(14f, pdaSpectrum.getScanSignal(1).getAbsorbance(), 0);
		assertEquals(1f, pdaSpectrum.getScanSignal(1).getWavelength(), 0);
		assertEquals(13f, pdaSpectrum.getScanSignal(2).getAbsorbance(), 0);
		assertEquals(2f, pdaSpectrum.getScanSignal(2).getWavelength(), 0);
		assertEquals(12f, pdaSpectrum.getScanSignal(3).getAbsorbance(), 0);
		assertEquals(3f, pdaSpectrum.getScanSignal(3).getWavelength(), 0);
		assertEquals(11f, pdaSpectrum.getScanSignal(4).getAbsorbance(), 0);
		assertEquals(4f, pdaSpectrum.getScanSignal(4).getWavelength(), 0);
		assertEquals(10f, pdaSpectrum.getScanSignal(5).getAbsorbance(), 0);
		assertEquals(5f, pdaSpectrum.getScanSignal(5).getWavelength(), 0);
	}
}
