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

import org.junit.Before;
import org.junit.Test;

public class AnalysisSegment_3_Test {

	private IAnalysisSegment segment;
	private final int startScan = -5;
	private final int segmentWidth = -5;

	@Before
	public void setUp() throws Exception {

		segment = new AnalysisSegment(startScan, segmentWidth) {

			@Override
			public int getStartRetentionTime() {

				return 0;
			}

			@Override
			public int getStopRetentionTime() {

				return 0;
			}
		};
	}

	@Test
	public void testGetStartScan_1() {

		assertEquals("StartScan", 0, segment.getStartScan());
	}

	@Test
	public void testGetStopScan_1() {

		assertEquals("StopScan", 0, segment.getStopScan());
	}

	@Test
	public void testGetSegmentWidth_1() {

		assertEquals("SegmentWidth", 0, segment.getWidth());
	}
}
