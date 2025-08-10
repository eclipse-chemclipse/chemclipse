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

import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak exceptions.
 */
public class PeakBuilder_30_Test {

	private IChromatogramMSD chromatogram;
	private IRegularMassSpectrum massSpectrum;
	private IIon defaultIon;
	private IExtractedIonSignals extractedIonSignals;
	private IScanRange scanRange;
	private IExtractedIonSignalExtractor extractedIonSignalExtractor;

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
		/*
		 * ScanRange
		 */
		scanRange = new ScanRange(1, 10);
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
		extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals();
	}

	@Test
	public void testGetPeakIntensityValues_1() {

		IChromatogramPeakMSD peak;
		peak = PeakBuilderMSD.createPeak(extractedIonSignals, scanRange);
		assertNotNull(peak);
		float totalSignal = peak.getPeakModel().getPeakMassSpectrum().getTotalSignal();
		assertEquals("TotalSignal", 8708643.0f, totalSignal, 0);
	}

	@Test
	public void testGetPeakIntensityValues_2() {

		extractedIonSignals = null;
		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(extractedIonSignals, scanRange);
		});
	}

	@Test
	public void testGetPeakIntensityValues_3() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.createPeak(extractedIonSignals, null);
		});
	}
}
