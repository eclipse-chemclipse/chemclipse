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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.chemclipse.model.core.IPeakIntensityValues;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.implementation.PeakIntensityValues;
import org.eclipse.chemclipse.msd.model.core.IPeakIon;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.junit.Before;
import org.junit.Test;

public class PeakModel_3_Test {

	private IPeakModelMSD peakModel;
	private IPeakMassSpectrum peakMaximum;
	private IPeakIon ion;
	private TreeMap<Float, Float> fragmentValues;
	private IPeakIntensityValues intensityValues;
	private TreeMap<Integer, Float> scanValues;
	private float startBackgroundAbundance = 0.0f;
	private float stopBackgroundAbundance = 0.0f;

	@Before
	public void setUp() {

		// ----------------------PeakMaximum
		peakMaximum = new PeakMassSpectrum();
		fragmentValues = new TreeMap<Float, Float>();
		fragmentValues.put(104.0f, 2300.0f);
		fragmentValues.put(103.0f, 580.0f);
		fragmentValues.put(51.0f, 260.0f);
		fragmentValues.put(50.0f, 480.0f);
		fragmentValues.put(78.0f, 236.0f);
		fragmentValues.put(77.0f, 25.0f);
		fragmentValues.put(74.0f, 380.0f);
		fragmentValues.put(105.0f, 970.0f);
		for(Entry<Float, Float> entry : fragmentValues.entrySet()) {
			ion = new PeakIon(entry.getKey(), entry.getValue());
			peakMaximum.addIon(ion);
		}
		// ----------------------PeakMaximum
		// ----------------------BackgroundValues
		startBackgroundAbundance = 360.0f;
		stopBackgroundAbundance = 420.5f;
		// ----------------------BackgroundValues
	}

	@Test
	public void testConstruct_1() {

		// ----------------------IntensityValues
		intensityValues = new PeakIntensityValues();
		scanValues = new TreeMap<Integer, Float>();
		scanValues.put(1500, 0.0f);
		scanValues.put(9500, 100.0f);
		scanValues.put(10500, 86.0f);
		for(Entry<Integer, Float> entry : scanValues.entrySet()) {
			intensityValues.addIntensityValue(entry.getKey(), entry.getValue());
		}
		// ----------------------IntensityValues
		peakModel = new PeakModelMSD(peakMaximum, intensityValues, startBackgroundAbundance, stopBackgroundAbundance);
		assertNotNull("The construction was fine.", peakModel);
	}

	@Test
	public void testConstruct_2() {

		// ----------------------IntensityValues
		intensityValues = new PeakIntensityValues();
		scanValues = new TreeMap<>();
		scanValues.put(9500, 100.0f);
		scanValues.put(10500, 86.0f);
		for(Entry<Integer, Float> entry : scanValues.entrySet()) {
			intensityValues.addIntensityValue(entry.getKey(), entry.getValue());
		}
		// ----------------------IntensityValues
		assertThrows(IllegalArgumentException.class, () -> {
			peakModel = new PeakModelMSD(peakMaximum, intensityValues, startBackgroundAbundance, stopBackgroundAbundance);
		});
	}

	@Test
	public void testConstruct_3() {

		// ----------------------IntensityValues
		intensityValues = new PeakIntensityValues();
		scanValues = new TreeMap<>();
		scanValues.put(10500, 86.0f);
		for(Entry<Integer, Float> entry : scanValues.entrySet()) {
			intensityValues.addIntensityValue(entry.getKey(), entry.getValue());
		}
		// ----------------------IntensityValues
		assertThrows(IllegalArgumentException.class, () -> {
			peakModel = new PeakModelMSD(peakMaximum, intensityValues, startBackgroundAbundance, stopBackgroundAbundance);
		});
	}

	@Test
	public void testConstruct_4() {

		// ----------------------IntensityValues
		intensityValues = new PeakIntensityValues();
		// ----------------------IntensityValues
		assertThrows(IllegalArgumentException.class, () -> {
			peakModel = new PeakModelMSD(peakMaximum, intensityValues, startBackgroundAbundance, stopBackgroundAbundance);
		});
	}

	@Test
	public void testConstruct_5() {

		// ----------------------IntensityValues
		intensityValues = new PeakIntensityValues();
		/*
		 * There must be at least one value with an intensity of 100.0f
		 * (IPeakIntensityValues.MAX_INTENSITY).
		 */
		scanValues = new TreeMap<>();
		scanValues.put(1500, 0.0f);
		scanValues.put(9500, 99.0f);
		scanValues.put(10500, 86.0f);
		for(Entry<Integer, Float> entry : scanValues.entrySet()) {
			intensityValues.addIntensityValue(entry.getKey(), entry.getValue());
		}
		// ----------------------IntensityValues
		assertThrows(PeakException.class, () -> {
			peakModel = new PeakModelMSD(peakMaximum, intensityValues, startBackgroundAbundance, stopBackgroundAbundance);
		});
	}
}
