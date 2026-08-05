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
public class PenaltyCalculationSupport_2_Test {

	private IScan unknown;
	private IScan reference;

	@BeforeAll
	public void setUp() {

		unknown = new Scan(1000.0f);
		reference = new Scan(1000.0f);
	}

	@Test
	public void test4() {

		unknown.setRetentionTime(2500);
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
	public void test5() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(3001);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_TIME_MS);
		penaltyCalculationSettings.setPenaltyWindow(500.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(0.02f, value, 0);
	}

	@Test
	public void test6() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(3999);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_TIME_MS);
		penaltyCalculationSettings.setPenaltyWindow(500.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(19.98f, value, 0);
	}

	@Test
	public void test7() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(4000);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_TIME_MS);
		penaltyCalculationSettings.setPenaltyWindow(500.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(20.0f, value, 0);
	}

	@Test
	public void test8() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(4001);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_TIME_MS);
		penaltyCalculationSettings.setPenaltyWindow(500.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(20.0f, value, 0);
	}

	@Test
	public void test9() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(3999);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_TIME_MS);
		penaltyCalculationSettings.setPenaltyWindow(0.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(20.0f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(0.0f, value, 0);
	}

	@Test
	public void test10() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(3999);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_TIME_MS);
		penaltyCalculationSettings.setPenaltyWindow(500.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(-0.1f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(0.0f, value, 0);
	}

	@Test
	public void test11() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(3999);
		IPenaltyCalculationSettings penaltyCalculationSettings = new PenaltyCalculationSettings();
		penaltyCalculationSettings.setPenaltyCalculation(PenaltyCalculation.RETENTION_TIME_MS);
		penaltyCalculationSettings.setPenaltyWindow(500.0f);
		penaltyCalculationSettings.setPenaltyLevelFactor(10.0f);
		penaltyCalculationSettings.setMaxPenalty(100.1f);
		penaltyCalculationSettings.setPenaltyMissingReference(0.0f);
		float value = PenaltyCalculationSupport.calculatePenalty(unknown, reference, penaltyCalculationSettings);
		assertEquals(0.0f, value, 0);
	}
}