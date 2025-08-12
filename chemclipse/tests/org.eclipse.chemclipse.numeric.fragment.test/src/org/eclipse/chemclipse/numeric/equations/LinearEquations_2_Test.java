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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.junit.Before;
import org.junit.Test;

public class LinearEquations_2_Test {

	private LinearEquation eq1;
	private LinearEquation eq2;

	@Before
	public void setUp() {

		IPoint p11 = new Point(1, 4);
		IPoint p12 = new Point(8, 14);
		eq1 = Equations.createLinearEquation(p11, p12);
		eq2 = Equations.createLinearEquation(p11, p12);
	}

	@Test
	public void testEquals_1() {

		assertEquals("equals", eq1, eq2);
	}

	@Test
	public void testEquals_2() {

		assertEquals("equals", eq2, eq1);
	}

	@Test
	public void testEquals_3() {

		assertNotNull("equals", eq1);
	}

	@Test
	public void testEquals_4() {

		assertNotEquals("equals", eq1, new Object());
	}

	@Test
	public void testHashCode_1() {

		assertEquals("hashCode", eq1.hashCode(), eq2.hashCode());
	}
}
