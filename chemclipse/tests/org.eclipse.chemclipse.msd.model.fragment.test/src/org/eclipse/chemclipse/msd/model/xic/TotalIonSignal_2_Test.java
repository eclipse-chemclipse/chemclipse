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
public class TotalIonSignal_2_Test {

	private ITotalScanSignal totalIonSignal;

	@Before
	public void setUp() {

		totalIonSignal = new TotalScanSignal(-1, -1.0f, -1.0f);
	}

	@Test
	public void testGetRetentionTime_1() {

		assertEquals("getRetentionTime", 0, totalIonSignal.getRetentionTime());
	}

	@Test
	public void testGetRetentionIndex_1() {

		assertEquals("getRetentionIndex", 0.0f, totalIonSignal.getRetentionIndex(), 0);
	}

	@Test
	public void testGetTotalSignal_1() {

		assertEquals("getTotalSignal", 0.0f, totalIonSignal.getTotalSignal(), 0);
	}
}
