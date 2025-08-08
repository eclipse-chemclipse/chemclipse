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

import org.eclipse.chemclipse.numeric.core.Point;
import org.junit.Test;

public class SegmentAreaCalculator_1_Test {

	@Test
	public void testCalculator_1() {

		/*
		 * Chromatogram baseline linear, 0 Peak baseline increasing
		 */
		Point peakBaselinePoint1 = new Point(1, 2);
		Point peakBaselinePoint2 = new Point(4, 3);
		Point chromatogramBaselinePoint1 = new Point(1, 0);
		Point chromatogramBaselinePoint2 = new Point(4, 0);
		Segment segment = new Segment(chromatogramBaselinePoint1, chromatogramBaselinePoint2, peakBaselinePoint1, peakBaselinePoint2);
		assertEquals("Area", 7.5d, SegmentAreaCalculator.calculateSegmentArea(segment), 0);
	}

	@Test
	public void testCalculator_2() {

		/*
		 * Chromatogram baseline decreasing Peak baseline increasing
		 */
		Point peakBaselinePoint1 = new Point(1, 2);
		Point peakBaselinePoint2 = new Point(4, 3);
		Point chromatogramBaselinePoint1 = new Point(1, 1);
		Point chromatogramBaselinePoint2 = new Point(4, 0);
		Segment segment = new Segment(chromatogramBaselinePoint1, chromatogramBaselinePoint2, peakBaselinePoint1, peakBaselinePoint2);
		assertEquals("Area", 6.0d, SegmentAreaCalculator.calculateSegmentArea(segment), 0);
	}

	@Test
	public void testCalculator_3() {

		/*
		 * Chromatogram baseline linear above peak baseline Peak baseline
		 * increasing
		 */
		Point peakBaselinePoint1 = new Point(1, 2);
		Point peakBaselinePoint2 = new Point(4, 3);
		Point chromatogramBaselinePoint1 = new Point(1, 4);
		Point chromatogramBaselinePoint2 = new Point(4, 4);
		Segment segment = new Segment(chromatogramBaselinePoint1, chromatogramBaselinePoint2, peakBaselinePoint1, peakBaselinePoint2);
		assertEquals("Area", -4.5d, SegmentAreaCalculator.calculateSegmentArea(segment), 0);
	}

	@Test
	public void testCalculator_4() {

		/*
		 * Chromatogram baseline linear above peak baseline Peak baseline
		 * increasing
		 */
		Point peakBaselinePoint1 = new Point(1, 2);
		Point peakBaselinePoint2 = new Point(4, 3);
		Point chromatogramBaselinePoint1 = new Point(1, 5);
		Point chromatogramBaselinePoint2 = new Point(4, 4);
		Segment segment = new Segment(chromatogramBaselinePoint1, chromatogramBaselinePoint2, peakBaselinePoint1, peakBaselinePoint2);
		assertEquals("Area", -6.0d, SegmentAreaCalculator.calculateSegmentArea(segment), 0);
	}

	@Test
	public void testCalculator_5() {

		/*
		 * Chromatogram baseline and peak baseline are crossing Peak baseline
		 * increasing Chromatogram baseline linear
		 */
		Point peakBaselinePoint1 = new Point(1, 1);
		Point peakBaselinePoint2 = new Point(5, 5);
		Point chromatogramBaselinePoint1 = new Point(1, 3);
		Point chromatogramBaselinePoint2 = new Point(5, 3);
		Segment segment = new Segment(chromatogramBaselinePoint1, chromatogramBaselinePoint2, peakBaselinePoint1, peakBaselinePoint2);
		assertEquals("Area", 0.0d, SegmentAreaCalculator.calculateSegmentArea(segment), 0);
	}

	@Test
	public void testCalculator_6() {

		/*
		 * Chromatogram baseline and peak baseline are crossing Peak baseline
		 * linear Chromatogram baseline increasing
		 */
		Point peakBaselinePoint1 = new Point(1, 3);
		Point peakBaselinePoint2 = new Point(5, 3);
		Point chromatogramBaselinePoint1 = new Point(1, 1);
		Point chromatogramBaselinePoint2 = new Point(5, 5);
		Segment segment = new Segment(chromatogramBaselinePoint1, chromatogramBaselinePoint2, peakBaselinePoint1, peakBaselinePoint2);
		assertEquals("Area", 0.0d, SegmentAreaCalculator.calculateSegmentArea(segment), 0);
	}

	@Test
	public void testCalculator_7() {

		/*
		 * Chromatogram baseline and peak baseline are crossing Peak baseline
		 * decreasing Chromatogram baseline increasing
		 */
		Point peakBaselinePoint1 = new Point(1, 5);
		Point peakBaselinePoint2 = new Point(5, 1);
		Point chromatogramBaselinePoint1 = new Point(1, 1);
		Point chromatogramBaselinePoint2 = new Point(5, 5);
		Segment segment = new Segment(chromatogramBaselinePoint1, chromatogramBaselinePoint2, peakBaselinePoint1, peakBaselinePoint2);
		assertEquals("Area", 0.0d, SegmentAreaCalculator.calculateSegmentArea(segment), 0);
	}

	@Test
	public void testCalculator_8() {

		/*
		 * Chromatogram baseline and peak baseline are crossing Peak baseline
		 * decreasing Chromatogram baseline increasing
		 */
		Point peakBaselinePoint1 = new Point(1, 5);
		Point peakBaselinePoint2 = new Point(6, 0);
		Point chromatogramBaselinePoint1 = new Point(1, 1);
		Point chromatogramBaselinePoint2 = new Point(6, 6);
		Segment segment = new Segment(chromatogramBaselinePoint1, chromatogramBaselinePoint2, peakBaselinePoint1, peakBaselinePoint2);
		assertEquals("Area", -5.0d, SegmentAreaCalculator.calculateSegmentArea(segment), 0);
	}

	@Test
	public void testCalculator_9() {

		/*
		 * Chromatogram baseline and peak baseline are crossing Peak baseline
		 * decreasing Chromatogram baseline increasing
		 */
		Point peakBaselinePoint1 = new Point(0, 6);
		Point peakBaselinePoint2 = new Point(5, 1);
		Point chromatogramBaselinePoint1 = new Point(0, 0);
		Point chromatogramBaselinePoint2 = new Point(5, 5);
		Segment segment = new Segment(chromatogramBaselinePoint1, chromatogramBaselinePoint2, peakBaselinePoint1, peakBaselinePoint2);
		assertEquals("Area", 5.0d, SegmentAreaCalculator.calculateSegmentArea(segment), 0);
	}
}
