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

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak exceptions.
 */
public class PeakBuilder_24_Test {

	private ITotalScanSignals totalIonSignals;
	private ITotalScanSignal totalIonSignal;
	private IScanRange scanRange;
	private IChromatogramMSD chromatogram;
	private IRegularMassSpectrum massSpectrum;
	private IIon defaultIon;
	private IMarkedIons excludedIons;

	@Before
	public void setUp() throws Exception {

		scanRange = new ScanRange(1, 10);
		chromatogram = new ChromatogramMSD();
		chromatogram.setScanDelay(500);
		chromatogram.setScanInterval(1000);
		for(int scan = 1; scan <= 10; scan++) {
			massSpectrum = new VendorMassSpectrum();
			for(int ion = 32; ion <= 38; ion++) {
				defaultIon = new Ion(ion, ion * 5);
				massSpectrum.addIon(defaultIon);
			}
			chromatogram.addScan(massSpectrum);
		}
		chromatogram.recalculateRetentionTimes();
		excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
		excludedIons.add(new MarkedIon(34));
		excludedIons.add(new MarkedIon(36));
	}

	@Test
	public void testGetTotalIonSignals_1() {

		totalIonSignals = PeakBuilderMSD.getTotalIonSignals(chromatogram, scanRange, excludedIons);
		assertNotNull(totalIonSignals);
		totalIonSignal = totalIonSignals.getTotalScanSignal(1);
		assertEquals("Signal", 875.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(10);
		assertEquals("Signal", 875.0f, totalIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignals_2() {

		assertThrows(PeakException.class, () -> {
			totalIonSignals = PeakBuilderMSD.getTotalIonSignals(null, scanRange, excludedIons);
		});
	}

	@Test
	public void testGetTotalIonSignals_3() {

		assertThrows(PeakException.class, () -> {
			totalIonSignals = PeakBuilderMSD.getTotalIonSignals(chromatogram, null, excludedIons);
		});
	}

	@Test
	public void testGetTotalIonSignals_4() {

		assertThrows(PeakException.class, () -> {
			totalIonSignals = PeakBuilderMSD.getTotalIonSignals(chromatogram, scanRange, null);
		});
	}
}
