/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.io;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.msd.model.core.IIonTransitionSettings;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.TestPathHelper;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramReader_1_MSD_0801_ITest extends ChromatogramReaderMSDTestCase {

	@Override
	@Before
	public void setUp() throws Exception {

		pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1_MSD_0801);
		super.setUp();
	}

	@Test
	public void testReader_1() {

		assertEquals(110, chromatogram.getNumberOfScans());
	}

	@Test
	public void testReader_2() {

		assertEquals("Chromatogram1-0801", chromatogram.getName());
	}

	@Test
	public void testReader_3() {

		assertEquals(3927, chromatogram.getNumberOfScanIons());
	}

	@Test
	public void testReader_4() {

		assertEquals(841111, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testReader_5() {

		assertEquals(918652, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testReader_6() {

		assertEquals(15.1d, chromatogram.getStartIon(), 0);
	}

	@Test
	public void testReader_7() {

		assertEquals(281.3d, chromatogram.getStopIon(), 0);
	}

	@Test
	public void testReader_8() {

		assertEquals(442733.0f, chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testReader_9() {

		assertEquals(21543.0f, chromatogram.getMinSignal(), 0);
	}

	@Test
	public void testReader_10() {

		assertEquals(841111, chromatogram.getScanDelay());
	}

	@Test
	public void testReader_11() {

		assertEquals(710, chromatogram.getScanInterval());
	}

	@Test
	public void testChromatogramReader_12() {

		IScanMSD massSpectrum = chromatogram.getSupplierScan(1);

		assertEquals(32, massSpectrum.getNumberOfIons());
		assertEquals(15.1d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(141.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(206.9d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(131.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(28.0d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(7851.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(841111, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
	}

	@Test
	public void testChromatogramReader_13() {

		IScanMSD massSpectrum = chromatogram.getSupplierScan(22);

		assertEquals(35, massSpectrum.getNumberOfIons());
		assertEquals(15.1d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(107.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(206.9d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(148.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(28.1d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(7952.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(856050, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
	}

	@Test
	public void testChromatogramReader_14() {

		IScanMSD massSpectrum = chromatogram.getSupplierScan(80);

		assertEquals(29, massSpectrum.getNumberOfIons());
		assertEquals(16.0d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(223.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(281.1d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(100.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(28.0d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(7228.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(897310, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
	}

	@Test
	public void testChromatogramReader_15() {

		IScanMSD massSpectrum = chromatogram.getSupplierScan(110);

		assertEquals(31, massSpectrum.getNumberOfIons());
		assertEquals(15.1d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(83.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(206.7d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(67.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(28.1d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(7759.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(918652, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
	}

	@Test
	public void testReader_16() {

		assertEquals(4, chromatogram.getPeaks().size());
	}

	@Test
	public void testReader_17() {

		assertEquals(1.7471400074129336E7d, chromatogram.getPeakIntegratedArea(), 0);
	}

	@Test
	public void testReader_18() {

		assertEquals(3.7163158155000016E7d, chromatogram.getChromatogramIntegratedArea(), 0);
	}

	@Test
	public void testReader_19() {

		assertEquals(1.7316770049970705E7d, chromatogram.getBackgroundIntegratedArea(), 0);
	}

	@Test
	public void testReader_20() {

		assertEquals(0.0d, chromatogram.getSampleWeight(), 0);
	}

	@Test
	public void testReader_21() {

		IIonTransitionSettings ionTransitionSettings = chromatogram.getIonTransitionSettings();
		assertEquals(0, ionTransitionSettings.size());
	}
}
