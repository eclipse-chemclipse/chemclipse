/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.peak.support;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.chemclipse.chromatogram.peak.detector.support.IRawPeak;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.RawPeak;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the raw peak.
 * 
 * @author Philip Wenig
 */
public class RawPeak_3_Test {

	IRawPeak rawPeak1, rawPeak2;
	int startScan;
	int maximumScan;
	int stopScan;

	@Before
	public void setUp() throws Exception {

		startScan = 5;
		stopScan = 20;
		maximumScan = 15;
		rawPeak1 = new RawPeak(startScan, maximumScan, stopScan);
		rawPeak2 = new RawPeak(4, maximumScan, stopScan);
	}

	@Test
	public void testEquals_1() {

		assertNotEquals("equals", rawPeak1, rawPeak2);
	}

	@Test
	public void testEquals_2() {

		assertNotEquals("equals", rawPeak2, rawPeak1);
	}

	@Test
	public void testEquals_3() {

		assertNotNull("equals", rawPeak1);
	}

	@Test
	public void testEquals_4() {

		assertNotEquals("equals", rawPeak1, new Object());
	}
}
