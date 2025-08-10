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

public class IonUniquenessValues_1_Test {

	private IIonUniquenessValues uniquenessValues = new IonUniquenessValues();

	@Test
	public void testAdd_1() {

		uniquenessValues.add(43, 0.26f);
		// 1.0 - 0.26
		float uv = uniquenessValues.getUniquenessValue(43);
		assertEquals("Uniqueness Value", 0.74f, uv, 0);
		float pv = uniquenessValues.getPropabilityValue(43);
		assertEquals("Probability Value", 0.26f, pv, 0);
	}

	@Test
	public void testRemove_1() {

		uniquenessValues.add(43, 0.26f);
		uniquenessValues.remove(43);
		float uv = uniquenessValues.getUniquenessValue(43);
		assertEquals("Uniqueness Value", 1.0f, uv, 0);
		float pv = uniquenessValues.getPropabilityValue(43);
		assertEquals("Probability Value", 0.0f, pv, 0);
	}

	@Test
	public void testGetUniquenessValue_1() {

		float uv = uniquenessValues.getUniquenessValue(43);
		assertEquals("Uniqueness Value", 1.0f, uv, 0);
	}

	@Test
	public void testGetPropabilityValue_1() {

		float pv = uniquenessValues.getPropabilityValue(43);
		assertEquals("Probability Value", 0.0f, pv, 0);
	}
}
