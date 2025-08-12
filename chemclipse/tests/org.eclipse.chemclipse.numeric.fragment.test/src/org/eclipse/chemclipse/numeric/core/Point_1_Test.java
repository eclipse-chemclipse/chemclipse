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
package org.eclipse.chemclipse.numeric.core;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Point_1_Test {

	private IPoint point;
	private double x = 25.3;
	private double y = 457.7;

	@Before
	public void setUp() throws Exception {

		point = new Point(x, y);
	}

	@Test
	public void testPointX_1() {

		assertEquals("X", x, point.getX(), 0);
	}

	@Test
	public void testPointX_2() {

		assertEquals("X", x, point.getX(), 0);
		x = 3682.234;
		point.setX(x);
		assertEquals("X", x, point.getX(), 0);
	}

	@Test
	public void testPointY_1() {

		assertEquals("Y", y, point.getY(), 0);
	}

	@Test
	public void testPointY_2() {

		assertEquals("Y", y, point.getY(), 0);
		y = 8273.3;
		point.setY(y);
		assertEquals("Y", y, point.getY(), 0);
	}
}
