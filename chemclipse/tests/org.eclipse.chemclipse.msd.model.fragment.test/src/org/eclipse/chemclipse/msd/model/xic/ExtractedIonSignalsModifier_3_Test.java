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
package org.eclipse.chemclipse.msd.model.xic;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class ExtractedIonSignalsModifier_3_Test {

	private IChromatogramMSD chromatogram;
	private IRegularMassSpectrum supplierMassSpectrum;
	private IIon supplierIon;
	private IExtractedIonSignals extractedIonSignals;
	private IExtractedIonSignal extractedIonSignal;
	private List<Integer> scans;
	private List<Integer> ions;
	private IExtractedIonSignalExtractor extractedIonSignalExtractor;

	@Before
	public void setUp() throws NoExtractedIonSignalStoredException {

		/*
		 * Build a chromatogram and add scans with ions and no
		 * abundance.
		 */
		chromatogram = new ChromatogramMSD();
		for(int scan = 1; scan <= 100; scan++) {
			supplierMassSpectrum = new VendorMassSpectrum();
			for(int ion = 32; ion <= 104; ion++) {
				supplierIon = new Ion(ion, ion * 2);
				supplierMassSpectrum.addIon(supplierIon);
			}
			chromatogram.addScan(supplierMassSpectrum);
		}
		extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals();
		scans = new ArrayList<Integer>();
		scans.add(92);
		scans.add(94);
		scans.add(96);
		scans.add(98);
		ions = new ArrayList<Integer>();
		ions.add(45);
		ions.add(76);
		ions.add(102);
		/*
		 * Set some ions to 0 to test the adjustment method.
		 */
		for(int scan : scans) {
			extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
			for(int ion : ions) {
				extractedIonSignal.setAbundance(ion, 0.0f, true);
			}
		}
	}

	@Test
	public void testInitialization_1() throws NoExtractedIonSignalStoredException {

		/*
		 * Test that the values are zero.
		 */
		for(int scan : scans) {
			extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
			for(int ion : ions) {
				assertEquals("TotalIonSignal before", 0.0f, extractedIonSignal.getAbundance(ion), 0);
			}
		}
	}

	@Test
	public void testAdjustThresholdTransitions_1() throws NoExtractedIonSignalStoredException {

		ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
		/*
		 * Test the adjusted value.
		 */
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(92);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(45), 0);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(76), 0);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(102), 0);
	}

	@Test
	public void testAdjustThresholdTransitions_2() throws NoExtractedIonSignalStoredException {

		ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
		/*
		 * Test the adjusted value.
		 */
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(94);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(45), 0);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(76), 0);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(102), 0);
	}

	@Test
	public void testAdjustThresholdTransitions_3() throws NoExtractedIonSignalStoredException {

		ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
		/*
		 * Test the adjusted value.
		 */
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(96);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(45), 0);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(76), 0);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(102), 0);
	}

	@Test
	public void testAdjustThresholdTransitions_4() throws NoExtractedIonSignalStoredException {

		ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
		/*
		 * Test the adjusted value.
		 */
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(98);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(45), 0);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(76), 0);
		assertEquals("TotalIonSignal after", 40.477154f, extractedIonSignal.getAbundance(102), 0);
	}

	@Test
	public void testAdjustThresholdTransitions_5() throws NoExtractedIonSignalStoredException {

		ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
		/*
		 * Test the adjusted value.
		 */
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(93);
		assertEquals("TotalIonSignal after", 90.0f, extractedIonSignal.getAbundance(45), 0);
		assertEquals("TotalIonSignal after", 152.0f, extractedIonSignal.getAbundance(76), 0);
		assertEquals("TotalIonSignal after", 204.0f, extractedIonSignal.getAbundance(102), 0);
	}

	@Test
	public void testAdjustThresholdTransitions_6() throws NoExtractedIonSignalStoredException {

		ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
		/*
		 * Test the adjusted value.
		 */
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(99);
		assertEquals("TotalIonSignal after", 90.0f, extractedIonSignal.getAbundance(45), 0);
		assertEquals("TotalIonSignal after", 152.0f, extractedIonSignal.getAbundance(76), 0);
		assertEquals("TotalIonSignal after", 204.0f, extractedIonSignal.getAbundance(102), 0);
	}

	@Test
	public void testAdjustThresholdTransitions_7() throws NoExtractedIonSignalStoredException {

		ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
		/*
		 * Test the adjusted value.
		 */
		extractedIonSignal = extractedIonSignals.getExtractedIonSignal(100);
		assertEquals("TotalIonSignal after", 90.0f, extractedIonSignal.getAbundance(45), 0);
		assertEquals("TotalIonSignal after", 152.0f, extractedIonSignal.getAbundance(76), 0);
		assertEquals("TotalIonSignal after", 204.0f, extractedIonSignal.getAbundance(102), 0);
	}
}
