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

import org.junit.Test;

public class ExtractedIonSignals_1_Test {

	@Test
	public void testConstructor_1() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(200);
		assertEquals("StartScan", 1, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 200, extractedIonSignals.getStopScan());
	}

	@Test
	public void testConstructor_2() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(0);
		assertEquals("StartScan", 0, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 0, extractedIonSignals.getStopScan());
	}

	@Test
	public void testConstructor_3() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(-1);
		assertEquals("StartScan", 0, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 0, extractedIonSignals.getStopScan());
	}

	@Test
	public void testConstructor_4() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(20);
		assertEquals("StartScan", 1, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 20, extractedIonSignals.getStopScan());
	}

	@Test
	public void testConstructor_5() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(20, 50);
		assertEquals("StartScan", 20, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 50, extractedIonSignals.getStopScan());
	}

	@Test
	public void testConstructor_6() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(50, 20);
		assertEquals("StartScan", 20, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 50, extractedIonSignals.getStopScan());
	}

	@Test
	public void testConstructor_7() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(-1, 20);
		assertEquals("StartScan", 0, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 0, extractedIonSignals.getStopScan());
	}

	@Test
	public void testConstructor_8() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(20, -1);
		assertEquals("StartScan", 0, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 0, extractedIonSignals.getStopScan());
	}

	@Test
	public void testConstructor_9() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(-1, -1);
		assertEquals("StartScan", 0, extractedIonSignals.getStartScan());
		assertEquals("StopScan", 0, extractedIonSignals.getStopScan());
	}
}
