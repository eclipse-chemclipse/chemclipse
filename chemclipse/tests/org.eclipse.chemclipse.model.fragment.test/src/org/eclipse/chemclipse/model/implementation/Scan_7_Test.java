/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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

public class Scan_7_Test {

	private IScan scan;

	@Before
	public void setUp() throws Exception {

		scan = new Scan(1000.0f);
	}

	@Test
	public void test_1() {

		assertEquals(0.0f, scan.getRetentionIndex(), 0);
	}

	@Test
	public void test_2() {

		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.NON_POLAR), 0);
	}

	@Test
	public void test_3() {

		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.POLAR), 0);
	}

	@Test
	public void test_4() {

		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.SEMI_POLAR), 0);
	}

	@Test
	public void test_5() {

		scan.setRetentionIndex(0.1f);
		assertEquals(0.1f, scan.getRetentionIndex(), 0);
	}

	@Test
	public void test_6() {

		scan.setRetentionIndex(SeparationColumnType.NON_POLAR, 0.1f);
		assertEquals(0.1f, scan.getRetentionIndex(SeparationColumnType.NON_POLAR), 0);
	}

	@Test
	public void test_7() {

		scan.setRetentionIndex(SeparationColumnType.POLAR, 0.1f);
		assertEquals(0.1f, scan.getRetentionIndex(SeparationColumnType.POLAR), 0);
	}

	@Test
	public void test_8() {

		scan.setRetentionIndex(SeparationColumnType.SEMI_POLAR, 0.1f);
		assertEquals(0.1f, scan.getRetentionIndex(SeparationColumnType.SEMI_POLAR), 0);
	}

	@Test
	public void test_9() {

		scan.setRetentionIndex(0.1f);
		scan.setRetentionIndex(0.0f);
		assertEquals(0.0f, scan.getRetentionIndex(), 0);
	}

	@Test
	public void test_10() {

		scan.setRetentionIndex(SeparationColumnType.NON_POLAR, 0.1f);
		scan.setRetentionIndex(SeparationColumnType.NON_POLAR, 0.0f);
		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.NON_POLAR), 0);
	}

	@Test
	public void test_11() {

		scan.setRetentionIndex(SeparationColumnType.POLAR, 0.1f);
		scan.setRetentionIndex(SeparationColumnType.POLAR, 0.0f);
		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.POLAR), 0);
	}

	@Test
	public void test_12() {

		scan.setRetentionIndex(SeparationColumnType.SEMI_POLAR, 0.1f);
		scan.setRetentionIndex(SeparationColumnType.SEMI_POLAR, 0.0f);
		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.SEMI_POLAR), 0);
	}

	@Test
	public void test_13() {

		scan.setRetentionIndex(0.1f);
		scan.setRetentionIndex(-0.1f);
		assertEquals(0.0f, scan.getRetentionIndex(), 0);
	}

	@Test
	public void test_14() {

		scan.setRetentionIndex(SeparationColumnType.NON_POLAR, 0.1f);
		scan.setRetentionIndex(SeparationColumnType.NON_POLAR, -0.1f);
		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.NON_POLAR), 0);
	}

	@Test
	public void test_15() {

		scan.setRetentionIndex(SeparationColumnType.POLAR, 0.1f);
		scan.setRetentionIndex(SeparationColumnType.POLAR, -0.1f);
		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.POLAR), 0);
	}

	@Test
	public void test_16() {

		scan.setRetentionIndex(SeparationColumnType.SEMI_POLAR, 0.1f);
		scan.setRetentionIndex(SeparationColumnType.SEMI_POLAR, -0.1f);
		assertEquals(0.0f, scan.getRetentionIndex(SeparationColumnType.SEMI_POLAR), 0);
	}
}