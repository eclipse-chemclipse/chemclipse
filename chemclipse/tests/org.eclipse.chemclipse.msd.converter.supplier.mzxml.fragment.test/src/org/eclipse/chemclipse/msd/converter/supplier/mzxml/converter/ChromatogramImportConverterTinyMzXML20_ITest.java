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
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.IVendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.IVendorScan;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorChromatogram;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ChromatogramImportConverterTinyMzXML20_ITest {

	private IVendorChromatogram chromatogram;

	@BeforeAll
	public void setUp() {

		File importFile = new File("testData/files/import/tiny1.mzXML2.0.mzxml");
		ChromatogramImportConverter converter = new ChromatogramImportConverter();
		IProcessingInfo<IChromatogramMSD> processingInfo = converter.convert(importFile, new NullProgressMonitor());
		chromatogram = (VendorChromatogram)processingInfo.getProcessingResult();
	}

	@Test
	public void testInstrument() {

		assertEquals("ThermoFinnigan LCQ Deca", chromatogram.getInstrument());
		assertEquals("ESI", chromatogram.getIonisation());
		assertEquals("Ion Trap", chromatogram.getMassAnalyzer());
		assertEquals("EMT", chromatogram.getMassDetector());
		assertEquals("Xcalibur 1.3 alpha 8", chromatogram.getSoftware());
	}

	@Test
	public void testEditHistory() {

		assertEquals("conversion", chromatogram.getEditHistory().get(0).getDescription());
		assertEquals("Thermo2mzXML 1", chromatogram.getEditHistory().get(0).getEditor());
	}

	@Test
	public void testNumberOfScans() {

		assertEquals(2, chromatogram.getNumberOfScans(), "NumberOfScans");
	}

	@Test
	public void testTotalSignal() {

		assertEquals(1.7440164E7, chromatogram.getTotalSignal(), 0, "Total Signal");
	}

	@Test
	public void testMaxIonAbundance() {

		assertEquals(301045.0f, chromatogram.getMaxIonAbundance(), 0, "Max Signal");
	}

	@Test
	public void testFirstScan() {

		IVendorScan massSpectrum = (IVendorScan)chromatogram.getScan(1);
		assertEquals(1313, massSpectrum.getNumberOfIons(), "Ions");
		assertEquals(Polarity.POSITIVE, massSpectrum.getPolarity(), "Polarity");
		assertEquals(353430, massSpectrum.getRetentionTime(), "RT");
	}

	@Test
	public void testSecondScan() {

		IVendorScan massSpectrum = (IVendorScan)chromatogram.getScan(2);
		assertEquals(43, massSpectrum.getNumberOfIons(), "Ions");
		assertEquals(356680, massSpectrum.getRetentionTime(), "RT");
		assertEquals(Polarity.POSITIVE, massSpectrum.getPolarity(), "Polarity");
		assertEquals(2, massSpectrum.getMassSpectrometer(), "MS");
		assertEquals(445.3500061035156, massSpectrum.getPrecursorIon(), 0, "Precursor");

		IIonMSn ionMSn = (IIonMSn)massSpectrum.getHighestIon();
		IIonTransition ionTransition = ionMSn.getIonTransition();
		assertEquals(35, ionTransition.getCollisionEnergy(), 0, "CE");
		assertEquals(445, ionTransition.getQ1Ion(), 0, "Q1");
		assertEquals(531.1, ionTransition.getQ3Ion(), 0, "Q2");
	}
}
