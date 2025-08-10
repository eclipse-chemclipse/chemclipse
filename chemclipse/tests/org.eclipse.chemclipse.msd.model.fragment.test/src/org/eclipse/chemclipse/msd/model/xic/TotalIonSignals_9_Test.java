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

public class TotalIonSignals_9_Test {

	@Test
	public void testConstructor_1() {

		ITotalScanSignals signals = new TotalScanSignals(12);
		assertEquals("StartScan", 1, signals.getStartScan());
		assertEquals("StopScan", 12, signals.getStopScan());
	}

	@Test
	public void testConstructor_2() {

		ITotalScanSignals signals = new TotalScanSignals(150);
		assertEquals("StartScan", 1, signals.getStartScan());
		assertEquals("StopScan", 150, signals.getStopScan());
	}

	@Test
	public void testConstructor_3() {

		ITotalScanSignals signals = new TotalScanSignals(0);
		assertEquals("StartScan", 0, signals.getStartScan());
		assertEquals("StopScan", 0, signals.getStopScan());
	}

	@Test
	public void testConstructor_4() {

		ITotalScanSignals signals = new TotalScanSignals(-1);
		assertEquals("StartScan", 0, signals.getStartScan());
		assertEquals("StopScan", 0, signals.getStopScan());
	}

	@Test
	public void testConstructor_5() {

		ITotalScanSignals signals = new TotalScanSignals(12, 18);
		assertEquals("StartScan", 12, signals.getStartScan());
		assertEquals("StopScan", 18, signals.getStopScan());
	}

	@Test
	public void testConstructor_6() {

		ITotalScanSignals signals = new TotalScanSignals(18, 12);
		assertEquals("StartScan", 12, signals.getStartScan());
		assertEquals("StopScan", 18, signals.getStopScan());
	}

	@Test
	public void testConstructor_7() {

		ITotalScanSignals signals = new TotalScanSignals(0, 18);
		assertEquals("StartScan", 0, signals.getStartScan());
		assertEquals("StopScan", 0, signals.getStopScan());
	}

	@Test
	public void testConstructor_8() {

		ITotalScanSignals signals = new TotalScanSignals(18, 0);
		assertEquals("StartScan", 0, signals.getStartScan());
		assertEquals("StopScan", 0, signals.getStopScan());
	}

	@Test
	public void testConstructor_9() {

		ITotalScanSignals signals = new TotalScanSignals(0, 0);
		assertEquals("StartScan", 0, signals.getStartScan());
		assertEquals("StopScan", 0, signals.getStopScan());
	}

	@Test
	public void testConstructor_10() {

		ITotalScanSignals signals = new TotalScanSignals(-1, 0);
		assertEquals("StartScan", 0, signals.getStartScan());
		assertEquals("StopScan", 0, signals.getStopScan());
	}

	@Test
	public void testConstructor_11() {

		ITotalScanSignals signals = new TotalScanSignals(0, -1);
		assertEquals("StartScan", 0, signals.getStartScan());
		assertEquals("StopScan", 0, signals.getStopScan());
	}

	@Test
	public void testConstructor_12() {

		ITotalScanSignals signals = new TotalScanSignals(-1, -1);
		assertEquals("StartScan", 0, signals.getStartScan());
		assertEquals("StopScan", 0, signals.getStopScan());
	}
}
