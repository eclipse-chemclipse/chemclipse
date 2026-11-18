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
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.chromatogram.msd.filter.impl.settings.CoverageReference;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.junit.jupiter.api.Test;

public class IonChannelGapFiller_1_Test {

	/**
	 * One scan per entry: m/z 29.1 with the given abundance (0 = missing), plus m/z 43.4 at 500 in every
	 * scan as an untouched, non-nominal neighbour.
	 */
	static ChromatogramMSD chromatogram(double... abundances29) {

		ChromatogramMSD chromatogram = new ChromatogramMSD();
		for(int i = 0; i < abundances29.length; i++) {
			List<IIon> ions = new ArrayList<>();
			if(abundances29[i] > 0) {
				ions.add(new Ion(29.1, (float)abundances29[i]));
			}
			ions.add(new Ion(43.4, 500));
			IScanMSD scan = new ScanMSD(ions);
			scan.setRetentionTime(1000 * (i + 1));
			chromatogram.addScan(scan);
		}
		chromatogram.setDirty(false);
		return chromatogram;
	}

	static float abundance(ChromatogramMSD chromatogram, int scanIndex, int mz) {

		return ((IScanMSD)chromatogram.getScans().get(scanIndex)).getExtractedIonSignal().getAbundance(mz);
	}

	/**
	 * Default settings: 50 % from the first signal, short gaps up to 2.
	 */
	private static IonChannelGapFiller.Result fill(ChromatogramMSD chromatogram, int maxShortGap) {

		return IonChannelGapFiller.fill(chromatogram, 50, CoverageReference.FIRST_SIGNAL, maxShortGap);
	}

	@Test
	public void testSingleGapFilled() {

		ChromatogramMSD chromatogram = chromatogram(4000, 0, 6000);
		IonChannelGapFiller.Result result = fill(chromatogram, 2);
		assertEquals(1, result.filled());
		assertEquals(Map.of(29, 1), result.filledPerIon());
		assertFalse(result.highResolution());
		assertEquals(5000.0f, abundance(chromatogram, 1, 29), 0.01f);
		assertTrue(chromatogram.isDirty());
	}

	@Test
	public void testGapOfTwoFilledLinearly() {

		ChromatogramMSD chromatogram = chromatogram(3000, 0, 0, 6000);
		assertEquals(2, fill(chromatogram, 2).filled());
		assertEquals(4000.0f, abundance(chromatogram, 1, 29), 0.01f);
		assertEquals(5000.0f, abundance(chromatogram, 2, 29), 0.01f);
	}

	@Test
	public void testLongerGapKeptInSparseChannel() {

		// m/z 29 in 2 of 5 scans: below 50 %, so only gaps up to maxShortGap are filled.
		ChromatogramMSD chromatogram = chromatogram(3000, 0, 0, 0, 6000);
		assertEquals(0, fill(chromatogram, 2).filled());
		assertEquals(0.0f, abundance(chromatogram, 2, 29), 0.0f);
		assertFalse(chromatogram.isDirty());
		assertEquals(3, fill(chromatogram, 3).filled());
	}

	@Test
	public void testNoShortGapsInSparseChannel() {

		// 2 of 5 scans, sparse: maxShortGap 0 fills nothing, not even a single missing scan.
		ChromatogramMSD chromatogram = chromatogram(3000, 0, 6000, 0, 0);
		assertEquals(0, fill(chromatogram, 0).filled());
		assertEquals(1, fill(chromatogram, 1).filled());
	}

	@Test
	public void testLeadingAndTrailingZerosKept() {

		ChromatogramMSD chromatogram = chromatogram(0, 4000, 5000, 0);
		assertEquals(0, fill(chromatogram, 2).filled());
		assertEquals(0.0f, abundance(chromatogram, 0, 29), 0.0f);
		assertEquals(0.0f, abundance(chromatogram, 3, 29), 0.0f);
	}

	@Test
	public void testOtherIonsUntouched() {

		ChromatogramMSD chromatogram = chromatogram(4000, 0, 6000);
		fill(chromatogram, 2);
		for(int i = 0; i < 3; i++) {
			IScanMSD scan = (IScanMSD)chromatogram.getScans().get(i);
			assertEquals(2, scan.getNumberOfIons());
			assertEquals(500.0f, scan.getIon(43.4).getAbundance(), 0.0f);
		}
		// The existing ions keep their exact m/z, the added one is nominal.
		assertEquals(4000.0f, ((IScanMSD)chromatogram.getScans().get(0)).getIon(29.1).getAbundance(), 0.0f);
		assertEquals(5000.0f, ((IScanMSD)chromatogram.getScans().get(1)).getIon(29.0).getAbundance(), 0.01f);
	}

	@Test
	public void testRepeatedGaps() {

		// The comb seen in real data: every other scan missing.
		ChromatogramMSD chromatogram = chromatogram(1000, 0, 3000, 0, 5000, 0, 7000);
		assertEquals(3, fill(chromatogram, 2).filled());
		assertEquals(2000.0f, abundance(chromatogram, 1, 29), 0.01f);
		assertEquals(4000.0f, abundance(chromatogram, 3, 29), 0.01f);
		assertEquals(6000.0f, abundance(chromatogram, 5, 29), 0.01f);
	}

	@Test
	public void testAnyGapFilledInDenseChannel() {

		// m/z 29 in 6 of 10 scans: dense, the gap of 4 is filled although maxShortGap is 2.
		ChromatogramMSD chromatogram = chromatogram(1000, 2000, 0, 0, 0, 0, 7000, 8000, 9000, 10000);
		assertEquals(4, fill(chromatogram, 2).filled());
		assertEquals(3000.0f, abundance(chromatogram, 2, 29), 0.01f);
		assertEquals(6000.0f, abundance(chromatogram, 5, 29), 0.01f);
	}

	@Test
	public void testDenseExactlyAtMinCoverage() {

		// 3 of 6 scans: exactly 50 % counts as dense.
		assertEquals(3, fill(chromatogram(1000, 0, 0, 0, 5000, 6000), 1).filled());
		// 3 of 6 is below 51 %.
		assertEquals(0, IonChannelGapFiller.fill(chromatogram(1000, 0, 0, 0, 5000, 6000), 51, CoverageReference.FIRST_SIGNAL, 1).filled());
	}

	@Test
	public void testCoverageReference() {

		// No m/z 29 in the first 6 scans, then 4 of 7: 31 % over the whole range, 57 % from the first
		// signal on.
		double[] abundances = {0, 0, 0, 0, 0, 0, 1000, 0, 0, 0, 5000, 6000, 7000};
		ChromatogramMSD fromFirstSignal = chromatogram(abundances);
		assertEquals(3, IonChannelGapFiller.fill(fromFirstSignal, 50, CoverageReference.FIRST_SIGNAL, 1).filled());
		assertEquals(2000.0f, abundance(fromFirstSignal, 7, 29), 0.01f);
		assertEquals(4000.0f, abundance(fromFirstSignal, 9, 29), 0.01f);
		assertEquals(0.0f, abundance(fromFirstSignal, 5, 29), 0.0f);
		// Whole range: sparse, the gap of 3 is longer than maxShortGap 1.
		assertEquals(0, IonChannelGapFiller.fill(chromatogram(abundances), 50, CoverageReference.WHOLE_RANGE, 1).filled());
		// null = from the first signal.
		assertEquals(3, IonChannelGapFiller.fill(chromatogram(abundances), 50, null, 1).filled());
	}

	@Test
	public void testZeroCoverageFillsEveryGap() {

		// 2 of 10 scans, a gap of 8: filled with 0 % (the former behaviour of the filter).
		ChromatogramMSD chromatogram = chromatogram(1000, 0, 0, 0, 0, 0, 0, 0, 0, 10000);
		assertEquals(8, IonChannelGapFiller.fill(chromatogram, 0, CoverageReference.FIRST_SIGNAL, 0).filled());
		assertEquals(2000.0f, abundance(chromatogram, 1, 29), 0.01f);
	}

	@Test
	public void testHighResolutionUntouched() {

		// 30 ions within 1 m/z: more ions than nominal positions in the range.
		ChromatogramMSD chromatogram = new ChromatogramMSD();
		for(int i = 0; i < 3; i++) {
			List<IIon> ions = new ArrayList<>();
			for(int k = 0; k < 30; k++) {
				ions.add(new Ion(100.0 + k * 0.03, 1000));
			}
			if(i != 1) {
				ions.add(new Ion(105.0, 4000));
			}
			IScanMSD scan = new ScanMSD(ions);
			scan.setRetentionTime(1000 * (i + 1));
			chromatogram.addScan(scan);
		}
		chromatogram.setDirty(false);
		IonChannelGapFiller.Result result = IonChannelGapFiller.fill(chromatogram, 50, CoverageReference.FIRST_SIGNAL, 2);
		assertTrue(result.highResolution());
		assertEquals(0, result.filled());
		assertEquals(30, ((IScanMSD)chromatogram.getScans().get(1)).getNumberOfIons());
		assertFalse(chromatogram.isDirty());
	}

	@Test
	public void testNothingToDo() {

		assertEquals(0, fill(new ChromatogramMSD(), 2).filled());
		assertEquals(0, IonChannelGapFiller.fill((ChromatogramMSD)null, 50, CoverageReference.FIRST_SIGNAL, 2).filled());
		assertEquals(0, IonChannelGapFiller.fill((List<IScanMSD>)null, 50, CoverageReference.FIRST_SIGNAL, 2).filled());
	}

	@Test
	public void testDescribeChannels() {

		IonChannelGapFiller.Result result = new IonChannelGapFiller.Result(13, Map.of(29, 10, 31, 2, 74, 1), false);
		assertEquals("m/z 29: 10, m/z 31: 2 and 1 more channel(s)", result.describeChannels(2));
		assertEquals("m/z 29: 10, m/z 31: 2, m/z 74: 1", result.describeChannels(5));
	}
}
