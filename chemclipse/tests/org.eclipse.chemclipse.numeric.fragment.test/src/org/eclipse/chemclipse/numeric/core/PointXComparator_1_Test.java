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

public class PointXComparator_1_Test {

	private IPoint point1;
	private double x1 = 25.3;
	private double y1 = 457.7;
	private IPoint point2;
	private double x2 = 25.3;
	private double y2 = 457.7;
	private PointXComparator pointXComparator;

	@Before
	public void setUp() {

		point1 = new Point(x1, y1);
		point2 = new Point(x2, y2);
		pointXComparator = new PointXComparator();
	}

	@Test
	public void testComparator_1() {

		assertEquals("Compare", 0, pointXComparator.compare(point1, point2));
	}
}
