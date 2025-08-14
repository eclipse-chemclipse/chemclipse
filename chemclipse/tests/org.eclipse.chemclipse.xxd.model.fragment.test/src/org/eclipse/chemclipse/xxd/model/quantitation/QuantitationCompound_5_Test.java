/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.quantitation.CalibrationMethod;
import org.junit.Before;
import org.junit.Test;

public class QuantitationCompound_5_Test {

	private QuantitationCompound quantitationCompound;

	@Before
	public void setUp() {

		quantitationCompound = new QuantitationCompound("", "", 0);
	}

	@Test
	public void test1() {

		assertNotNull(quantitationCompound);
	}

	@Test
	public void test2() {

		assertEquals("", quantitationCompound.getName());
	}

	@Test
	public void test3() {

		assertEquals("", quantitationCompound.getChemicalClass());
	}

	@Test
	public void test4() {

		assertEquals(0, quantitationCompound.getRetentionTimeWindow().getRetentionTime());
	}

	@Test
	public void test5() {

		assertEquals(0.0f, quantitationCompound.getRetentionTimeWindow().getAllowedNegativeDeviation(), 0);
	}

	@Test
	public void test6() {

		assertEquals(0.0f, quantitationCompound.getRetentionTimeWindow().getAllowedPositiveDeviation(), 0);
	}

	@Test
	public void test7() {

		assertEquals(0.0f, quantitationCompound.getRetentionIndexWindow().getRetentionIndex(), 0);
	}

	@Test
	public void test8() {

		assertEquals(0.0f, quantitationCompound.getRetentionIndexWindow().getAllowedNegativeDeviation(), 0);
	}

	@Test
	public void test9() {

		assertEquals(0.0f, quantitationCompound.getRetentionIndexWindow().getAllowedPositiveDeviation(), 0);
	}

	@Test
	public void test10() {

		assertEquals("", quantitationCompound.getConcentrationUnit());
	}

	@Test
	public void test11() {

		assertTrue(quantitationCompound.isUseTIC());
	}

	@Test
	public void test12() {

		assertEquals(CalibrationMethod.LINEAR, quantitationCompound.getCalibrationMethod());
	}

	@Test
	public void test13() {

		assertTrue(quantitationCompound.isCrossZero());
	}

	@Test
	public void test14() {

		assertEquals(0, quantitationCompound.getQuantitationSignals().size());
	}

	@Test
	public void test15() {

		assertEquals(0, quantitationCompound.getResponseSignals().size());
	}

	@Test
	public void test16() {

		assertEquals(0, quantitationCompound.getQuantitationPeaks().size());
	}
}
