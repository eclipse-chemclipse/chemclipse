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

import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class PeakModel_4_Test extends PeakModelTestCase {

	protected float startBackgroundAbundance = 360.0f;
	protected float stopBackgroundAbundance = 100000.0f;

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
		super.setUp(startBackgroundAbundance, stopBackgroundAbundance, peakMaximum);
	}

	@Test
	public void testGetBackgroundAbundance_1() {

		assertEquals("GetBackgroundAbundance", 360.0f, peakModel.getBackgroundAbundance(1500), 0);
	}

	@Test
	public void testGetBackgroundAbundance_2() {

		assertEquals("GetBackgroundAbundance", 45198.0f, peakModel.getBackgroundAbundance(7800), 0);
	}

	@Test
	public void testGetBackgroundAbundance_3() {

		assertEquals("GetBackgroundAbundance", 100000.0f, peakModel.getBackgroundAbundance(15500), 0);
	}

	@Test
	public void testGetBackgroundAbundance_4() {

		assertEquals("GetBackgroundAbundance", 57297.145f, peakModel.getBackgroundAbundance(), 0);
	}

	@Test
	public void testGetPeakAbundance_1() {

		assertEquals("GetPeakAbundance", 523100.00f, peakModel.getPeakAbundance(), 0);
	}

	@Test
	public void testGetPeakAbundanceByInflectionPoints_1() {

		assertEquals("GetPeakAbundanceByInflectionPoints", 584789.75f, peakModel.getPeakAbundanceByInflectionPoints(), 0);
	}

	@Test
	public void testGetWidthBaselineTotal_1() {

		assertEquals("GetWidthBaselineTotal", 14001, peakModel.getWidthBaselineTotal());
	}

	@Test
	public void testGetWidthBaselineByInflectionPoints_1() {

		assertEquals("GetWidthBaselineByInflectionPoints", 8187, peakModel.getWidthBaselineByInflectionPoints());
	}

	@Test
	public void testGetWidthByInflectionPoints_1() {

		assertEquals("GetWidthByInflectionPoints", 4094, peakModel.getWidthByInflectionPoints());
	}

	@Test
	public void testGetWidthByInflectionPoints_2() {

		assertEquals("GetWidthByInflectionPoints 50%", 4094, peakModel.getWidthByInflectionPoints(0.5f));
	}

	@Test
	public void testGetWidthByInflectionPoints_3() {

		assertEquals("GetWidthByInflectionPoints 80%", 1638, peakModel.getWidthByInflectionPoints(0.8f));
	}

	@Test
	public void testGetWidthByInflectionPoints_4() {

		assertEquals("GetWidthByInflectionPoints -10%", 0, peakModel.getWidthByInflectionPoints(-0.1f));
	}

	@Test
	public void testGetWidthByInflectionPoints_5() {

		assertEquals("GetWidthByInflectionPoints 110%", 0, peakModel.getWidthByInflectionPoints(1.1f));
	}

	@Test
	public void testGetStartRetentionTime_1() {

		assertEquals("GetStartRetentionTime", 1500, peakModel.getStartRetentionTime());
	}

	@Test
	public void testGetStopRetentionTime_1() {

		assertEquals("GetStopRetentionTime", 15500, peakModel.getStopRetentionTime());
	}

	@Test
	public void testGetRetentionTimeAtPeakMaximum_1() {

		assertEquals("GetRetentionTimeAtPeakMaximum", 9500, peakModel.getRetentionTimeAtPeakMaximum());
	}

	@Test
	public void testGetRetentionTimeAtPeakMaximumByInflectionPoints_1() {

		assertEquals("GetRetentionTimeAtPeakMaximumByInflectionPoints", 9327, peakModel.getRetentionTimeAtPeakMaximumByInflectionPoints());
	}

	@Test
	public void testGradientAngle_1() {

		assertEquals("GradientAngle", 82.001403397434d, peakModel.getGradientAngle(), 0);
	}

	@Test
	public void testGetIncreasingInflectionPointAbundance_1() {

		assertEquals("GetIncreasingInflectionPointAbundance", -889270.0f, peakModel.getIncreasingInflectionPointAbundance(1500), 0);
	}

	@Test
	public void testGetIncreasingInflectionPointAbundance_2() {

		assertEquals("GetIncreasingInflectionPointAbundance", 297120.8f, peakModel.getIncreasingInflectionPointAbundance(7800), 0);
	}

	@Test
	public void testGetIncreasingInflectionPointAbundance_3() {

		assertEquals("GetIncreasingInflectionPointAbundance", 1747154.0f, peakModel.getIncreasingInflectionPointAbundance(15500), 0);
	}

	@Test
	public void testGetDecreasingInflectionPointAbundance_1() {

		assertEquals("GetDecreasingInflectionPointAbundance", 1485604.0f, peakModel.getDecreasingInflectionPointAbundance(1500), 0);
	}

	@Test
	public void testGetDecreasingInflectionPointAbundance_2() {

		assertEquals("GetDecreasingInflectionPointAbundance", 760587.4f, peakModel.getDecreasingInflectionPointAbundance(7800), 0);
	}

	@Test
	public void testGetDecreasingInflectionPointAbundance_3() {

		assertEquals("GetDecreasingInflectionPointAbundance", -125544.0f, peakModel.getDecreasingInflectionPointAbundance(15500), 0);
	}
}
