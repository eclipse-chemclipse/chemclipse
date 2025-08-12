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

import org.junit.Before;
import org.junit.Test;

public class RectangularTrapezium_1_Test {

	private RectangularTrapezium rectangularTrapezium;

	@Before
	public void setUp() {

		rectangularTrapezium = new RectangularTrapezium(10.0d, 15.0d, 10.0d);
	}

	@Test
	public void testGetArea() {

		assertEquals("Area", 125.0d, rectangularTrapezium.getArea(), 0);
	}
}
