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
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class Trace_RasteredVSD_04_Test {

	private TraceRasteredVSD trace;

	@Before
	public void setUp() {

		trace = TraceFactory.parseTrace("1801 (x1.6)", TraceRasteredVSD.class);
	}

	@Test
	public void testNull() {

		assertNotNull(trace);
	}

	@Test
	public void testWavenumber() {

		assertEquals(1801, trace.getWavenumber());
	}

	@Test
	public void testScaleFactor() {

		assertEquals(1.6d, trace.getScaleFactor(), 0);
	}

	@Test
	public void testString() {

		assertEquals("1801 (x1.6)", trace.toString());
	}
}