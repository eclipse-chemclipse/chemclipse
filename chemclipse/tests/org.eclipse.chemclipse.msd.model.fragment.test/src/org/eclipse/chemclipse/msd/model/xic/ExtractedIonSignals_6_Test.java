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
package org.eclipse.chemclipse.msd.model.xic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ExtractedIonSignals_6_Test {

	private IExtractedIonSignals extractedIonSignals;
	private ITotalScanSignals totalIonSignals;
	private ITotalScanSignal totalIonSignal;
	private IScanMSD massSpectrum;
	private IMarkedTraces<ITrace> excludedIons;

	@BeforeAll
	public void setUp() {

		int scans = 100;
		int ionStart = 25;
		int ionStop = 30;
		IChromatogramMSD chromatogram = new ChromatogramMSD();
		extractedIonSignals = new ExtractedIonSignals(scans, chromatogram);
		/*
		 * Add 100 scans with scans of 6 ions.
		 */
		for(int scan = 1; scan <= scans; scan++) {
			IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(ionStart, ionStop);
			extractedIonSignal.setRetentionTime(scan);
			extractedIonSignal.setRetentionIndex(scan / 60.0f);
			for(int ion = ionStart; ion <= ionStop; ion++) {
				extractedIonSignal.setAbundance(ion, ion * scan);
			}
			extractedIonSignals.add(extractedIonSignal);
		}
	}

	@Test
	public void testGetTotalIonSignals_1() {

		IScanRange scanRange = new ScanRange(26, 100);
		totalIonSignals = extractedIonSignals.getTotalIonSignals((int)IIon.TIC_ION, scanRange);
		totalIonSignal = totalIonSignals.getTotalScanSignal(1);
		assertNull(totalIonSignal);
		totalIonSignal = totalIonSignals.getTotalScanSignal(100);
		assertNotNull(totalIonSignal);
		assertEquals(16500.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(26);
		assertNotNull(totalIonSignal);
		assertEquals(4290.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(87);
		assertNotNull(totalIonSignal);
		assertEquals(14355.0f, totalIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignals_2() {

		totalIonSignals = extractedIonSignals.getTotalIonSignals();
		totalIonSignal = totalIonSignals.getTotalScanSignal(1);
		assertNotNull(totalIonSignal);
		assertEquals(165.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(100);
		assertNotNull(totalIonSignal);
		assertEquals(16500.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(26);
		assertNotNull(totalIonSignal);
		assertEquals(4290.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(87);
		assertNotNull(totalIonSignal);
		assertEquals(14355.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(101);
		assertNull(totalIonSignal);
	}

	@Test
	public void testGetTotalIonSignals_3() {

		IScanRange scanRange = new ScanRange(26, 87);
		totalIonSignals = extractedIonSignals.getTotalIonSignals(scanRange);
		totalIonSignal = totalIonSignals.getTotalScanSignal(1);
		assertNull(totalIonSignal);
		totalIonSignal = totalIonSignals.getTotalScanSignal(100);
		assertNull(totalIonSignal);
		totalIonSignal = totalIonSignals.getTotalScanSignal(26);
		assertNotNull(totalIonSignal);
		assertEquals(4290.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(87);
		assertNotNull(totalIonSignal);
		assertEquals(14355.0f, totalIonSignal.getTotalSignal(), 0);
		totalIonSignal = totalIonSignals.getTotalScanSignal(101);
		assertNull(totalIonSignal);
	}

	@Test
	public void testGetTotalIonSignals_4() {

		totalIonSignals = extractedIonSignals.getTotalIonSignals(null);
	}

	@Test
	public void testGetScan_1() {

		excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);
		massSpectrum = extractedIonSignals.getScan(26, excludedIons);
		assertEquals(6, massSpectrum.getIons().size());
		assertEquals(4290.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetScan_2() {

		massSpectrum = extractedIonSignals.getScan(26);
		assertEquals(6, massSpectrum.getIons().size());
		assertEquals(4290.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetScan_3() {

		excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);
		excludedIons.add(new TraceNominalMSD(25));
		excludedIons.add(new TraceNominalMSD(30));
		massSpectrum = extractedIonSignals.getScan(26, excludedIons);
		assertEquals(4, massSpectrum.getIons().size());
		assertEquals(2860.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetScan_4() {

		excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);
		excludedIons.add(new TraceNominalMSD(25));
		excludedIons.add(new TraceNominalMSD(26));
		excludedIons.add(new TraceNominalMSD(27));
		excludedIons.add(new TraceNominalMSD(28));
		excludedIons.add(new TraceNominalMSD(30));
		massSpectrum = extractedIonSignals.getScan(26, excludedIons);
		assertEquals(1, massSpectrum.getIons().size());
		assertEquals(754.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetScan_5() {

		excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);
		excludedIons.add(new TraceNominalMSD(25));
		excludedIons.add(new TraceNominalMSD(26));
		excludedIons.add(new TraceNominalMSD(27));
		excludedIons.add(new TraceNominalMSD(28));
		excludedIons.add(new TraceNominalMSD(29));
		excludedIons.add(new TraceNominalMSD(30));
		massSpectrum = extractedIonSignals.getScan(26, excludedIons);
		assertEquals(0, massSpectrum.getIons().size());
		assertEquals(0.0f, massSpectrum.getTotalSignal(), 0);
	}
}