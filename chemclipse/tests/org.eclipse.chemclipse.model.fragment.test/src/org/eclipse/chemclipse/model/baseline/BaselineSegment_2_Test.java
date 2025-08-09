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
package org.eclipse.chemclipse.model.baseline;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class BaselineSegment_2_Test {

	private IBaselineSegment segment;
	private int startRetentionTime;
	private int stopRetentionTime;
	private float startBackgroundAbundance;
	private float stopBackgroundAbundance;

	@Before
	public void setUp() throws Exception {

		startRetentionTime = 4500;
		stopRetentionTime = 10500;
		startBackgroundAbundance = -1.0f;
		stopBackgroundAbundance = -1.0f;
		segment = new BaselineSegment(stopRetentionTime, startRetentionTime);
		segment.setStartBackgroundAbundance(startBackgroundAbundance);
		segment.setStopBackgroundAbundance(stopBackgroundAbundance);
	}

	@Test
	public void testGetStartRetentionTime_1() {

		assertEquals("StartRetentionTime", startRetentionTime, segment.getStartRetentionTime());
	}

	@Test
	public void testGetStopRetentionTime_1() {

		assertEquals("StopRetentionTime", stopRetentionTime, segment.getStopRetentionTime());
	}

	@Test
	public void testGetStartBackgroundAbundance_1() {

		assertEquals("StartBackgroundAbundance", -1.0f, segment.getStartBackgroundAbundance(), 0);
	}

	@Test
	public void testGetStopBackgroundAbundance_1() {

		assertEquals("StopBackgroundAbundance", -1.0f, segment.getStopBackgroundAbundance(), 0);
	}
}
