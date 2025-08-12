/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
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

import org.junit.Test;

public class Point_3_Test {

	@Test
	public void test1() {

		Point point = new Point(0, 0);
		assertEquals(0.0d, point.getX(), 0);
		assertEquals(0.0d, point.getY(), 0);
	}

	@Test
	public void test2() {

		Point point = new Point(1.5d, 0);
		assertEquals(1.5d, point.getX(), 0);
		assertEquals(0.0d, point.getY(), 0);
	}

	@Test
	public void test3() {

		Point point = new Point(0, 1.5d);
		assertEquals(0.0d, point.getX(), 0);
		assertEquals(1.5d, point.getY(), 0);
	}

	@Test
	public void test4() {

		Point point = new Point(-1.5d, 0);
		assertEquals(-1.5d, point.getX(), 0);
		assertEquals(0.0d, point.getY(), 0);
	}

	@Test
	public void test5() {

		Point point = new Point(0, -1.5d);
		assertEquals(0.0d, point.getX(), 0);
		assertEquals(-1.5d, point.getY(), 0);
	}
}
