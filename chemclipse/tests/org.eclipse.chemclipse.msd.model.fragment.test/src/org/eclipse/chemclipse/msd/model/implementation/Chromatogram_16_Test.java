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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.util.MarkedTracesSupportMSD;
import org.eclipse.chemclipse.msd.model.xic.ITotalIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.TotalIonSignalExtractor;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Add 100 scans and get an {@link ITotalScanSignals} object.
 */
public class Chromatogram_16_Test {

	private ChromatogramMSD chromatogram;
	private ITotalScanSignals signals;
	private IMarkedTraces<ITrace> excludedIons;
	private ITotalIonSignalExtractor totalIonSignalExtractor;

	@BeforeEach
	public void setUp() {

		chromatogram = new ChromatogramMSD();
		// ------------------------------Scan 1-100
		for(int i = 1; i <= 100; i++) {
			IRegularMassSpectrum supplierMassSpectrum = new RegularMassSpectrum();
			supplierMassSpectrum.setRetentionTime(i);
			for(int j = 1; j <= 50; j++) {
				IIon ion = new Ion(j, j);
				supplierMassSpectrum.addIon(ion);
			}
			chromatogram.addScan(supplierMassSpectrum);
		}
		// ------------------------------Scan 1-100
		excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);

		totalIonSignalExtractor = new TotalIonSignalExtractor(chromatogram);
	}

	@Test
	public void testGetNumberOfScans_1() {

		assertEquals(100, chromatogram.getNumberOfScans());
	}

	@Test
	public void testGetStartRetentionTime_1() {

		assertEquals(1, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testGetStopRetentionTime_1() {

		assertEquals(100, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testGetRange_1() {

		signals = totalIonSignalExtractor.getTotalScanSignals();
		assertEquals(100, signals.size());
		assertEquals(1, signals.getStartScan());
		assertEquals(100, signals.getStopScan());
	}

	@Test
	public void testGetTotalIonSignals_1() {

		MarkedTracesSupportMSD.add(excludedIons, 1, 50);
		signals = totalIonSignalExtractor.getTotalIonSignals(excludedIons);
		assertEquals(0.0f, signals.getTotalScanSignal(1).getTotalSignal(), 0);
		assertEquals(0.0f, signals.getTotalScanSignal(20).getTotalSignal(), 0);
		assertEquals(0.0f, signals.getTotalScanSignal(100).getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignals_2() {

		MarkedTracesSupportMSD.add(excludedIons, 26, 50);
		signals = totalIonSignalExtractor.getTotalIonSignals(excludedIons);
		assertEquals(325.0f, signals.getTotalScanSignal(1).getTotalSignal(), 0);
		assertEquals(325.0f, signals.getTotalScanSignal(20).getTotalSignal(), 0);
		assertEquals(325.0f, signals.getTotalScanSignal(100).getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignals_3() {

		excludedIons.add(new TraceNominalMSD(26));
		signals = totalIonSignalExtractor.getTotalIonSignals(excludedIons);
		assertEquals(1249.0f, signals.getTotalScanSignal(1).getTotalSignal(), 0);
		assertEquals(1249.0f, signals.getTotalScanSignal(20).getTotalSignal(), 0);
		assertEquals(1249.0f, signals.getTotalScanSignal(100).getTotalSignal(), 0);
	}
}