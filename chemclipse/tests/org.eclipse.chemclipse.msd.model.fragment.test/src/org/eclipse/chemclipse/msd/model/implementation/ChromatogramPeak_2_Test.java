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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.junit.Before;
import org.junit.Test;

/**
 * The chromatogram and peak will be initialized in DefaultPeakTestCase.<br/>
 * The peak has 15 scans, starting at a retention time of 1500 milliseconds (ms)
 * and ends at a retention time of 15500 ms.<br/>
 * The chromatogram has 17 scans, starting at a retention time of 500 ms and
 * ends at a retention time of 16500 ms. It has a background of 1750 units.
 */
public class ChromatogramPeak_2_Test extends ChromatogramPeakTestCase {

	private IChromatogramPeakMSD peak;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
	}

	@Test
	public void testConstructor_1() {

		peak = new ChromatogramPeakMSD(getPeakModel(), getChromatogram());
		assertNotNull(peak);
	}

	@Test
	public void testConstructor_2() {

		assertThrows(IllegalArgumentException.class, () -> {
			peak = new ChromatogramPeakMSD(null, getChromatogram());
		});
	}

	@Test
	public void testConstructor_3() {

		assertThrows(IllegalArgumentException.class, () -> {
			peak = new ChromatogramPeakMSD(getPeakModel(), null);
		});
	}

	@Test
	public void testConstructor_4() {

		Exception e = assertThrows(Exception.class, () -> new ChromatogramPeakMSD(null, null));
		assertTrue(e instanceof IllegalArgumentException || e instanceof PeakException);
	}
}
