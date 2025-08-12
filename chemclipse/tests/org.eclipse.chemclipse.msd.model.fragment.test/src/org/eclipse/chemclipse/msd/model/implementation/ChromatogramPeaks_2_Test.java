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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.msd.model.core.PeaksMSD;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramPeaks_2_Test extends ChromatogramPeaksTestCase {

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
	public void testGetPeak_1() {

		peak = peaks.getPeak(1);
		assertTrue(peak.equals(getPeak1()));
	}

	@Test
	public void testGetPeak_2() {

		peak = peaks.getPeak(2);
		assertTrue(peak.equals(getPeak2()));
	}

	@Test
	public void testGetPeak_3() {

		peak = peaks.getPeak(3);
		assertTrue(peak.equals(getPeak3()));
	}

	@Test
	public void testGetPeak_4() {

		peak = peaks.getPeak(0);
		assertNull(peak);
	}

	@Test
	public void testGetPeak_5() {

		peak = peaks.getPeak(4);
		assertNull(peak);
	}
}
