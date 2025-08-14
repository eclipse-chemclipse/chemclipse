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

import org.junit.Before;
import org.junit.Test;

public class Trace_HighResMSD_01_Test {

	private TraceHighResMSD trace;

	@Before
	public void setUp() {

		trace = TraceFactory.parseTrace("427.240", TraceHighResMSD.class);
	}

	@Test
	public void testMZ() {

		assertEquals(427.240d, trace.getMZ(), 0);
	}

	@Test
	public void testDelta() {

		assertEquals(0.0d, trace.getDelta(), 0);
	}

	@Test
	public void testUseRange() {

		assertFalse(trace.isUseRange());
	}

	@Test
	public void testStartMZ() {

		assertEquals(427.240d, trace.getStartMZ(), 0.0000000001d);
	}

	@Test
	public void testStopMZ() {

		assertEquals(427.240d, trace.getStopMZ(), 0.0000000001d);
	}

	@Test
	public void testScaleFactor() {

		assertEquals(1.0d, trace.getScaleFactor(), 0);
	}

	@Test
	public void testString() {

		assertEquals("427.24", trace.toString());
	}
}