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
package org.eclipse.chemclipse.numeric.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.junit.Before;
import org.junit.Test;

public class Slope_2_Test {

	private ISlope slope1, slope2;
	private IPoint p1, p2;

	@Before
	public void setUp() {

		p1 = new Point(5.0d, 10.0d);
		p2 = new Point(7.0d, 3.0d);
		slope1 = new Slope(p1, p2);
		slope2 = new Slope(p1, p2);
	}

	@Test
	public void testEquals_1() {

		assertEquals("equals", slope1, slope2);
	}

	@Test
	public void testEquals_2() {

		assertEquals("equals", slope2, slope1);
	}

	@Test
	public void testEquals_3() {

		assertNotNull("equals", slope1);
	}

	@Test
	public void testEquals_4() {

		assertNotEquals("equals", slope1, new Object());
	}

	@Test
	public void hashCode_1() {

		assertEquals("hashCode", slope1.hashCode(), slope2.hashCode());
	}
}
