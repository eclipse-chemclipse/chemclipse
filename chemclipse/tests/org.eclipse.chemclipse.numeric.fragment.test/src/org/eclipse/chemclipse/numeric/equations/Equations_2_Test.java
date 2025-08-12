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
import org.junit.Before;
import org.junit.Test;

/**
 * Check the compilation of a linear equation by multiple x,y value pairs using
 * GaussJordan fitting.
 */
public class Equations_2_Test {

	private IPoint[] points;

	@Before
	public void setUp() {

		points = new IPoint[4];
		points[0] = new Point(4.0d, 4.0d);
		points[1] = new Point(7.0d, 1.0d);
		points[2] = new Point(6.0d, 3.0d);
		points[3] = new Point(2.0d, 5.0d);
	}

	@Test
	public void testCreateLinearEquation_1() {

		LinearEquation eq = Equations.createLinearEquation(points);
		assertEquals("X=0", 6.711864406779662d, eq.calculateY(0), 0);
	}

	@Test
	public void testCreateLinearEquation_2() {

		LinearEquation eq = Equations.createLinearEquation(points);
		assertEquals("toString()", "org.eclipse.chemclipse.numeric.equations.LinearEquation[f(x)=-0.7288135593220342x + 6.711864406779662]", eq.toString());
	}
}
