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

import org.eclipse.chemclipse.model.quantitation.IQuantitationSignal;
import org.eclipse.chemclipse.model.quantitation.QuantitationSignal;
import org.junit.Before;
import org.junit.Test;

public class QuantitationSignal_1_Test {

	private IQuantitationSignal quantitationSignal;

	@Before
	public void setUp() {

		quantitationSignal = new QuantitationSignal(56.2d, 78.5f);
	}

	@Test
	public void testGetIon_1() {

		assertEquals(56.2d, quantitationSignal.getSignal(), 0);
	}

	@Test
	public void testGetRelativeResponse_1() {

		assertEquals(78.5d, quantitationSignal.getRelativeResponse(), 0);
	}

	@Test
	public void testGetUncertainty_1() {

		assertEquals(0.0d, quantitationSignal.getUncertainty(), 0);
	}

	@Test
	public void testGetUncertainty_2() {

		quantitationSignal.setUncertainty(0.6d);
		assertEquals(0.6d, quantitationSignal.getUncertainty(), 0);
	}

	@Test
	public void testIsUse_1() {

		assertEquals(true, quantitationSignal.isUse());
	}

	@Test
	public void testIsUse_2() {

		quantitationSignal.setUse(false);
		assertEquals(false, quantitationSignal.isUse());
	}
}
