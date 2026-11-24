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
package org.eclipse.chemclipse.model.selection;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.implementation.Chromatogram;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Covers {@link AbstractChromatogramSelection#setRanges(int, int, float, float, boolean)}
 * on a chromatogram with scans from 500 ms to 2500 ms.
 */
public class ChromatogramSelection_1_Test {

	private IChromatogram chromatogram;
	private IChromatogramSelection chromatogramSelection;

	@BeforeEach
	public void setUp() {

		chromatogram = new Chromatogram();
		addScan(500, 2898.2f);
		addScan(1000, 7837.2f);
		addScan(1500, 128900.48f);
		addScan(2000, 289830.65f);
		addScan(2500, 3983.0f);
		chromatogramSelection = new ChromatogramSelection(chromatogram, false);
	}

	@Test
	public void testWithoutValidationKeepsValuesInRange() {

		chromatogramSelection.setRanges(1000, 2000, 10.0f, 20.0f, false);
		assertSelection(1000, 2000, 10.0f, 20.0f);
	}

	@Test
	public void testWithoutValidationKeepsRetentionTimesOutOfRange() {

		chromatogramSelection.setRanges(100, 9000, 10.0f, 20.0f, false);
		assertSelection(100, 9000, 10.0f, 20.0f);
	}

	@Test
	public void testWithoutValidationKeepsInvertedRetentionTimes() {

		chromatogramSelection.setRanges(2000, 1000, 10.0f, 20.0f, false);
		assertSelection(2000, 1000, 10.0f, 20.0f);
	}

	@Test
	public void testWithoutValidationKeepsInvalidAbundances() {

		chromatogramSelection.setRanges(1000, 2000, Float.NaN, Float.POSITIVE_INFINITY, false);
		assertSelection(1000, 2000, Float.NaN, Float.POSITIVE_INFINITY);
	}

	@Test
	public void testValidationKeepsValuesInRange() {

		chromatogramSelection.setRanges(1000, 2000, 10.0f, 20.0f, true);
		assertSelection(1000, 2000, 10.0f, 20.0f);
	}

	@Test
	public void testValidationKeepsChromatogramBorders() {

		chromatogramSelection.setRanges(500, 2500, 10.0f, 20.0f, true);
		assertSelection(500, 2500, 10.0f, 20.0f);
	}

	@Test
	public void testValidationResetsStartBeforeChromatogram() {

		chromatogramSelection.setRanges(100, 2000, 10.0f, 20.0f, true);
		assertSelection(500, 2000, 10.0f, 20.0f);
	}

	@Test
	public void testValidationResetsStopAfterChromatogram() {

		chromatogramSelection.setRanges(1000, 9000, 10.0f, 20.0f, true);
		assertSelection(1000, 2500, 10.0f, 20.0f);
	}

	@Test
	public void testValidationResetsBothRetentionTimesOutOfRange() {

		chromatogramSelection.setRanges(100, 9000, 10.0f, 20.0f, true);
		assertSelection(500, 2500, 10.0f, 20.0f);
	}

	@Test
	public void testValidationResetsNonPositiveRetentionTimes() {

		chromatogramSelection.setRanges(0, -1, 10.0f, 20.0f, true);
		assertSelection(500, 2500, 10.0f, 20.0f);
	}

	@Test
	public void testValidationResetsStartOfInvertedRetentionTimes() {

		/*
		 * The start is checked against the new stop first, fails and falls back
		 * to the chromatogram start. The stop is then valid against that start.
		 */
		chromatogramSelection.setRanges(2000, 1000, 10.0f, 20.0f, true);
		assertSelection(500, 1000, 10.0f, 20.0f);
	}

	@Test
	public void testValidationResetsNaNAbundances() {

		chromatogramSelection.setRanges(1000, 2000, Float.NaN, Float.NaN, true);
		assertSelection(1000, 2000, 0.0f, chromatogram.getMaxSignal());
	}

	@Test
	public void testValidationResetsInfiniteAbundances() {

		chromatogramSelection.setRanges(1000, 2000, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, true);
		assertSelection(1000, 2000, 0.0f, chromatogram.getMaxSignal());
	}

	@Test
	public void testSetRangeRetentionTimeKeepsAbundances() {

		chromatogramSelection.setRanges(1000, 2000, 10.0f, 20.0f, false);
		chromatogramSelection.setRangeRetentionTime(100, 9000, true);
		assertSelection(500, 2500, 10.0f, 20.0f);
	}

	private void assertSelection(int startRetentionTime, int stopRetentionTime, float startAbundance, float stopAbundance) {

		assertEquals(startRetentionTime, chromatogramSelection.getStartRetentionTime(), "start retention time");
		assertEquals(stopRetentionTime, chromatogramSelection.getStopRetentionTime(), "stop retention time");
		assertEquals(startAbundance, chromatogramSelection.getStartAbundance(), "start abundance");
		assertEquals(stopAbundance, chromatogramSelection.getStopAbundance(), "stop abundance");
	}

	private void addScan(int retentionTime, float intensity) {

		IScan scan = new Scan(intensity);
		scan.setRetentionTime(retentionTime);
		chromatogram.addScan(scan);
	}
}
