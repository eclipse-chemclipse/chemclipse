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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
public class PeakIntensityValues_5_Test {

	private PeakIntensityValues intensityValues;
	private TreeMap<Integer, Float> scanValues;

	@Before
	public void setUp() throws Exception {

		intensityValues = new PeakIntensityValues();
		scanValues = new TreeMap<Integer, Float>();
		scanValues.put(10500, IPeakIntensityValues.MAX_INTENSITY);
		for(Entry<Integer, Float> entry : scanValues.entrySet()) {
			intensityValues.addIntensityValue(entry.getKey(), entry.getValue());
		}
	}

	@Test
	public void testGetHighestIntensityValue_1() {

		Entry<Integer, Float> entry = intensityValues.getHighestIntensityValue();
		assertNotNull("Entry<Integer, Float> must not be null.", entry);
		int retentionTime = entry.getKey();
		float intensity = entry.getValue();
		assertEquals("retentionTime", 10500, retentionTime);
		assertEquals("intensity", IPeakIntensityValues.MAX_INTENSITY, intensity, 0);
	}

	@Test
	public void testGetIntensityValue_1() {

		Entry<Integer, Float> entry = intensityValues.getIntensityValue(8500);
		assertNull("Entry<Integer, Float> must be null.", entry);
	}

	@Test
	public void testGetIntensityValue_2() {

		Entry<Integer, Float> entry = intensityValues.getIntensityValue(2600);
		assertNull("Entry<Integer, Float> must be null.", entry);
	}

	@Test
	public void testGetIntensityValue_3() {

		Entry<Integer, Float> entry = intensityValues.getIntensityValue(1200);
		assertNull("Entry<Integer, Float> must be null.", entry);
	}

	@Test
	public void testGetIntensityValue_4() {

		Entry<Integer, Float> entry = intensityValues.getIntensityValue(11700);
		assertNull("Entry<Integer, Float> must be null.", entry);
	}

	@Test
	public void testGetIntensityValue_5() {

		Entry<Integer, Float> entry = intensityValues.getIntensityValue(14500);
		assertNull("Entry<Integer, Float> must be null.", entry);
	}

	@Test
	public void testGetIntensityValue_6() {

		Entry<Integer, Float> entry = intensityValues.getIntensityValue(15600);
		assertNull("Entry<Integer, Float> must be null.", entry);
	}

	@Test
	public void testGetStartRetentionTime_1() {

		assertEquals("StartRetentionTime", 10500, intensityValues.getStartRetentionTime());
	}

	@Test
	public void testGetStopRetentionTime_1() {

		assertEquals("StopRetentionTime", 10500, intensityValues.getStopRetentionTime());
	}
}
