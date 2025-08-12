/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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

import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.chemclipse.model.core.IPeakIntensityValues;
import org.eclipse.chemclipse.model.implementation.PeakIntensityValues;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak intensity values.<br/>
 * Make sure that the limit IPeakIntensityValues.MAX_INTENSITY is implemented
 * correctly.
 */
public class PeakIntensityValues_7_Test {

	private PeakIntensityValues intensityValues;
	private TreeMap<Integer, Float> scanValues;

	@Before
	public void setUp() throws Exception {

		intensityValues = new PeakIntensityValues();
		scanValues = new TreeMap<Integer, Float>();
		scanValues.put(1500, 0.0f);
		scanValues.put(2500, 0.05f);
		scanValues.put(3500, 0.1f);
		scanValues.put(4500, 0.15f);
		scanValues.put(5500, 0.2f);
		scanValues.put(6500, 0.3f);
		scanValues.put(7500, 0.46f);
		scanValues.put(8500, 0.82f);
		scanValues.put(9500, 1.0f);
		scanValues.put(10500, 0.86f);
		scanValues.put(11500, 0.64f);
		scanValues.put(12500, 0.43f);
		scanValues.put(13500, 0.3f);
		scanValues.put(14500, 0.15f);
		scanValues.put(15500, 0.04f);
		for(Entry<Integer, Float> entry : scanValues.entrySet()) {
			intensityValues.addIntensityValue(entry.getKey(), entry.getValue());
		}
		intensityValues.normalize();
	}

	@Test
	public void testGetIntensityValue_1() {

		assertEquals(IPeakIntensityValues.MAX_INTENSITY, intensityValues.getIntensityValue(9500).getValue(), 0);
	}

	@Test
	public void testGetIntensityValue_2() {

		assertEquals(0.0f, intensityValues.getIntensityValue(1500).getValue(), 0);
	}

	@Test
	public void testGetIntensityValue_3() {

		assertEquals(4.0f, intensityValues.getIntensityValue(15500).getValue(), 0);
	}

	@Test
	public void testGetIntensityValue_4() {

		assertEquals(15.000001f, intensityValues.getIntensityValue(4500).getValue(), 0);
	}

	@Test
	public void testGetIntensityValue_5() {

		assertEquals(43.0f, intensityValues.getIntensityValue(12500).getValue(), 0);
	}
}
