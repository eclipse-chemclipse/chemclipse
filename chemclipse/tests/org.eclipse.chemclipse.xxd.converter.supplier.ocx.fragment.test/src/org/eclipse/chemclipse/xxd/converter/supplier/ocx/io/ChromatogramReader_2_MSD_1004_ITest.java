/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
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
import static org.junit.Assert.assertNotNull;

import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IIonTransitionGroup;
import org.eclipse.chemclipse.msd.model.core.IIonTransitionSettings;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.TestPathHelper;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramReader_2_MSD_1004_ITest extends ChromatogramReaderMSDTestCase {

	@Override
	@Before
	public void setUp() throws Exception {

		pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_2_MSD_1004);
		super.setUp();
	}

	@Test
	public void testReader_1() {

		assertEquals(207, chromatogram.getNumberOfScans());
	}

	@Test
	public void testReader_2() {

		assertEquals("Chromatogram2-1004", chromatogram.getName());
	}

	@Test
	public void testReader_3() {

		assertEquals(2030, chromatogram.getNumberOfScanIons());
	}

	@Test
	public void testReader_4() {

		assertEquals(925500, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testReader_5() {

		assertEquals(1031906, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testReader_6() {

		assertEquals(62.8d, chromatogram.getStartIon(), 0);
	}

	@Test
	public void testReader_7() {

		assertEquals(456.3d, chromatogram.getStopIon(), 0);
	}

	@Test
	public void testReader_8() {

		assertEquals(3.237204E7f, chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testReader_8a() {

		chromatogram.enforceLoadScanProxies(new NullProgressMonitor());
		assertEquals(3.237204E7f, chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testReader_9() {

		assertEquals(143985.0f, chromatogram.getMinSignal(), 0);
	}

	@Test
	public void testReader_9a() {

		chromatogram.enforceLoadScanProxies(new NullProgressMonitor());
		assertEquals(143985.0f, chromatogram.getMinSignal(), 0);
	}

	@Test
	public void testReader_10() {

		assertEquals(925500, chromatogram.getScanDelay());
	}

	@Test
	public void testReader_11() {

		assertEquals(520, chromatogram.getScanInterval());
	}

	@Test
	public void testChromatogramReader_12() {

		IScanMSD massSpectrum = chromatogram.getSupplierScan(1);
		/*
		 * Proxy
		 */
		assertEquals(6, massSpectrum.getNumberOfIons());
		assertEquals(309421.0f, massSpectrum.getTotalSignal(), 0);
		/*
		 * Import complete
		 */
		massSpectrum.enforceLoadScanProxy();
		assertEquals(6, massSpectrum.getNumberOfIons());
		assertEquals(159.0d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(41303.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(456.3d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(58610.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(377.4d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(61234.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(925500, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertEquals(1, massSpectrum.getTimeSegmentId());
		assertEquals(1, massSpectrum.getCycleNumber());
		assertEquals(309421.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testChromatogramReader_13() {

		IScanMSD massSpectrum = chromatogram.getSupplierScan(92);
		/*
		 * Proxy
		 */
		assertEquals(11, massSpectrum.getNumberOfIons());
		assertEquals(641153.0f, massSpectrum.getTotalSignal(), 0);
		/*
		 * Import complete
		 */
		massSpectrum.enforceLoadScanProxy();
		assertEquals(11, massSpectrum.getNumberOfIons());
		assertEquals(135.0d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(24425.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(330.0d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(89695.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(265.0d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(176973.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(972500, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertEquals(1, massSpectrum.getTimeSegmentId());
		assertEquals(1, massSpectrum.getCycleNumber());
		assertEquals(641153.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testChromatogramReader_14() {

		IScanMSD massSpectrum = chromatogram.getSupplierScan(147);
		/*
		 * Proxy
		 */
		assertEquals(10, massSpectrum.getNumberOfIons());
		assertEquals(409367.0f, massSpectrum.getTotalSignal(), 0);
		/*
		 * Import complete
		 */
		massSpectrum.enforceLoadScanProxy();
		assertEquals(10, massSpectrum.getNumberOfIons());
		assertEquals(62.9d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(5871.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(231.0d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(47918.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(81.1d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(105117.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(1001031, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertEquals(1, massSpectrum.getTimeSegmentId());
		assertEquals(1, massSpectrum.getCycleNumber());
		assertEquals(409367.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testChromatogramReader_15() {

		IScanMSD massSpectrum = chromatogram.getSupplierScan(207);
		/*
		 * Proxy
		 */
		assertEquals(15, massSpectrum.getNumberOfIons());
		assertEquals(527807.0f, massSpectrum.getTotalSignal(), 0);
		/*
		 * Import complete
		 */
		massSpectrum.enforceLoadScanProxy();
		assertEquals(15, massSpectrum.getNumberOfIons());
		assertEquals(78.9d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(96610.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(245.0d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(43242.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(78.9d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(96610.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(1031906, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertEquals(527807.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testReader_16() {

		assertEquals(0, chromatogram.getPeaks().size());
	}

	@Test
	public void testReader_17() {

		assertEquals(0.0d, chromatogram.getPeakIntegratedArea(), 0);
	}

	@Test
	public void testReader_18() {

		assertEquals(0.0d, chromatogram.getChromatogramIntegratedArea(), 0);
	}

	@Test
	public void testReader_19() {

		assertEquals(0.0d, chromatogram.getBackgroundIntegratedArea(), 0);
	}

	@Test
	public void testReader_20() {

		assertEquals(0.0d, chromatogram.getSampleWeight(), 0);
	}

	@Test
	public void testReader_21() {

		chromatogram.enforceLoadScanProxies(new NullProgressMonitor());
		IIonTransitionSettings ionTransitionSettings = chromatogram.getIonTransitionSettings();
		assertEquals(12, ionTransitionSettings.size());
	}

	@Test
	public void testReader_22() {

		chromatogram.enforceLoadScanProxies(new NullProgressMonitor());
		IIonTransitionSettings ionTransitionSettings = chromatogram.getIonTransitionSettings();
		IIonTransitionGroup ionTransitionGroup = ionTransitionSettings.get(0);
		IIonTransition ionTransition = ionTransitionGroup.get(0);
		//
		assertEquals(1, ionTransitionGroup.size());
		assertEquals(15.0d, ionTransition.getCollisionEnergy(), 0);
		assertEquals(292, ionTransition.getQ1Ion());
		assertEquals(292.0d, ionTransition.getQ1StartIon(), 0);
		assertEquals(292.0d, ionTransition.getQ1StopIon(), 0);
		assertEquals(1.2d, ionTransition.getQ1Resolution(), 0);
		assertEquals(206.1d, ionTransition.getQ3Ion(), 0);
		assertEquals(205.7d, ionTransition.getQ3StartIon(), 0);
		assertEquals(206.4d, ionTransition.getQ3StopIon(), 0);
		assertEquals(1.2d, ionTransition.getQ3Resolution(), 0);
		assertEquals(6, ionTransition.getTransitionGroup());
	}

	@Test
	public void testReader_23() {

		chromatogram.enforceLoadScanProxies(new NullProgressMonitor());
		IIonTransitionSettings ionTransitionSettings = chromatogram.getIonTransitionSettings();
		IIonTransitionGroup ionTransitionGroup = ionTransitionSettings.get(11);
		IIonTransition ionTransition = ionTransitionGroup.get(0);
		//
		assertEquals(14, ionTransitionGroup.size());
		assertEquals(10.0d, ionTransition.getCollisionEnergy(), 0);
		assertEquals(161, ionTransition.getQ1Ion());
		assertEquals(161.0d, ionTransition.getQ1StartIon(), 0);
		assertEquals(161.0d, ionTransition.getQ1StopIon(), 0);
		assertEquals(1.2d, ionTransition.getQ1Resolution(), 0);
		assertEquals(99.1d, ionTransition.getQ3Ion(), 0);
		assertEquals(98.7d, ionTransition.getQ3StartIon(), 0);
		assertEquals(99.4d, ionTransition.getQ3StopIon(), 0);
		assertEquals(1.2d, ionTransition.getQ3Resolution(), 0);
		assertEquals(11, ionTransition.getTransitionGroup());
	}

	@Test
	public void testReader_24() {

		chromatogram.enforceLoadScanProxies(new NullProgressMonitor());
		IIonTransitionSettings ionTransitionSettings = chromatogram.getIonTransitionSettings();
		IIonTransitionGroup ionTransitionGroup = ionTransitionSettings.get(11);
		IIonTransition ionTransition = ionTransitionGroup.get(12);
		//
		assertEquals(14, ionTransitionGroup.size());
		assertEquals(15.0d, ionTransition.getCollisionEnergy(), 0);
		assertEquals(359, ionTransition.getQ1Ion());
		assertEquals(359.0d, ionTransition.getQ1StartIon(), 0);
		assertEquals(359.0d, ionTransition.getQ1StopIon(), 0);
		assertEquals(1.2d, ionTransition.getQ1Resolution(), 0);
		assertEquals(243.1d, ionTransition.getQ3Ion(), 0);
		assertEquals(242.7d, ionTransition.getQ3StartIon(), 0);
		assertEquals(243.4d, ionTransition.getQ3StopIon(), 0);
		assertEquals(1.2d, ionTransition.getQ3Resolution(), 0);
		assertEquals(11, ionTransition.getTransitionGroup());
	}

	@Test
	public void testChromatogramReader_25() {

		IScanMSD massSpectrum = chromatogram.getSupplierScan(117);
		massSpectrum.enforceLoadScanProxy();
		assertNotNull(massSpectrum.getOptimizedMassSpectrum());
		assertEquals(8, massSpectrum.getTargets().size());
	}
}
