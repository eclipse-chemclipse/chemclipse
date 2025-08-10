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
package org.eclipse.chemclipse.msd.model.xic;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the class IonRange concerning equals, hashCode and toString.
 */
public class IonRange_2_Test {

	private IIonRange ionRange1;
	private IIonRange ionRange2;

	@Before
	public void setUp() {

		ionRange1 = new IonRange(1, 5);
		ionRange2 = new IonRange(1, 5);
	}

	@Test
	public void testEquals_1() {

		assertTrue(ionRange1.equals(ionRange2));
	}

	@Test
	public void testEquals_2() {

		assertTrue(ionRange2.equals(ionRange1));
	}

	@Test
	public void testHashCode_1() {

		assertTrue(ionRange1.hashCode() == ionRange2.hashCode());
	}

	@Test
	public void testHashCode_2() {

		assertTrue(ionRange2.hashCode() == ionRange1.hashCode());
	}

	@Test
	public void testToString_1() {

		assertTrue(ionRange1.toString().equals(ionRange2.toString()));
	}

	@Test
	public void testToString_2() {

		assertTrue(ionRange2.toString().equals(ionRange1.toString()));
	}
}
