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

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.chemclipse.model.implementation.PeakIntensityValues;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak intensity values.<br/>
 * Make sure that the limit IPeakIntensityValues.MAX_INTENSITY is implemented
 * correctly.
 */
public class PeakIntensityValues_6_Test {

	private PeakIntensityValues intensityValues;
	private TreeMap<Integer, Float> scanValues;

	@Before
	public void setUp() throws Exception {

		intensityValues = new PeakIntensityValues();
		scanValues = new TreeMap<Integer, Float>();
		scanValues.put(1500, 0.0f);
		scanValues.put(2500, 5.0f);
		scanValues.put(3500, 10.0f);
		scanValues.put(4500, 15.0f);
		scanValues.put(5500, 20.0f);
		scanValues.put(6500, 30.0f);
		scanValues.put(7500, 46.0f);
		scanValues.put(8500, 82.0f);
		scanValues.put(9500, 100.0f);
		scanValues.put(10500, 86.0f);
		scanValues.put(11500, 64.0f);
		scanValues.put(12500, 43.0f);
		scanValues.put(13500, 30.0f);
		scanValues.put(14500, 15.0f);
		scanValues.put(15500, 4.0f);
		for(Entry<Integer, Float> entry : scanValues.entrySet()) {
			intensityValues.addIntensityValue(entry.getKey(), entry.getValue());
		}
	}

	@Test
	public void testGetRetentionTimes_1() {

		int rt;
		List<Integer> retentionTimes = intensityValues.getRetentionTimes();
		rt = retentionTimes.get(0);
		assertEquals("retentionTime", 1500, rt);
		rt = retentionTimes.get(4);
		assertEquals("retentionTime", 5500, rt);
		rt = retentionTimes.get(7);
		assertEquals("retentionTime", 8500, rt);
		rt = retentionTimes.get(10);
		assertEquals("retentionTime", 11500, rt);
		rt = retentionTimes.get(14);
		assertEquals("retentionTime", 15500, rt);
	}
}
