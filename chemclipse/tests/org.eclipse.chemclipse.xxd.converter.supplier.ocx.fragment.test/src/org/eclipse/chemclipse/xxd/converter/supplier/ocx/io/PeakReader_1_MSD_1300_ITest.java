/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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

import java.util.Set;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.TestPathHelper;
import org.junit.Before;
import org.junit.Test;

public class PeakReader_1_MSD_1300_ITest extends PeakReaderMSDTestCase {

	@Override
	@Before
	public void setUp() throws Exception {

		pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1_MSD_1300);
		super.setUp();
	}

	@Test
	public void testReader_1() {

		assertEquals(4, peaks.getPeaks().size());
	}

	@Test
	public void testReader_2() {

		IPeak peak = peaks.getPeak(1);
		IPeakMSD peakMSD = (IPeakMSD)peak;
		assertEquals("Peak Detector First Derivative", peakMSD.getDetectorDescription());
		assertEquals(1219140.1874049378d, peakMSD.getIntegratedArea(), 0);
		assertEquals("Integrator Trapezoid: TIC", peakMSD.getIntegratorDescription());
		assertEquals("", peakMSD.getQuantifierDescription());
		Set<IIdentificationTarget> peakTargets = peakMSD.getTargets();
		assertEquals(1, peakTargets.size());
		IPeakMassSpectrum massSpectrum = peakMSD.getPeakModel().getPeakMassSpectrum();
		assertEquals(47, massSpectrum.getNumberOfIons());
		assertEquals(16.0d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(143.81145f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(207.0d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(60.937057f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(57.1d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(6787.7783f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(853916, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertEquals(1, massSpectrum.getTimeSegmentId());
		assertEquals(1, massSpectrum.getCycleNumber());
	}
}
