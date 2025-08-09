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
package org.eclipse.chemclipse.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.junit.Before;
import org.junit.Test;

public class Scan_5_Test {

	private IScan scan;

	@Before
	public void setUp() throws Exception {

		scan = new Scan(1000.0f);
	}

	@Test
	public void test_1() {

		assertEquals(false, scan.hasAdditionalRetentionIndices());
	}

	@Test
	public void test_2() {

		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.POLAR), 0);
	}

	@Test
	public void test_3() {

		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.SEMI_POLAR), 0);
	}

	@Test
	public void test_4() {

		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.NON_POLAR), 0);
	}

	@Test
	public void test_5() {

		scan.setRetentionIndex(SeparationColumnType.POLAR, 3456.34f);
		assertEquals(true, scan.hasAdditionalRetentionIndices());
		assertEquals(3456.34f, scan.getRetentionIndex(SeparationColumnType.POLAR), 0);
	}

	@Test
	public void test_6() {

		scan.setRetentionIndex(SeparationColumnType.SEMI_POLAR, 2358.22f);
		assertEquals(true, scan.hasAdditionalRetentionIndices());
		assertEquals(2358.22f, scan.getRetentionIndex(SeparationColumnType.SEMI_POLAR), 0);
	}

	@Test
	public void test_7() {

		scan.setRetentionIndex(SeparationColumnType.NON_POLAR, 789.11f);
		assertEquals(true, scan.hasAdditionalRetentionIndices());
		assertEquals(789.11f, scan.getRetentionIndex(SeparationColumnType.NON_POLAR), 0);
	}

	@Test
	public void test_8() {

		scan.setRetentionIndex(SeparationColumnType.POLAR, 3456.34f);
		scan.setRetentionIndex(SeparationColumnType.SEMI_POLAR, 2358.22f);
		scan.setRetentionIndex(SeparationColumnType.NON_POLAR, 789.11f);
		assertEquals(true, scan.hasAdditionalRetentionIndices());
		assertEquals(3456.34f, scan.getRetentionIndex(SeparationColumnType.POLAR), 0);
		assertEquals(2358.22f, scan.getRetentionIndex(SeparationColumnType.SEMI_POLAR), 0);
		assertEquals(789.11f, scan.getRetentionIndex(SeparationColumnType.NON_POLAR), 0);
	}
}