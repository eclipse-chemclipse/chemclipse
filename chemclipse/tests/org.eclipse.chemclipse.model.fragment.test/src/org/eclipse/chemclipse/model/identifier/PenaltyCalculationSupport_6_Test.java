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
package org.eclipse.chemclipse.model.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class PenaltyCalculationSupport_6_Test {

	private IScan unknown;
	private IScan reference;

	@BeforeAll
	public void setUp() {

		unknown = new Scan(1000.0f);
		reference = new Scan(1000.0f);
	}

	@Test
	public void test1() {

		unknown.setRetentionIndex(0.0f);
		reference.setRetentionIndex(2505.5f);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_INDEX);
		penaltyCalculationSettings.setPenaltyWindow(5.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(0.0f, value, 0);
	}

	@Test
	public void test2() {

		unknown.setRetentionIndex(0.0f);
		reference.setRetentionIndex(0.0f);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_INDEX);
		penaltyCalculationSettings.setPenaltyWindow(5.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(0.0f, value, 0);
	}

	@Test
	public void test3() {

		unknown.setRetentionTime(0);
		reference.setRetentionTime(3000);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_TIME_MS);
		penaltyCalculationSettings.setPenaltyWindow(500.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(0.0f, value, 0);
	}

	@Test
	public void test4() {

		unknown.setRetentionTime(0);
		reference.setRetentionTime(0);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_TIME_MS);
		penaltyCalculationSettings.setPenaltyWindow(500.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(0.0f, value, 0);
	}
}