/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.core.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.eclipse.chemclipse.numeric.equations.Equations;
import org.eclipse.chemclipse.numeric.equations.LinearEquation;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak exceptions.
 */
public class PeakBuilder_28_Test {

	private ITotalScanSignals totalIonSignals;
	private IChromatogramMSD chromatogram;
	private IRegularMassSpectrum massSpectrum;
	private IIon defaultIon;
	private LinearEquation backgroundEquation;
	private IMarkedIons excludedIons;
	private ITotalScanSignalExtractor totalIonSignalExtractor;

	@Before
	public void setUp() throws Exception {

		/*
		 * chromatogram
		 */
		List<Float> intensities = new ArrayList<Float>();
		intensities.add(1000.0f);
		intensities.add(5578.14f);
		intensities.add(7596.27f);
		intensities.add(9386.37f);
		intensities.add(5000.0f);
		intensities.add(2709.21f);
		intensities.add(1440.9f);
		intensities.add(810.72f);
		intensities.add(538.22f);
		intensities.add(400.00f);
		chromatogram = new ChromatogramMSD();
		chromatogram.setScanDelay(500);
		chromatogram.setScanInterval(1000);
		for(int scan = 1; scan <= 10; scan++) {
			massSpectrum = new VendorMassSpectrum();
			for(int ion = 32; ion <= 38; ion++) {
				defaultIon = new Ion(ion, ion * scan * intensities.get(scan - 1));
				massSpectrum.addIon(defaultIon);
			}
			chromatogram.addScan(massSpectrum);
		}
		chromatogram.recalculateRetentionTimes();
		/*
		 * Total ion signals.
		 */
		totalIonSignalExtractor = new TotalScanSignalExtractor(chromatogram);
		totalIonSignals = totalIonSignalExtractor.getTotalScanSignals();
		/*
		 * background equation
		 */
		IPoint p1 = new Point(10, 0);
		IPoint p2 = new Point(100, 0);
		backgroundEquation = Equations.createLinearEquation(p1, p2);
		/*
		 * Excluded ions
		 */
		excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
	}

	@Test
	public void testGetPeakIntensityValues_1() {

		IPeakMassSpectrum peakMassSpectrum;
		peakMassSpectrum = PeakBuilderMSD.getPeakMassSpectrum(chromatogram, totalIonSignals, backgroundEquation, excludedIons);
		assertNotNull(peakMassSpectrum);
		assertEquals("Total Signal", 9198643.0f, peakMassSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetPeakIntensityValues_2() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.getPeakMassSpectrum(null, totalIonSignals, backgroundEquation, excludedIons);
		});
	}

	@Test
	public void testGetPeakIntensityValues_3() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.getPeakMassSpectrum(chromatogram, null, backgroundEquation, excludedIons);
		});
	}

	@Test
	public void testGetPeakIntensityValues_4() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.getPeakMassSpectrum(chromatogram, totalIonSignals, null, excludedIons);
		});
	}

	@Test
	public void testGetPeakIntensityValues_5() {

		PeakBuilderMSD.getPeakMassSpectrum(chromatogram, totalIonSignals, backgroundEquation, null);
	}
}
