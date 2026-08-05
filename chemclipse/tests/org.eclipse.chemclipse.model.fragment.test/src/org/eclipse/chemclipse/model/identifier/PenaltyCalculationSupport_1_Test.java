/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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
public class PenaltyCalculationSupport_1_Test {

	private IScan unknown;
	private IScan reference;

	@BeforeAll
	public void setUp() {

		unknown = new Scan(1000.0f);
		reference = new Scan(1000.0f);
	}

	/*
	 * Tests 1 - 3 were instance null tests. The method has been removed in the PenaltyCalculationSupport.
	 */

	@Test
	public void test4() {

		unknown.setRetentionIndex(2500.5f);
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
	public void test5() {

		unknown.setRetentionIndex(2500.5f);
		reference.setRetentionIndex(2506.5f);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_INDEX);
		penaltyCalculationSettings.setPenaltyWindow(5.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(2.0f, value, 0);
	}

	@Test
	public void test6() {

		unknown.setRetentionIndex(2500.5f);
		reference.setRetentionIndex(2515.4f);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_INDEX);
		penaltyCalculationSettings.setPenaltyWindow(5.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(19.799805f, value, 0);
	}

	@Test
	public void test7() {

		unknown.setRetentionIndex(2500.5f);
		reference.setRetentionIndex(2515.5f);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_INDEX);
		penaltyCalculationSettings.setPenaltyWindow(5.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(20.0f, value, 0);
	}

	@Test
	public void test8() {

		unknown.setRetentionIndex(2500.5f);
		reference.setRetentionIndex(2515.6f);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_INDEX);
		penaltyCalculationSettings.setPenaltyWindow(5.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(20.0f, value, 0);
	}

	@Test
	public void test9() {

		unknown.setRetentionIndex(2500.5f);
		reference.setRetentionIndex(2515.4f);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_INDEX);
		penaltyCalculationSettings.setPenaltyWindow(0.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(0.0f, value, 0);
	}

	@Test
	public void test10() {

		unknown.setRetentionIndex(2500.5f);
		reference.setRetentionIndex(2515.4f);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_INDEX);
		penaltyCalculationSettings.setPenaltyWindow(5.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(-0.1f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(0.0f, value, 0);
	}

	@Test
	public void test11() {

		unknown.setRetentionIndex(2500.5f);
		reference.setRetentionIndex(2515.4f);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_INDEX);
		penaltyCalculationSettings.setPenaltyWindow(5.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(100.1f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(0.0f, value, 0);
	}
}