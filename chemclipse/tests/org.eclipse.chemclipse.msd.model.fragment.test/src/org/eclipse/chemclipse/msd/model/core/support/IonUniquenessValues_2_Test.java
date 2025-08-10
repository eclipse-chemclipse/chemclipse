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
package org.eclipse.chemclipse.msd.model.core.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IonUniquenessValues_2_Test {

	private IIonUniquenessValues uniquenessValues = new IonUniquenessValues();

	@Test
	public void testAdd_1() {

		uniquenessValues.add(43, -0.1f);
		float uv = uniquenessValues.getUniquenessValue(43);
		assertEquals("Uniqueness Value", 1.0f, uv, 0);
		float pv = uniquenessValues.getPropabilityValue(43);
		assertEquals("Probability Value", 0.0f, pv, 0);
	}

	@Test
	public void testAdd_2() {

		uniquenessValues.add(43, 1.1f);
		float uv = uniquenessValues.getUniquenessValue(43);
		assertEquals("Uniqueness Value", 1.0f, uv, 0);
		float pv = uniquenessValues.getPropabilityValue(43);
		assertEquals("Probability Value", 0.0f, pv, 0);
	}

	@Test
	public void testAdd_3() {

		uniquenessValues.add(150, 0.1f);
		float uv = uniquenessValues.getUniquenessValue(150);
		assertEquals("Uniqueness Value", 0.9f, uv, 0);
		float pv = uniquenessValues.getPropabilityValue(150);
		assertEquals("Probability Value", 0.1f, pv, 0);
	}

	@Test
	public void testRemove_1() {

		uniquenessValues.remove(56);
	}
}
