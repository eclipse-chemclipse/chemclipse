/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class Point_2_Test {

	private IPoint point;
	private double x = 25.3;
	private double y = 457.7;

	@Before
	public void setUp() {

		point = new Point(x, y);
	}

	@Test
	public void test_1() {

		assertEquals(25.3d, point.getX(), 0);
	}

	@Test
	public void test_2() {

		assertEquals(457.7d, point.getY(), 0);
	}

	@Test
	public void test_3() {

		IPoint point2 = new Point(25.3d, 457.7d);
		assertTrue(point.equals(point2));
	}

	@Test
	public void test_4() {

		IPoint point2 = new Point(25.3d, 457.7d);
		assertTrue(point2.equals(point));
	}

	@Test
	public void test_5() {

		IPoint point2 = new Point(25.4d, 457.7d);
		assertFalse(point.equals(point2));
	}

	@Test
	public void test_6() {

		IPoint point2 = new Point(25.4d, 457.7d);
		assertFalse(point2.equals(point));
	}

	@Test
	public void test_7() {

		IPoint point2 = new Point(25.3d, 457.6d);
		assertFalse(point.equals(point2));
	}

	@Test
	public void test_8() {

		IPoint point2 = new Point(25.3d, 457.6d);
		assertFalse(point2.equals(point));
	}

	@Test
	public void test_9() {

		IPoint point2 = new Point(25.2d, 457.8d);
		assertFalse(point.equals(point2));
	}

	@Test
	public void test_10() {

		IPoint point2 = new Point(25.2d, 457.8d);
		assertFalse(point2.equals(point));
	}
}
