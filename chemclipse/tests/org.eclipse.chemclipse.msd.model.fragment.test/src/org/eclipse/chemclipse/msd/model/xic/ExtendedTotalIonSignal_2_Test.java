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
import static org.junit.Assert.assertNotSame;

import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignal;
import org.junit.Before;
import org.junit.Test;

/**
 * Test methods of ExtendedTotalIonSignal.
 */
public class ExtendedTotalIonSignal_2_Test {

	private ITotalScanSignal totalIonSignal;

	@Before
	public void setUp() {

		totalIonSignal = new TotalScanSignal(1524, 0.0f, -346.0f);
	}

	@Test
	public void testMakeDeepCopy_1() {

		ITotalScanSignal totalIonSignal2 = totalIonSignal.makeDeepCopy();
		assertNotSame(totalIonSignal2, totalIonSignal);
		assertEquals("retention time", totalIonSignal.getRetentionTime(), totalIonSignal2.getRetentionTime());
		assertEquals("retention index", totalIonSignal.getRetentionIndex(), totalIonSignal2.getRetentionIndex(), 0);
		assertEquals("total signal", totalIonSignal.getTotalSignal(), totalIonSignal2.getTotalSignal(), 0);
	}
}
