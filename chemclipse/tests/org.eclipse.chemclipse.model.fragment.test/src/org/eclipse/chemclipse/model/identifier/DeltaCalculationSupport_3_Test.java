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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.junit.Before;
import org.junit.Test;

public class DeltaCalculationSupport_3_Test {

	private IScan unknown;
	private IScan reference;
	private IDeltaCalculationSettings identifierSettings;

	@Before
	public void setUp() throws Exception {

		unknown = new Scan(1000.0f);
		reference = new Scan(1000.0f);
		identifierSettings = new AbstractIdentifierDeltaPenaltyCalculationSettings();
		identifierSettings.setDeltaCalculation(DeltaCalculation.RETENTION_TIME_MS);
		identifierSettings.setDeltaWindow(1000.0f);
	}

	@Test
	public void test1() {

		unknown.setRetentionTime(6000);
		unknown.setRetentionIndex(600.0f);
		reference.setRetentionTime(6000);
		reference.setRetentionIndex(600.0f);
		assertTrue(DeltaCalculationSupport.useTarget(unknown, reference, identifierSettings));
	}

	@Test
	public void test2() {

		unknown.setRetentionTime(6000);
		unknown.setRetentionIndex(600.0f);
		reference.setRetentionTime(5000);
		reference.setRetentionIndex(500.0f);
		assertTrue(DeltaCalculationSupport.useTarget(unknown, reference, identifierSettings));
	}

	@Test
	public void test3() {

		unknown.setRetentionTime(6000);
		unknown.setRetentionIndex(600.0f);
		reference.setRetentionTime(7000);
		reference.setRetentionIndex(700.0f);
		assertTrue(DeltaCalculationSupport.useTarget(unknown, reference, identifierSettings));
	}

	@Test
	public void test4() {

		unknown.setRetentionTime(6000);
		unknown.setRetentionIndex(600.0f);
		reference.setRetentionTime(4999);
		reference.setRetentionIndex(499.9f);
		assertFalse(DeltaCalculationSupport.useTarget(unknown, reference, identifierSettings));
	}

	@Test
	public void test5() {

		unknown.setRetentionTime(6000);
		unknown.setRetentionIndex(600.0f);
		reference.setRetentionTime(7001);
		reference.setRetentionIndex(700.1f);
		assertFalse(DeltaCalculationSupport.useTarget(unknown, reference, identifierSettings));
	}
}
