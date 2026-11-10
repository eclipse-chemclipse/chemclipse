/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.model.core.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.support.BackgroundAbundanceRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramPeakWSD;
import org.eclipse.chemclipse.wsd.model.core.implementation.ChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.implementation.ScanSignalWSD;
import org.eclipse.chemclipse.wsd.model.core.implementation.ScanWSD;
import org.junit.jupiter.api.Test;

/**
 * Tests peaks with a negative baseline.
 */
public class PeakBuilderWSD_1_Test {

	private static final float HEIGHT = 5000.0f;
	private static final Set<Integer> WAVELENGTHS = Set.of(250, 300);
	/*
	 * The second baseline puts the whole peak below 0.
	 */
	private static final float[] BASELINES = {-1000.0f, -10000.0f};

	private ChromatogramWSD createChromatogram(float baseline) {

		ChromatogramWSD chromatogram = new ChromatogramWSD();
		for(int i = 1; i <= 21; i++) {
			double x = (i - 11) / 2.0d;
			float signal = baseline + (float)(HEIGHT * Math.exp(-0.5d * x * x));
			ScanWSD scan = new ScanWSD();
			scan.setRetentionTime(i * 1000);
			for(int wavelength : WAVELENGTHS) {
				scan.addScanSignal(new ScanSignalWSD(wavelength, signal / WAVELENGTHS.size()));
			}
			chromatogram.addScan(scan);
		}
		return chromatogram;
	}

	private void assertPeak(IChromatogramPeakWSD peak, float baseline) {

		IPeakModel peakModel = peak.getPeakModel();
		assertEquals(baseline, peakModel.getBackgroundAbundance(peakModel.getStartRetentionTime()), 1.0f);
		assertEquals(baseline, peakModel.getBackgroundAbundance(peakModel.getStopRetentionTime()), 1.0f);
		assertEquals(baseline, peakModel.getBackgroundAbundance(), 1.0f);
		assertEquals(11000, peakModel.getRetentionTimeAtPeakMaximum());
		assertEquals(HEIGHT, peakModel.getPeakAbundance(), 1.0f);
	}

	@Test
	public void testPeakIncludedBackground() {

		for(float baseline : BASELINES) {
			IChromatogramPeakWSD peak = PeakBuilderWSD.createPeak(createChromatogram(baseline), new ScanRange(1, 21), true);
			assertPeak(peak, baseline);
		}
	}

	@Test
	public void testBase() {

		for(float baseline : BASELINES) {
			IChromatogramPeakWSD peak = PeakBuilderWSD.createPeak(createChromatogram(baseline), new ScanRange(1, 21), false);
			assertPeak(peak, baseline);
		}
	}

	@Test
	public void testBackgroundAbundanceRange() {

		for(float baseline : BASELINES) {
			IChromatogramPeakWSD peak = PeakBuilderWSD.createPeak(createChromatogram(baseline), new ScanRange(1, 21), new BackgroundAbundanceRange(baseline, baseline, false), true);
			assertPeak(peak, baseline);
		}
	}

	@Test
	public void testIntensities() {

		for(float baseline : BASELINES) {
			IChromatogramPeakWSD peak = PeakBuilderWSD.createPeak(createChromatogram(baseline), new ScanRange(1, 21), baseline, baseline);
			assertPeak(peak, baseline);
		}
	}

	@Test
	public void testMarkedWavelengths() {

		for(float baseline : BASELINES) {
			/*
			 * No marked wavelengths, so the total signal is used.
			 */
			IChromatogramPeakWSD peak = PeakBuilderWSD.createPeak(createChromatogram(baseline), new ScanRange(1, 21), true, Set.of(), MarkedTraceModus.INCLUDE);
			assertPeak(peak, baseline);
		}
	}
}
