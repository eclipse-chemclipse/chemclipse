/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
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

import org.junit.Before;
import org.junit.Test;

public class QuadraticEquations_2_Test {

	private QuadraticEquation eq;

	@Before
	public void setUp() {

		eq = new QuadraticEquation(-2072.9175708325347d, 310206.81754652766d, 206670.62686893356);
	}

	@Test
	public void testCreateLinearEquation_1() {

		double result = eq.calculateX(4192434.278134346d);
		assertEquals(14.195265251939228d, result, 0);
	}
}