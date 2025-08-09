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
package org.eclipse.chemclipse.model.support;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AnalysisSupport_1_Test {

	private IAnalysisSupport support;

	@Before
	public void setUp() throws Exception {

		support = new AnalysisSupport(5726, 13);
	}

	@Test
	public void testGetNumberOfAnalysisSegments_1() {

		assertEquals("NumberOfAnalysisSegments", 441, support.getNumberOfAnalysisSegments());
	}

	@Test
	public void testSegment_1() {

		List<IAnalysisSegment> segments = support.getAnalysisSegments();
		IAnalysisSegment segment = segments.get(439);
		assertEquals("StartScan", 5708, segment.getStartScan());
		assertEquals("StopScan", 5720, segment.getStopScan());
		assertEquals("SegmentWidth", 13, segment.getWidth());
	}

	@Test
	public void testSegment_2() {

		List<IAnalysisSegment> segments = support.getAnalysisSegments();
		IAnalysisSegment segment = segments.get(440);
		assertEquals("StartScan", 5721, segment.getStartScan());
		assertEquals("StopScan", 5726, segment.getStopScan());
		assertEquals("SegmentWidth", 6, segment.getWidth());
	}
}
