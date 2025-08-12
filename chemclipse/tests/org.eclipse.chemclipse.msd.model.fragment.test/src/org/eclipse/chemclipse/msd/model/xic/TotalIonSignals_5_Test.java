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

import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.junit.Test;

public class TotalIonSignals_5_Test {

	private ITotalScanSignals signals = new TotalScanSignals(0);

	@Test
	public void testSize_1() {

		assertEquals("size", 0, signals.size());
	}

	@Test
	public void testGetMinMax_1() {

		assertEquals("max signal", 0.0f, signals.getMaxSignal(), 0);
		assertEquals("min signal", 0.0f, signals.getMinSignal(), 0);
	}
}
