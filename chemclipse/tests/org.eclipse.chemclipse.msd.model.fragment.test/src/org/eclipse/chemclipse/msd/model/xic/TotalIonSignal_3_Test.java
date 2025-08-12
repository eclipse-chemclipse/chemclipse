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
package org.eclipse.chemclipse.msd.model.xic;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignal;
import org.junit.Before;
import org.junit.Test;

/**
 * Test all methods of TotalIonSignal.
 */
public class TotalIonSignal_3_Test {

	private ITotalScanSignal totalIonSignal;

	@Before
	public void setUp() throws Exception {

		totalIonSignal = new TotalScanSignal(5720, 1245.5f, 25476.45f);
	}

	@Test
	public void testGetRetentionTime_1() {

		assertEquals("getRetentionTime", 5720, totalIonSignal.getRetentionTime());
	}

	@Test
	public void testGetRetentionIndex_1() {

		assertEquals("getRetentionIndex", 1245.5f, totalIonSignal.getRetentionIndex(), 0);
	}

	@Test
	public void testGetTotalSignal_1() {

		assertEquals("getTotalSignal", 25476.45f, totalIonSignal.getTotalSignal(), 0);
	}
}
