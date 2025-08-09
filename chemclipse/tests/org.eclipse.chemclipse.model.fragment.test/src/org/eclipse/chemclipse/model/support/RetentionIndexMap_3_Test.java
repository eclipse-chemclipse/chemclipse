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
import static org.junit.Assert.assertFalse;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.implementation.Chromatogram;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.junit.Before;
import org.junit.Test;

public class RetentionIndexMap_3_Test {

	private RetentionIndexMap retentionIndexMap = new RetentionIndexMap();

	@Before
	public void setUp() throws Exception {

		/*
		 * Start
		 * C5 - C53
		 * Stop
		 */
		IChromatogram chromatogram = new Chromatogram();
		for(int i = 1, j = 499; i < 5000; i++, j++) {
			IScan scan = new Scan((float)Math.random());
			scan.setRetentionTime(i * 750);
			scan.setRetentionIndex(j);
			if(j % 100 != 0) {
				chromatogram.addScan(scan);
			}
		}
		retentionIndexMap.update(chromatogram);
	}

	@Test
	public void test1() {

		assertEquals(51, retentionIndexMap.getSeparationColumnIndices().size());
	}

	@Test
	public void test2() {

		assertEquals(500.0f, retentionIndexMap.getRetentionIndex(1500), 0); // C5
	}

	@Test
	public void test3() {

		assertEquals(1500, retentionIndexMap.getRetentionTime(500)); // C5
	}

	@Test
	public void test4() {

		assertEquals(3600.0f, retentionIndexMap.getRetentionIndex(2326500), 0); // C36
	}

	@Test
	public void test5() {

		assertEquals(2326500, retentionIndexMap.getRetentionTime(3600)); // C36
	}

	@Test
	public void test6() {

		assertEquals(5400.0f, retentionIndexMap.getRetentionIndex(3676500), 0); // C54
	}

	@Test
	public void test7() {

		assertEquals(3676500, retentionIndexMap.getRetentionTime(5400)); // C54
	}

	@Test
	public void test8() {

		assertFalse(retentionIndexMap.isEmpty());
	}

	@Test
	public void test9() {

		assertEquals(499, retentionIndexMap.getRetentionIndexStart());
	}

	@Test
	public void test10() {

		assertEquals(5497, retentionIndexMap.getRetentionIndexStop());
	}
}
