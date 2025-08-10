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

import org.eclipse.chemclipse.model.signals.ExtendedTotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.junit.Test;

/**
 * Test methods of ExtendedTotalIonSignal.
 */
public class ExtendedTotalIonSignal_1_Test {

	@Test
	public void testConstructor_1() {

		ITotalScanSignal totalIonSignal = new ExtendedTotalScanSignal(1000, 0.0f, -5949.3f);
		assertEquals("Total Signal", -5949.3f, totalIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testSetTotalSignal_1() {

		ITotalScanSignal totalIonSignal = new ExtendedTotalScanSignal(1000, 0.0f, 0.0f);
		totalIonSignal.setTotalSignal(-5949.3f);
		assertEquals("Total Signal", -5949.3f, totalIonSignal.getTotalSignal(), 0);
	}
}
