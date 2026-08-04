/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
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
import org.eclipse.chemclipse.msd.model.implementation.RegularMassSpectrum;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Test the peak exceptions.
 */
@TestInstance(Lifecycle.PER_CLASS)
public class PeakBuilder_24_Test {

	private ITotalScanSignals totalIonSignals;
	private IScanRange scanRange;
	private IChromatogramMSD chromatogram;
	private IMarkedTraces<ITrace> excludedIons;

	@BeforeAll
	public void setUp() {

		scanRange = new ScanRange(1, 10);
		chromatogram = new ChromatogramMSD();
		chromatogram.setScanDelay(500);
		chromatogram.setScanInterval(1000);
		for(int scan = 1; scan <= 10; scan++) {
			IRegularMassSpectrum massSpectrum = new RegularMassSpectrum();
			for(int ion = 32; ion <= 38; ion++) {
				IIon defaultIon = new Ion(ion, ion * 5);
				massSpectrum.addIon(defaultIon);
			}
			chromatogram.addScan(massSpectrum);
		}
		chromatogram.recalculateRetentionTimes();
		excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);
		excludedIons.add(new TraceNominalMSD(34));
		excludedIons.add(new TraceNominalMSD(36));
	}

	@Test
	public void testGetTotalIonSignals_1() {

		totalIonSignals = PeakBuilderMSD.getTotalIonSignals(chromatogram, scanRange, excludedIons);
		assertNotNull(totalIonSignals);
		ITotalScanSignal totalIonSignal = totalIonSignals.getTotalScanSignal(1);
		assertEquals(875.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(10);
		assertEquals(875.0f, totalIonSignal.getTotalSignal(), 0);
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