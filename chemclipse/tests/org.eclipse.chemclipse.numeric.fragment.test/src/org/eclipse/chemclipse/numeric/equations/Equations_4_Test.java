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
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.eclipse.chemclipse.numeric.exceptions.SolverException;
import org.junit.Test;

/**
 * Checks calculation of intersection between two linear equations.
 */
public class Equations_4_Test {

	@Test
	public void testCalculateIntersection_1() throws SolverException {

		IPoint p11 = new Point(1, 4);
		IPoint p12 = new Point(8, 14);
		LinearEquation eq1 = Equations.createLinearEquation(p11, p12);
		IPoint p21 = new Point(6, 3);
		IPoint p22 = new Point(1, 8);
		LinearEquation eq2 = Equations.createLinearEquation(p21, p22);
		IPoint intersection = Equations.calculateIntersection(eq1, eq2);
		assertEquals("X", 2.6470588235294117d, intersection.getX(), 0);
		assertEquals("Y", 6.352941176470588d, intersection.getY(), 0);
	}

	@Test
	public void testCalculateIntersection_2() {

		IPoint p11 = new Point(1, 4);
		IPoint p12 = new Point(8, 14);
		LinearEquation eq1 = Equations.createLinearEquation(p11, p12);
		assertThrows(SolverException.class, () -> {
			Equations.calculateIntersection(eq1, eq1);
		});
	}

	@Test
	public void testCalculateIntersection_3() {

		IPoint p11 = new Point(1, 4);
		IPoint p12 = new Point(8, 14);
		LinearEquation eq1 = Equations.createLinearEquation(p11, p12);
		IPoint p21 = new Point(1, 10);
		IPoint p22 = new Point(8, 20);
		LinearEquation eq2 = Equations.createLinearEquation(p21, p22);
		assertThrows(SolverException.class, () -> {
			Equations.calculateIntersection(eq1, eq2);
		});
	}

	@Test
	public void testCalculateIntersection_4() {

		IPoint p11 = new Point(1, 4);
		IPoint p12 = new Point(8, 14);
		LinearEquation eq1 = Equations.createLinearEquation(p11, p12);
		assertThrows(SolverException.class, () -> {
			Equations.calculateIntersection(eq1, eq1);
		});
	}
}
