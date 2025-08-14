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
package org.eclipse.chemclipse.xxd.model.quantitation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.quantitation.IQuantitationSignal;
import org.eclipse.chemclipse.model.quantitation.QuantitationSignal;
import org.junit.Before;
import org.junit.Test;

public class QuantitationSignal_2_Test {

	private IQuantitationSignal quantitationSignal1;
	private IQuantitationSignal quantitationSignal2;

	@Before
	public void setUp() {

		quantitationSignal1 = new QuantitationSignal(56.2d, 78.5f);
		quantitationSignal2 = new QuantitationSignal(56.2d, 78.5f);
	}

	@Test
	public void testEquals_1() {

		assertTrue(quantitationSignal1.equals(quantitationSignal2));
	}

	@Test
	public void testEquals_2() {

		assertTrue(quantitationSignal2.equals(quantitationSignal1));
	}

	@Test
	public void testHashCode_1() {

		assertEquals(quantitationSignal1.hashCode(), quantitationSignal2.hashCode());
	}

	@Test
	public void testHashCode_2() {

		assertEquals(quantitationSignal2.hashCode(), quantitationSignal1.hashCode());
	}

	@Test
	public void testToString_1() {

		assertEquals(quantitationSignal1.toString(), quantitationSignal2.toString());
	}

	@Test
	public void testToString_2() {

		assertEquals(quantitationSignal2.toString(), quantitationSignal1.toString());
	}
}
