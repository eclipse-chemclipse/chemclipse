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

import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.eclipse.chemclipse.model.support.BackgroundAbundanceRange;
import org.eclipse.chemclipse.model.support.IBackgroundAbundanceRange;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.PeakMassSpectrum;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak exceptions.
 */
public class PeakBuilder_17_Test {

	private IScanRange scanRange;
	private IMarkedIons excludedIons;
	private IBackgroundAbundanceRange backgroundAbundanceRange;
	private IPeakMassSpectrum peakMassSpectrum;
	private ITotalScanSignals totalIonSignals;
	private IChromatogramMSD chromatogram;

	@Before
	public void setUp() throws Exception {

		scanRange = new ScanRange(1, 20);
		excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
		backgroundAbundanceRange = new BackgroundAbundanceRange(20.0f, 40.0f);
		peakMassSpectrum = new PeakMassSpectrum();
		totalIonSignals = new TotalScanSignals(200);
		chromatogram = new ChromatogramMSD();
	}

	@Test
	public void testCreatePeak_2() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(chromatogram, null, true);
		});
	}

	@Test
	public void testCreatePeak_3() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(null, scanRange, backgroundAbundanceRange, true);
		});
	}

	@Test
	public void testCreatePeak_4() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(chromatogram, null, backgroundAbundanceRange, true);
		});
	}

	@Test
	public void testCreatePeak_5() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(null, scanRange, excludedIons);
		});
	}

	@Test
	public void testCreatePeak_6() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(chromatogram, null, excludedIons);
		});
	}

	@Test
	public void testCreatePeak_7() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(null, totalIonSignals, peakMassSpectrum);
		});
	}

	@Test
	public void testCreatePeak_8() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(chromatogram, null, peakMassSpectrum);
		});
	}

	@Test
	public void testCreatePeak_9() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(chromatogram, totalIonSignals, null);
		});
	}

	@Test
	public void testCreatePeak_10() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(null, scanRange, backgroundAbundanceRange, excludedIons);
		});
	}

	@Test
	public void testCreatePeak_11() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(chromatogram, null, backgroundAbundanceRange, excludedIons);
		});
	}

	@Test
	public void testCreatePeak_12() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(chromatogram, scanRange, null, excludedIons);
		});
	}

	@Test
	public void testCreatePeak_13() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(chromatogram, scanRange, backgroundAbundanceRange, null);
		});
	}

	@Test
	public void testCreatePeak_14() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(null, totalIonSignals, peakMassSpectrum, backgroundAbundanceRange);
		});
	}

	@Test
	public void testCreatePeak_15() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(chromatogram, null, peakMassSpectrum, backgroundAbundanceRange);
		});
	}

	@Test
	public void testCreatePeak_16() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(chromatogram, totalIonSignals, null, backgroundAbundanceRange);
		});
	}

	@Test
	public void testCreatePeak_17() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(chromatogram, totalIonSignals, peakMassSpectrum, null);
		});
	}
}
