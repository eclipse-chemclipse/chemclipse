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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class BaselineSegment_4_Test {

	private IBaselineSegment segmentI;
	private IBaselineSegment segmentII;
	private int startRetentionTime;
	private int stopRetentionTime;
	private float startBackgroundAbundance;
	private float stopBackgroundAbundance;

	@Before
	public void setUp() throws Exception {

		startRetentionTime = 4500;
		stopRetentionTime = 10500;
		startBackgroundAbundance = 500.0f;
		stopBackgroundAbundance = 4000.0f;
		segmentI = new BaselineSegment(startRetentionTime, stopRetentionTime);
		segmentI.setStartBackgroundAbundance(startBackgroundAbundance);
		segmentI.setStopBackgroundAbundance(stopBackgroundAbundance);
		segmentII = new BaselineSegment(startRetentionTime, stopRetentionTime);
		segmentII.setStartBackgroundAbundance(startBackgroundAbundance);
		segmentII.setStopBackgroundAbundance(stopBackgroundAbundance);
	}

	@Test
	public void testEquals_1() {

		assertEquals("Equals", segmentI, segmentII);
	}

	@Test
	public void testEquals_2() {

		assertEquals("Equals", segmentII, segmentI);
	}

	@Test
	public void testEquals_3() {

		assertNotNull("Equals", segmentI);
	}

	@Test
	public void testEquals_4() {

		assertNotNull("Equals", segmentII);
	}

	@Test
	public void testEquals_5() {

		assertNotEquals("Equals", segmentI, new Object());
	}

	@Test
	public void testEquals_6() {

		assertNotEquals("Equals", segmentII, new Object());
	}

	@Test
	public void testHashCode_1() {

		assertEquals("hashCode", segmentI.hashCode(), segmentII.hashCode());
	}

	@Test
	public void testHashCode_2() {

		assertEquals("hashCode", segmentII.hashCode(), segmentI.hashCode());
	}
}
