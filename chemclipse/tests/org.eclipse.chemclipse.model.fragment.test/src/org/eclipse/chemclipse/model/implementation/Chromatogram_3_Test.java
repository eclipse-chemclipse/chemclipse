/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.core.IScan;
import org.junit.Before;
import org.junit.Test;

public class Chromatogram_3_Test {

	private Chromatogram chromatogram;

	@Before
	public void setUp() throws Exception {

		chromatogram = new Chromatogram();
		chromatogram.addScan(new Scan(1000.0f));
		chromatogram.addScan(new Scan(1000.0f));
		chromatogram.addScan(new Scan(1000.0f));
	}

	@Test
	public void testChromatogram_1() {

		assertFalse(chromatogram.containsScanCycles());
	}

	@Test
	public void testChromatogram_2() {

		IScan scan = new Scan(1000.0f);
		scan.setCycleNumber(1);
		chromatogram.addScan(scan);
		assertFalse(chromatogram.containsScanCycles());
	}

	@Test
	public void testChromatogram_3() {

		IScan scan = new Scan(1000.0f);
		scan.setCycleNumber(0);
		chromatogram.addScan(scan);
		assertTrue(chromatogram.containsScanCycles());
	}

	@Test
	public void testChromatogram_4() {

		IScan scan = new Scan(1000.0f);
		scan.setCycleNumber(2);
		chromatogram.addScan(scan);
		assertTrue(chromatogram.containsScanCycles());
	}
}
