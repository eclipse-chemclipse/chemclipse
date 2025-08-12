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

import org.eclipse.chemclipse.numeric.miscellaneous.SortOrder;
import org.junit.Before;
import org.junit.Test;

public class PointYComparator_5_Test {

	private IPoint point1;
	private double x1 = 24.3;
	private double y1 = 456.7;
	private IPoint point2;
	private double x2 = 25.3;
	private double y2 = 457.7;
	private PointYComparator pointYComparator;

	@Before
	public void setUp() {

		point1 = new Point(x1, y1);
		point2 = new Point(x2, y2);
		pointYComparator = new PointYComparator(SortOrder.DESCENDING);
	}

	@Test
	public void testComparator_1() {

		assertEquals("Compare", 1, pointYComparator.compare(point1, point2));
	}

	@Test
	public void testComparator_2() {

		assertEquals("Compare", -1, pointYComparator.compare(point2, point1));
	}
}
