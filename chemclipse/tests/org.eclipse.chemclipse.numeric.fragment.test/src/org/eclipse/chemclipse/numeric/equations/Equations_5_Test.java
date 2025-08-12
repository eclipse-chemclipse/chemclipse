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
package org.eclipse.chemclipse.numeric.equations;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.junit.Test;

/**
 * Checks calculation of width between two points.
 */
public class Equations_5_Test {

	@Test
	public void testCalculateWidth_1() {

		IPoint p1 = new Point(1, 4);
		IPoint p2 = new Point(8, 14);
		double width = Equations.calculateWidth(p1, p2);
		assertEquals("Width", 12.206555615733702d, width, 0);
	}

	@Test
	public void testCalculateWidth_2() {

		IPoint p1 = new Point(0, 0);
		IPoint p2 = new Point(0, 0);
		double width = Equations.calculateWidth(p1, p2);
		assertEquals("Width", 0.0d, width, 0);
	}
}
