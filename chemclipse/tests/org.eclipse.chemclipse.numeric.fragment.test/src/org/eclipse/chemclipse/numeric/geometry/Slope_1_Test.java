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
package org.eclipse.chemclipse.numeric.geometry;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.junit.Test;

public class Slope_1_Test {

	@Test
	public void testGetSlope_1() {

		IPoint p1 = new Point(5.0d, 10.0d);
		IPoint p2 = new Point(7.0d, 3.0d);
		ISlope slope = new Slope(p1, p2);
		assertEquals("Slope", -3.5d, slope.getSlope(), 0);
	}

	@Test
	public void testGetSlope_2() {

		IPoint p1 = null;
		IPoint p2 = new Point(7.0d, 3.0d);
		ISlope slope = new Slope(p1, p2);
		assertEquals("Slope", 0.0d, slope.getSlope(), 0);
	}

	@Test
	public void testGetSlope_3() {

		IPoint p1 = new Point(5.0d, 10.0d);
		IPoint p2 = null;
		ISlope slope = new Slope(p1, p2);
		assertEquals("Slope", 0.0d, slope.getSlope(), 0);
	}

	@Test
	public void testGetSlope_4() {

		IPoint p1 = new Point(5.0d, 10.0d);
		IPoint p2 = null;
		ISlope slope = new Slope(p1, p2);
		slope.setSlope(4.6d);
		assertEquals("Slope", 4.6d, slope.getSlope(), 0);
	}
}
