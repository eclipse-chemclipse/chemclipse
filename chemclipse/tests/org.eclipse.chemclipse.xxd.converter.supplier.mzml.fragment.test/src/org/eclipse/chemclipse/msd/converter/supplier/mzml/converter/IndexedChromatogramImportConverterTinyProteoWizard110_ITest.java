/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.mzml.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.IVendorChromatogramMSD;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.VendorChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class IndexedChromatogramImportConverterTinyProteoWizard110_ITest {

	private IVendorChromatogramMSD chromatogram;

	@BeforeAll
	public void setUp() {

		File importFile = new File("testData/files/import/tiny.pwiz.1.1.mzML");
		ChromatogramImportConverterIndexed converter = new ChromatogramImportConverterIndexed();
		IProcessingInfo<IChromatogramMSD> processingInfo = converter.convert(importFile, new NullProgressMonitor());
		chromatogram = (VendorChromatogramMSD)processingInfo.getProcessingResult();
	}

	@Test
	public void testInstrument() {

		assertEquals("LCQ Deca", chromatogram.getInstrument());
	}

	@Test
	public void testSample() {

		assertEquals("Sample 1", chromatogram.getSampleName());
	}

	@Test
	public void testOperator() {

		assertEquals("William Pennington, Higglesworth University, 12 Higglesworth Avenue, 12045, HI, USA, http://www.higglesworth.edu/, wpennington@higglesworth.edu", chromatogram.getOperator());
	}

	@Test
	public void testEditHistory() {

		assertEquals(4, chromatogram.getEditHistory().size());
		assertEquals("deisotoping", chromatogram.getEditHistory().get(0).getDescription());
		assertEquals("charge deconvolution", chromatogram.getEditHistory().get(1).getDescription());
		assertEquals("peak picking", chromatogram.getEditHistory().get(2).getDescription());
		assertEquals("Conversion to mzML", chromatogram.getEditHistory().get(3).getDescription());
		assertEquals("pwiz 1.0", chromatogram.getEditHistory().get(3).getEditor());
	}

	@Test
	public void testNumberOfScans() {

		assertEquals(4, chromatogram.getNumberOfScans());
	}

	@Test
	public void testStartRetentionTime() {

		assertEquals(353430, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testStopRetentionTime() {

		assertEquals(42050, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testTotalSignal() {

		assertEquals(350.0f, chromatogram.getTotalSignal(), 0);
	}

	@Test
	public void testMaxIonAbundance() {

		assertEquals(20.0f, chromatogram.getMaxIonAbundance(), 0);
	}

	@Test
	public void testFirstScan() {

		IRegularMassSpectrum massSpectrum = (IRegularMassSpectrum)chromatogram.getScan(1);
		// assertEquals(Polarity.POSITIVE, massSpectrum.getPolarity()); // TODO ChromatogramReaderVersion110.setReferencedPolarity
		assertEquals("scan=19", massSpectrum.getIdentifier());
		assertEquals(15, massSpectrum.getNumberOfIons(), "Ions");
		assertEquals(15f, massSpectrum.getIon(0).getAbundance(), 0);
		assertEquals(14f, massSpectrum.getIon(1).getAbundance(), 0);
		assertEquals(13f, massSpectrum.getIon(2).getAbundance(), 0);
		assertEquals(12f, massSpectrum.getIon(3).getAbundance(), 0);
		assertEquals(11f, massSpectrum.getIon(4).getAbundance(), 0);
		assertEquals(10f, massSpectrum.getIon(5).getAbundance(), 0);
		assertEquals(9f, massSpectrum.getIon(6).getAbundance(), 0);
		assertEquals(8f, massSpectrum.getIon(7).getAbundance(), 0);
		assertEquals(7f, massSpectrum.getIon(8).getAbundance(), 0);
		assertEquals(6f, massSpectrum.getIon(9).getAbundance(), 0);
		assertEquals(5f, massSpectrum.getIon(10).getAbundance(), 0);
		assertEquals(4f, massSpectrum.getIon(11).getAbundance(), 0);
		assertEquals(3f, massSpectrum.getIon(12).getAbundance(), 0);
		assertEquals(2f, massSpectrum.getIon(13).getAbundance(), 0);
		assertEquals(1f, massSpectrum.getIon(14).getAbundance(), 0);
	}
}
