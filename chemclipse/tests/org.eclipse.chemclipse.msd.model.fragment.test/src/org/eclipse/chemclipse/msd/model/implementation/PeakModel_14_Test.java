/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.chemclipse.model.core.IPeakIntensityValues;
import org.eclipse.chemclipse.model.implementation.PeakIntensityValues;
import org.eclipse.chemclipse.msd.model.core.IPeakIon;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.junit.Before;
import org.junit.Test;

public class PeakModel_14_Test {

	private IPeakModelMSD peakModel;
	private IPeakMassSpectrum peakMaximum;
	private IPeakIon ion;
	private TreeMap<Float, Float> fragmentValues;
	private IPeakIntensityValues intensityValues;
	private TreeMap<Integer, Float> scanValues;
	private float startBackgroundAbundance = 100.0f;
	private float stopBackgroundAbundance = 100.0f;

	@Before
	public void setUp() {

		// ----------------------PeakMaximum
		peakMaximum = new PeakMassSpectrum();
		fragmentValues = new TreeMap<Float, Float>();
		fragmentValues.put(104.0f, 230000.0f);
		fragmentValues.put(103.0f, 58000.0f);
		fragmentValues.put(51.0f, 26000.0f);
		fragmentValues.put(50.0f, 48000.0f);
		fragmentValues.put(78.0f, 23600.0f);
		fragmentValues.put(77.0f, 2500.0f);
		fragmentValues.put(74.0f, 38000.0f);
		fragmentValues.put(105.0f, 97000.0f);
		for(Entry<Float, Float> entry : fragmentValues.entrySet()) {
			ion = new PeakIon(entry.getKey(), entry.getValue());
			peakMaximum.addIon(ion);
		}
		// ----------------------PeakMaximum
		// ----------------------IntensityValues
		intensityValues = new PeakIntensityValues();
		scanValues = new TreeMap<Integer, Float>();
		scanValues.put(4500, 0.0f);
		scanValues.put(5500, 20.0f);
		scanValues.put(6500, 100.0f);
		scanValues.put(7500, 80.0f);
		scanValues.put(8500, 60.0f);
		scanValues.put(9500, 40.0f);
		scanValues.put(10500, 20.0f);
		scanValues.put(11500, 0.0f);
		for(Entry<Integer, Float> entry : scanValues.entrySet()) {
			intensityValues.addIntensityValue(entry.getKey(), entry.getValue());
		}
		// ----------------------IntensityValues
		peakModel = new PeakModelMSD(peakMaximum, intensityValues, startBackgroundAbundance, stopBackgroundAbundance);
	}

	@Test
	public void testRetentionTimes_1() {

		/*
		 * Fails, size does not match.
		 */
		List<Integer> retentionTimesNew = new ArrayList<Integer>();
		retentionTimesNew.add(1000);
		retentionTimesNew.add(2000);
		retentionTimesNew.add(3000);
		retentionTimesNew.add(4000);
		retentionTimesNew.add(5000);
		retentionTimesNew.add(6000);
		retentionTimesNew.add(7000);
		retentionTimesNew.add(8000);
		peakModel.replaceRetentionTimes(retentionTimesNew);

		int rt;
		List<Integer> retentionTimes = peakModel.getRetentionTimes();
		rt = retentionTimes.get(0);
		assertEquals("retentionTime", 1000, rt);
		rt = retentionTimes.get(2);
		assertEquals("retentionTime", 3000, rt);
		rt = retentionTimes.get(5);
		assertEquals("retentionTime", 6000, rt);
		rt = retentionTimes.get(7);
		assertEquals("retentionTime", 8000, rt);
	}
}
