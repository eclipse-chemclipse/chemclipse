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
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalsModifier;
import org.junit.Before;
import org.junit.Test;

public class TotalIonSignalsModifier_6_Test {

	private ITotalScanSignals signals;
	private ITotalScanSignal signal;

	@Before
	public void setUp() {

		signals = new TotalScanSignals(4);
		float[] abundance = new float[4];
		abundance[0] = 4512.3f;
		abundance[1] = 57822.4f;
		abundance[2] = 47556.2f;
		abundance[3] = 23354.2f;
		for(int i = 1; i <= 4; i++) {
			signal = new TotalScanSignal(i * 100, 0.0f, abundance[i - 1]);
			signals.add(signal);
		}
		TotalScanSignalsModifier.calculateMovingAverage(signals, 5);
	}

	@Test
	public void testSize_1() {

		assertEquals("size", 4, signals.size());
	}

	@Test
	public void testGetPoint_1() {

		int scan = 1;
		ITotalScanSignal s1 = signals.getTotalScanSignal(scan);
		assertEquals("total signal", 4512.3f, s1.getTotalSignal(), 0);
	}

	@Test
	public void testGetPoint_2() {

		int scan = 2;
		ITotalScanSignal s1 = signals.getTotalScanSignal(scan);
		assertEquals("total signal", 57822.4f, s1.getTotalSignal(), 0);
	}

	@Test
	public void testGetPoint_3() {

		int scan = 3;
		ITotalScanSignal s1 = signals.getTotalScanSignal(scan);
		assertEquals("total signal", 47556.2f, s1.getTotalSignal(), 0);
	}

	@Test
	public void testGetPoint_4() {

		int scan = 4;
		ITotalScanSignal s1 = signals.getTotalScanSignal(scan);
		assertEquals("total signal", 23354.2f, s1.getTotalSignal(), 0);
	}
}
