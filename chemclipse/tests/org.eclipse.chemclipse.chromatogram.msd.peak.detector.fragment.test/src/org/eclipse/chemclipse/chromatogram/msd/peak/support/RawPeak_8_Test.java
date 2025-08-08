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

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.chromatogram.peak.detector.support.IRawPeak;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.RawPeak;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the raw peak.
 * 
 * @author Philip Wenig
 */
public class RawPeak_8_Test {

	IRawPeak rawPeak;
	int startScan;
	int maximumScan;
	int stopScan;

	@Before
	public void setUp() throws Exception {

		startScan = 14;
		stopScan = 20;
		maximumScan = 21;
		rawPeak = new RawPeak(startScan, maximumScan, stopScan);
	}

	@Test
	public void testGetRetentionTimeAtMaximum_1() {

		assertEquals("RetentionTimeAtMaximum", 0, rawPeak.getRetentionTimeAtMaximum());
	}

	@Test
	public void testGetRetentionTimeAtMaximum_2() {

		rawPeak.setRetentionTimeAtMaximum(2562);
		assertEquals("RetentionTimeAtMaximum", 2562, rawPeak.getRetentionTimeAtMaximum());
	}

	@Test
	public void testGetRetentionTimeAtMaximum_3() {

		rawPeak.setRetentionTimeAtMaximum(-1);
		assertEquals("RetentionTimeAtMaximum", 0, rawPeak.getRetentionTimeAtMaximum());
	}

	@Test
	public void testGetRetentionTimeAtMaximum_4() {

		rawPeak.setRetentionTimeAtMaximum(2562);
		rawPeak.setRetentionTimeAtMaximum(0);
		assertEquals("RetentionTimeAtMaximum", 0, rawPeak.getRetentionTimeAtMaximum());
	}
}
