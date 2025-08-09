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
package org.eclipse.chemclipse.model.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the class IonRange.
 */
public class ScanRange_1_Test {

	@Test
	public void testSetup_1() {

		IScanRange scanRange = new ScanRange(1, 5);
		assertEquals("startScan", 1, scanRange.getStartScan());
		assertEquals("stopScan", 5, scanRange.getStopScan());
	}

	@Test
	public void testSetup_2() {

		IScanRange scanRange = new ScanRange(5, 1);
		assertEquals("startScan", 1, scanRange.getStartScan());
		assertEquals("stopScan", 5, scanRange.getStopScan());
		assertEquals("width", 5, scanRange.getWidth());
	}

	@Test
	public void testSetup_3() {

		IScanRange scanRange = new ScanRange(0, 5);
		assertEquals("startScan", IScanRange.MIN_SCAN, scanRange.getStartScan());
		assertEquals("stopScan", 5, scanRange.getStopScan());
		assertEquals("width", 5, scanRange.getWidth());
	}

	@Test
	public void testSetup_4() {

		IScanRange scanRange = new ScanRange(1, 0);
		assertEquals("startScan", 1, scanRange.getStartScan());
		assertEquals("stopScan", IScanRange.MIN_SCAN, scanRange.getStopScan());
		assertEquals("width", 1, scanRange.getWidth());
	}

	@Test
	public void testSetup_5() {

		// Because IScanRange.MAX_SCAN+1 is negative.
		IScanRange scanRange = new ScanRange(IScanRange.MAX_SCAN + 1, 5);
		assertEquals("startScan", 1, scanRange.getStartScan());
		assertEquals("stopScan", 5, scanRange.getStopScan());
		assertEquals("width", 5, scanRange.getWidth());
	}

	@Test
	public void testSetup_6() {

		// Because IScanRange.MAX_SCAN+1 is negative.
		IScanRange scanRange = new ScanRange(1, IScanRange.MAX_SCAN + 1);
		assertEquals("startScan", 1, scanRange.getStartScan());
		assertEquals("stopScan", 1, scanRange.getStopScan());
		assertEquals("width", 1, scanRange.getWidth());
	}

	@Test
	public void testSetup_7() {

		// Because IScanRange.MAX_SCAN+1 is negative.
		IScanRange scanRange = new ScanRange(-1, IScanRange.MAX_SCAN + 1);
		assertEquals("startScan", 1, scanRange.getStartScan());
		assertEquals("stopScan", IScanRange.MAX_SCAN, scanRange.getStopScan());
		assertEquals("width", IScanRange.MAX_SCAN, scanRange.getWidth());
	}
}
