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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.support;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.junit.Before;
import org.junit.Test;

public class Segment_4_Test {

	private ISegment segment;
	private IPoint p1;
	private IPoint p2;
	private IPoint p3;
	private IPoint p4;

	@Before
	public void setUp() throws Exception {

		p1 = new Point(1, 2);
		p2 = new Point(10, 20);
		p3 = new Point(100, 200);
		p4 = new Point(1000, 2000);
		segment = new Segment();
	}

	@Test
	public void testPoint_1() {

		segment.setChromatogramBaselinePoint1(p1);
		assertEquals("ChromatogramBaselinePoint1 X", 1.0d, segment.getChromatogramBaselinePoint1().getX(), 0);
		assertEquals("ChromatogramBaselinePoint1 Y", 2.0d, segment.getChromatogramBaselinePoint1().getY(), 0);
	}

	@Test
	public void testPoint_2() {

		segment.setChromatogramBaselinePoint2(p2);
		assertEquals("ChromatogramBaselinePoint2 X", 10.0d, segment.getChromatogramBaselinePoint2().getX(), 0);
		assertEquals("ChromatogramBaselinePoint2 Y", 20.0d, segment.getChromatogramBaselinePoint2().getY(), 0);
	}

	@Test
	public void testPoint_3() {

		segment.setPeakBaselinePoint1(p3);
		assertEquals("PeakBaselinePoint1 X", 100.0d, segment.getPeakBaselinePoint1().getX(), 0);
		assertEquals("PeakBaselinePoint1 Y", 200.0d, segment.getPeakBaselinePoint1().getY(), 0);
	}

	@Test
	public void testPoint_4() {

		segment.setPeakBaselinePoint2(p4);
		assertEquals("PeakBaselinePoint2 X", 1000.0d, segment.getPeakBaselinePoint2().getX(), 0);
		assertEquals("PeakBaselinePoint2 Y", 2000.0d, segment.getPeakBaselinePoint2().getY(), 0);
	}
}
