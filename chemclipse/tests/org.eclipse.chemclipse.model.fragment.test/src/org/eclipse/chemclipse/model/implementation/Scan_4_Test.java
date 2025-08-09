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
import org.junit.Before;
import org.junit.Test;

public class Scan_4_Test {

	private IScan scan;

	@Before
	public void setUp() throws Exception {

		scan = new Scan(1000.0f);
	}

	@Test
	public void test_1() {

		scan.setRetentionTimeColumn1(2000);
		assertEquals(2000, scan.getRetentionTimeColumn1());
	}

	@Test
	public void test_2() {

		scan.setRetentionTimeColumn1(-1);
		assertEquals(0, scan.getRetentionTimeColumn1());
	}

	@Test
	public void test_3() {

		scan.setRetentionTimeColumn1(0);
		assertEquals(0, scan.getRetentionTimeColumn1());
	}

	@Test
	public void test_4() {

		scan.setRetentionTimeColumn2(2130);
		assertEquals(2130, scan.getRetentionTimeColumn2());
	}

	@Test
	public void test_5() {

		scan.setRetentionTimeColumn2(-1);
		assertEquals(0, scan.getRetentionTimeColumn2());
	}

	@Test
	public void test_6() {

		scan.setRetentionTimeColumn2(0);
		assertEquals(0, scan.getRetentionTimeColumn2());
	}
}
