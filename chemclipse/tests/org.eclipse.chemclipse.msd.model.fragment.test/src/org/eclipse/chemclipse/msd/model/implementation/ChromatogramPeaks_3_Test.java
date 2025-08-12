/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.msd.model.core.PeaksMSD;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramPeaks_3_Test extends ChromatogramPeaksTestCase {

	private IPeaksMSD peaks;
	private IPeakMSD peak;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		peaks = new PeaksMSD();
		peaks.addPeak(getPeak1());
		peaks.addPeak(getPeak2());
		peaks.addPeak(getPeak3());
	}

	@Test
	public void testSize_1() {

		assertEquals(3, peaks.getPeaks().size());
	}

	@Test
	public void testRemovePeak_1() {

		peaks.removePeak(getPeak1());
		assertEquals(2, peaks.getPeaks().size());
		// Peak 2
		peak = peaks.getPeak(1);
		assertTrue(peak.equals(getPeak2()));
		// Peak 3
		peak = peaks.getPeak(2);
		assertTrue(peak.equals(getPeak3()));
	}

	@Test
	public void testRemovePeak_2() {

		peaks.removePeak(getPeak2());
		assertEquals(2, peaks.getPeaks().size());
		// Peak 1
		peak = peaks.getPeak(1);
		assertTrue(peak.equals(getPeak1()));
		// Peak 3
		peak = peaks.getPeak(2);
		assertTrue(peak.equals(getPeak3()));
	}

	@Test
	public void testRemovePeak_3() {

		peaks.removePeak(getPeak3());
		assertEquals(2, peaks.getPeaks().size());
		// Peak 1
		peak = peaks.getPeak(1);
		assertTrue(peak.equals(getPeak1()));
		// Peak 2
		peak = peaks.getPeak(2);
		assertTrue(peak.equals(getPeak2()));
	}
}
