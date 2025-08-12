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

public class LinearEquations_1_Test {

	@Test
	public void testCreateLinearEquation_1() {

		IPoint p1 = new Point(5.0d, 10.0d);
		IPoint p2 = new Point(7.0d, 3.0d);
		LinearEquation eq = Equations.createLinearEquation(p1, p2);
		assertEquals("X=0", 27.5d, eq.calculateY(0), 0);
	}

	@Test
	public void testCreateLinearEquation_2() {

		IPoint p1 = new Point(5.0d, 10.0d);
		IPoint p2 = new Point(7.0d, 3.0d);
		LinearEquation eq = Equations.createLinearEquation(p1, p2);
		assertEquals("toString()", "org.eclipse.chemclipse.numeric.equations.LinearEquation[f(x)=-3.5x + 27.5]", eq.toString());
	}
}
