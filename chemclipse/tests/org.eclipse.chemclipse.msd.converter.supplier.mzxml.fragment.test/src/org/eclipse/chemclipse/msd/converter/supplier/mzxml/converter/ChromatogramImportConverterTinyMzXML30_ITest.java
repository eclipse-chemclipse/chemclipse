/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
public class ChromatogramImportConverterTinyMzXML30_ITest {

	private IVendorChromatogram chromatogram;

	@BeforeAll
	public void setUp() {

		File importFile = new File("testData/files/import/tiny.mzXML3.0.mzXML");
		ChromatogramImportConverter converter = new ChromatogramImportConverter();
		IProcessingInfo<IChromatogramMSD> processingInfo = converter.convert(importFile, new NullProgressMonitor());
		chromatogram = (VendorChromatogram)processingInfo.getProcessingResult();
	}

	@Test
	public void testInstrument() {

		assertEquals("Thermo Scientific TSQ Quantum", chromatogram.getInstrument());
		assertEquals("ESI", chromatogram.getIonisation());
		assertEquals("ITMS", chromatogram.getMassAnalyzer());
		assertEquals("unknown", chromatogram.getMassDetector());
		assertEquals("Xcalibur 1.5", chromatogram.getSoftware());
	}

	@Test
	public void testEditHistory() {

		assertEquals("conversion", chromatogram.getEditHistory().get(0).getDescription());
		assertEquals("ReAdW 4.0.2(build Jul  1 2008 14:23:37)", chromatogram.getEditHistory().get(0).getEditor());
	}

	@Test
	public void testNumberOfScans() {

		assertEquals(4, chromatogram.getNumberOfScans());
	}

	@Test
	public void testFirstScan() {

		IVendorScan massSpectrum = (IVendorScan)chromatogram.getScan(1);
		assertEquals(10, massSpectrum.getNumberOfIons(), "Ions");
		assertEquals(Polarity.NEGATIVE, massSpectrum.getPolarity(), "Polarity");
		assertEquals(355, massSpectrum.getRetentionTime());

		assertEquals(88.01000213623047, massSpectrum.getLowestIon().getIon());
		assertEquals(2.827909469604492, massSpectrum.getLowestIon().getAbundance());
		assertEquals(88.0999984741211, massSpectrum.getHighestIon().getIon());
		assertEquals(3.1302645206451416, massSpectrum.getHighestIon().getAbundance());
	}

	@Test
	public void testSecondScan() {

		IVendorScan massSpectrum = (IVendorScan)chromatogram.getScan(2);
		assertEquals((short)2, massSpectrum.getMassSpectrometer());
		assertEquals(10, massSpectrum.getNumberOfIons(), "Ions");
		assertEquals(Polarity.NEGATIVE, massSpectrum.getPolarity(), "Polarity");
		assertEquals(369, massSpectrum.getRetentionTime());

		IIonMSn ionMSn = (IIonMSn)massSpectrum.getIons().iterator().next();
		IIonTransition ionTransition = ionMSn.getIonTransition();
		assertEquals(0, ionTransition.getCollisionEnergy(), 0);
		assertEquals(91, ionTransition.getQ1Ion());
		assertEquals(91, ionTransition.getQ3Ion(), 0);
	}
}
