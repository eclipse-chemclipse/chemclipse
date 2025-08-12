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
import org.junit.Before;
import org.junit.Test;

public class TotalIonSignals_4_Test {

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
	}

	@Test
	public void testSize_1() {

		assertEquals("size", 12, signals.size());
	}

	@Test
	public void testGetMinMax_1() {

		assertEquals("max signal", 1245234.8f, signals.getMaxSignal(), 0);
		assertEquals("min signal", 5.9f, signals.getMinSignal(), 0);
	}
}
