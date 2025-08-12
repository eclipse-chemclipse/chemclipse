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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.signals.ExtendedTotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.junit.Before;
import org.junit.Test;

public class TotalIonSignals_10_Test {

	private ITotalScanSignals signals;
	private ITotalScanSignal signal;

	@Before
	public void setUp() {

		/*
		 * Ok, we add some negative value here, but
		 */
		List<Float> totalSignals = new ArrayList<Float>();
		totalSignals.add(500.0f);
		totalSignals.add(750.0f);
		totalSignals.add(200.0f);
		totalSignals.add(-300.0f);
		totalSignals.add(-500.0f);
		totalSignals.add(-20.0f);
		totalSignals.add(5500.0f);
		totalSignals.add(4500.0f);
		totalSignals.add(-24500.0f);
		totalSignals.add(3500.0f);
		signals = new TotalScanSignals(10);
		int rt = 0;
		for(int scan = 0; scan < 10; scan++) {
			rt += 1000;
			signal = new ExtendedTotalScanSignal(rt, 0.0f, totalSignals.get(scan));
			signals.add(signal);
		}
	}

	@Test
	public void testSetNegativeTotalSignalsToZero_1() {

		signals.setNegativeTotalSignalsToZero();
		float min = signals.getMinSignal();
		float max = signals.getMaxSignal();
		assertEquals("Min", 0.0f, min, 0);
		assertEquals("Max", 5500.0f, max, 0);
	}

	@Test
	public void testSetPositiveTotalSignalsToZero_1() {

		signals.setPositiveTotalSignalsToZero();
		float min = signals.getMinSignal();
		float max = signals.getMaxSignal();
		assertEquals("Min", -24500.0f, min, 0);
		assertEquals("Max", 0.0f, max, 0);
	}

	@Test
	public void testTotalSignalsAsAbsoluteValues_1() {

		signals.setTotalSignalsAsAbsoluteValues();
		float min = signals.getMinSignal();
		float max = signals.getMaxSignal();
		assertEquals("Min", 20.0f, min, 0);
		assertEquals("Max", 24500.0f, max, 0);
	}
}
