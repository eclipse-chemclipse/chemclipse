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

public class Trace_TandemMSD_01_Test {

	private TraceTandemMSD trace;

	@Before
	public void setUp() {

		trace = TraceFactory.parseTrace("139 > 111.0 @12", TraceTandemMSD.class);
	}

	@Test
	public void testNull() {

		assertNotNull(trace);
	}

	@Test
	public void testParentMZ() {

		assertEquals(139, trace.getParentMZ());
	}

	@Test
	public void testDaughterMZ() {

		assertEquals(111.0, trace.getDaughterMZ(), 0);
	}

	@Test
	public void testCollisionEnergy() {

		assertEquals(12, trace.getCollisionEnergy());
	}

	@Test
	public void testScaleFactor() {

		assertEquals(1.0d, trace.getScaleFactor(), 0);
	}

	@Test
	public void testString() {

		assertEquals("139 > 111.0 @12", trace.toString());
	}
}