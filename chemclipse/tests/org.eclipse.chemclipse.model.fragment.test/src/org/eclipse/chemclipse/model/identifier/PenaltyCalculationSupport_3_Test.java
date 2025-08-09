/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.identifier;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PenaltyCalculationSupport_3_Test {

	@Test
	public void test1() {

		float unknown = 1397.0f;
		float reference = 1406.0f; // <- RI
		float window = 10.0f;
		float penaltyCalculationLevelFactor = 5.0f;
		float maxValue = 30.0f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown, reference, window, penaltyCalculationLevelFactor, maxValue);
		assertEquals(0.0f, value, 0); // windowRangeCount is < 1
	}

	@Test
	public void test2() {

		float unknown = 1397.0f;
		float reference = 1407.0f; // <- RI
		float window = 10.0f;
		float penaltyCalculationLevelFactor = 5.0f;
		float maxValue = 30.0f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown, reference, window, penaltyCalculationLevelFactor, maxValue);
		assertEquals(0.0f, value, 0); // windowRangeCount is 1
	}

	@Test
	public void test3() {

		float unknown = 1397.0f;
		float reference = 1408.0f; // <- RI
		float window = 10.0f;
		float penaltyCalculationLevelFactor = 5.0f;
		float maxValue = 30.0f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown, reference, window, penaltyCalculationLevelFactor, maxValue);
		assertEquals(0.5f, value, 0); // windowRangeCount is 1.1
	}

	@Test
	public void test4() {

		float unknown = 1397.0f;
		float reference = 1409.0f; // <- RI
		float window = 10.0f;
		float penaltyCalculationLevelFactor = 5.0f;
		float maxValue = 30.0f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown, reference, window, penaltyCalculationLevelFactor, maxValue);
		assertEquals(1.0f, value, 0); // windowRangeCount is 1.2
	}

	@Test
	public void test5() {

		float unknown = 1397.0f;
		float reference = 1410.0f; // <- RI
		float window = 10.0f;
		float penaltyCalculationLevelFactor = 5.0f;
		float maxValue = 30.0f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown, reference, window, penaltyCalculationLevelFactor, maxValue);
		assertEquals(1.5f, value, 0); // windowRangeCount is 1.3
	}
}