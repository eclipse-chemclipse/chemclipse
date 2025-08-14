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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class Trace_HighResMSD_10_Test {

	private TraceHighResMSD trace;

	@Before
	public void setUp() {

		trace = TraceFactory.parseTrace("400.01627+-10ppm (x4.7)", TraceHighResMSD.class);
	}

	@Test
	public void testMZ() {

		assertEquals(400.01627d, trace.getMZ(), 0);
	}

	@Test
	public void testDelta() {

		assertEquals(0.004000163d, trace.getDelta(), 0.000000001d);
	}

	@Test
	public void testUseRange() {

		assertTrue(trace.isUseRange());
	}

	@Test
	public void testStartMZ() {

		assertEquals(400.0122698373d, trace.getStartMZ(), 0.0000000001d);
	}

	@Test
	public void testStopMZ() {

		assertEquals(400.0202701627d, trace.getStopMZ(), 0.0000000001d);
	}

	@Test
	public void testScaleFactor() {

		assertEquals(4.7d, trace.getScaleFactor(), 0);
	}

	@Test
	public void testString() {

		assertEquals("400.01627±10ppm (x4.7)", trace.toString());
	}
}