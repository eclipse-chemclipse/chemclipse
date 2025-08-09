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
package org.eclipse.chemclipse.model.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.implementation.Chromatogram;
import org.junit.Before;
import org.junit.Test;

public class RetentionIndexMap_6_Test {

	private RetentionIndexMap retentionIndexMap = new RetentionIndexMap();

	@Before
	public void setUp() throws Exception {

		IChromatogram chromatogram = new Chromatogram();
		retentionIndexMap.update(chromatogram);
	}

	@Test
	public void test1() {

		assertEquals(-1, retentionIndexMap.getRetentionTime(1000));
	}

	@Test
	public void test2() {

		assertEquals(0.0f, retentionIndexMap.getRetentionIndex(1000), 0);
	}

	@Test
	public void test3() {

		assertTrue(retentionIndexMap.isEmpty());
	}

	@Test
	public void test4() {

		assertEquals(-1, retentionIndexMap.getRetentionIndexStart());
	}

	@Test
	public void test5() {

		assertEquals(-1, retentionIndexMap.getRetentionIndexStop());
	}
}
