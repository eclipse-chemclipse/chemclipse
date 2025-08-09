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

public class Scan_6_Test {

	private IScan scan;

	@Before
	public void setUp() throws Exception {

		scan = new Scan(1000.0f);
	}

	@Test
	public void test_1() {

		assertEquals(0, scan.getRelativeRetentionTime());
	}

	@Test
	public void test_2() {

		scan.setRelativeRetentionTime(-1);
		assertEquals(0, scan.getRelativeRetentionTime());
	}

	@Test
	public void test_3() {

		scan.setRelativeRetentionTime(0);
		assertEquals(0, scan.getRelativeRetentionTime());
	}

	@Test
	public void test_4() {

		scan.setRelativeRetentionTime(1);
		assertEquals(1, scan.getRelativeRetentionTime());
	}
}
