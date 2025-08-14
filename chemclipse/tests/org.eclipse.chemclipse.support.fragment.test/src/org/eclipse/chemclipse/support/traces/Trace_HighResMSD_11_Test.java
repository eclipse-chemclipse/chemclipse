/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.support.traces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Trace_HighResMSD_11_Test {

	@Test
	public void test1() {

		TraceHighResMSD trace = TraceFactory.parseTrace("400.01627+-10ppm", TraceHighResMSD.class);
		assertEquals(400.01627d, trace.getMZ(), 0);
		assertEquals(10, trace.getPPM());
		assertEquals(0.0040001627000000005d, trace.getDelta(), 0);
		assertEquals(400.01226983730004d, trace.getStartMZ(), 0);
		assertEquals(400.0202701627d, trace.getStopMZ(), 0);
		assertFalse(trace.matches(400.0122698373d));
		assertTrue(trace.matches(400.0122698374d));
		assertTrue(trace.matches(400.0202701627d));
		assertFalse(trace.matches(400.0202701628d));
	}

	@Test
	public void test2() {

		TraceHighResMSD trace = TraceFactory.parseTrace("400.01627", TraceHighResMSD.class);
		assertEquals(400.01627d, trace.getMZ(), 0);
		assertEquals(0, trace.getPPM());
		assertEquals(0.0d, trace.getDelta(), 0);
		assertEquals(400.01627d, trace.getStartMZ(), 0);
		assertEquals(400.01627d, trace.getStopMZ(), 0);
		assertFalse(trace.matches(400.01626d));
		assertTrue(trace.matches(400.01627d));
		assertFalse(trace.matches(400.01628d));
	}

	@Test
	public void test3() {

		TraceHighResMSD trace = TraceFactory.parseTrace("400.01627", TraceHighResMSD.class);
		trace.setPPM(10);
		assertEquals(400.01627d, trace.getMZ(), 0);
		assertEquals(10, trace.getPPM());
		assertEquals(0.0040001627000000005d, trace.getDelta(), 0);
		assertEquals(400.01226983730004d, trace.getStartMZ(), 0);
		assertEquals(400.0202701627d, trace.getStopMZ(), 0);
		assertFalse(trace.matches(400.0122698373d));
		assertTrue(trace.matches(400.0122698374d));
		assertTrue(trace.matches(400.0202701627d));
		assertFalse(trace.matches(400.0202701628d));
	}
}