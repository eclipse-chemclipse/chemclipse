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

public class TotalIonSignalsModifier_4_Test {

	private ITotalScanSignals signals;
	private ITotalScanSignal signal;

	@Before
	public void setUp() {

		signals = new TotalScanSignals(12);
		float[] abundance = new float[12];
		abundance[0] = 4512.3f;
		abundance[1] = 57822.4f;
		abundance[2] = 47556.2f;
		abundance[3] = 23354.2f;
		abundance[4] = 896116.3f;
		abundance[5] = 1245234.8f;
		abundance[6] = 785154.2f;
		abundance[7] = 655421.2f;
		abundance[8] = 1245.3f;
		abundance[9] = 512.9f;
		abundance[10] = 52.2f;
		abundance[11] = 5.9f;
		for(int i = 1; i <= 12; i++) {
			signal = new TotalScanSignal(i * 100, 0.0f, abundance[i - 1]);
			signals.add(signal);
		}
		TotalScanSignalsModifier.calculateMovingAverage(signals, 3);
	}

	@Test
	public void testSize_1() {

		assertEquals("size", 12, signals.size());
	}

	@Test
	public void testGetPoint_1() {

		int scan = 1;
		ITotalScanSignal s1 = signals.getTotalScanSignal(scan);
		assertEquals("total signal", 4512.3f, s1.getTotalSignal(), 0);
	}

	@Test
	public void testGetPoint_2() {

		int scan = 4;
		ITotalScanSignal s1 = signals.getTotalScanSignal(scan);
		assertEquals("total signal", 318439.16f, s1.getTotalSignal(), 0);
	}

	@Test
	public void testGetPoint_3() {

		int scan = 8;
		ITotalScanSignal s1 = signals.getTotalScanSignal(scan);
		assertEquals("total signal", 484520.12f, s1.getTotalSignal(), 0);
	}

	@Test
	public void testGetPoint_4() {

		int scan = 12;
		ITotalScanSignal s1 = signals.getTotalScanSignal(scan);
		assertEquals("total signal", 5.9f, s1.getTotalSignal(), 0);
	}
}
