/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.junit.Before;
import org.junit.Test;

public class PenaltyCalculationSupport_2_Test {

	private IScan unknown;
	private IScan reference;
	private int retentionTimeWindow;
	private float penaltyCalculationLevelFactor;
	private float penaltyCalculationMaxValue;

	@Before
	public void setUp() throws Exception {

		unknown = new Scan(1000.0f);
		reference = new Scan(1000.0f);
	}

	@Test
	public void test4() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(3000);
		retentionTimeWindow = 500;
		penaltyCalculationLevelFactor = 10.0f;
		penaltyCalculationMaxValue = 20.0f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown.getRetentionTime(), reference.getRetentionTime(), retentionTimeWindow, penaltyCalculationLevelFactor, penaltyCalculationMaxValue);
		assertEquals(0.0f, value, 0);
	}

	@Test
	public void test5() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(3001);
		retentionTimeWindow = 500;
		penaltyCalculationLevelFactor = 10.0f;
		penaltyCalculationMaxValue = 20.0f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown.getRetentionTime(), reference.getRetentionTime(), retentionTimeWindow, penaltyCalculationLevelFactor, penaltyCalculationMaxValue);
		assertEquals(0.02f, value, 0);
	}

	@Test
	public void test6() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(3999);
		retentionTimeWindow = 500;
		penaltyCalculationLevelFactor = 10.0f;
		penaltyCalculationMaxValue = 20.0f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown.getRetentionTime(), reference.getRetentionTime(), retentionTimeWindow, penaltyCalculationLevelFactor, penaltyCalculationMaxValue);
		assertEquals(19.98f, value, 0);
	}

	@Test
	public void test7() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(4000);
		retentionTimeWindow = 500;
		penaltyCalculationLevelFactor = 10.0f;
		penaltyCalculationMaxValue = 20.0f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown.getRetentionTime(), reference.getRetentionTime(), retentionTimeWindow, penaltyCalculationLevelFactor, penaltyCalculationMaxValue);
		assertEquals(20.0f, value, 0);
	}

	@Test
	public void test8() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(4001);
		retentionTimeWindow = 500;
		penaltyCalculationLevelFactor = 10.0f;
		penaltyCalculationMaxValue = 20.0f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown.getRetentionTime(), reference.getRetentionTime(), retentionTimeWindow, penaltyCalculationLevelFactor, penaltyCalculationMaxValue);
		assertEquals(20.0f, value, 0);
	}

	@Test
	public void test9() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(3999);
		retentionTimeWindow = 0;
		penaltyCalculationLevelFactor = 10.0f;
		penaltyCalculationMaxValue = 20.0f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown.getRetentionTime(), reference.getRetentionTime(), retentionTimeWindow, penaltyCalculationLevelFactor, penaltyCalculationMaxValue);
		assertEquals(0.0f, value, 0);
	}

	@Test
	public void test10() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(3999);
		retentionTimeWindow = 500;
		penaltyCalculationLevelFactor = 10.0f;
		penaltyCalculationMaxValue = -0.1f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown.getRetentionTime(), reference.getRetentionTime(), retentionTimeWindow, penaltyCalculationLevelFactor, penaltyCalculationMaxValue);
		assertEquals(0.0f, value, 0);
	}

	@Test
	public void test11() {

		unknown.setRetentionTime(2500);
		reference.setRetentionTime(3999);
		retentionTimeWindow = 500;
		penaltyCalculationLevelFactor = 10.0f;
		penaltyCalculationMaxValue = 100.1f;
		float value = (float)PenaltyCalculationSupport.calculatePenalty(unknown.getRetentionTime(), reference.getRetentionTime(), retentionTimeWindow, penaltyCalculationLevelFactor, penaltyCalculationMaxValue);
		assertEquals(0.0f, value, 0);
	}
}
