/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.quantitation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class InternalStandard_1_Test {

	private InternalStandard internalStandard = new InternalStandard("Styrene", 15.5d, "mg/L");

	@Test
	public void test1() {

		assertEquals(1.0d, internalStandard.getCompensationFactor(), 0);
		assertEquals(1.0d, internalStandard.getResponseFactor(), 0);
	}

	@Test
	public void test2() {

		internalStandard.setCompensationFactor(-1);
		assertEquals(1.0d, internalStandard.getCompensationFactor(), 0);
		assertEquals(1.0d, internalStandard.getResponseFactor(), 0);
	}

	@Test
	public void test3() {

		internalStandard.setCompensationFactor(0);
		assertEquals(1.0d, internalStandard.getCompensationFactor(), 0);
		assertEquals(1.0d, internalStandard.getResponseFactor(), 0);
	}

	@Test
	public void test4() {

		internalStandard.setCompensationFactor(0.5);
		assertEquals(0.5d, internalStandard.getCompensationFactor(), 0);
		assertEquals(2.0d, internalStandard.getResponseFactor(), 0);
	}

	@Test
	public void test5() {

		internalStandard.setCompensationFactor(1.0);
		assertEquals(1.0d, internalStandard.getCompensationFactor(), 0);
		assertEquals(1.0d, internalStandard.getResponseFactor(), 0);
	}

	@Test
	public void test6() {

		internalStandard.setCompensationFactor(2.0);
		assertEquals(2.0d, internalStandard.getCompensationFactor(), 0);
		assertEquals(0.5d, internalStandard.getResponseFactor(), 0);
	}
}