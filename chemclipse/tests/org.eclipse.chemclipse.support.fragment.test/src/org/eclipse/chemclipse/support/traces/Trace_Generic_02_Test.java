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

import java.util.List;

import org.junit.Test;

public class Trace_Generic_02_Test {

	@Test
	public void test1() {

		List<TraceGeneric> traces = TraceFactory.parseTraces("0 - 0", TraceGeneric.class);
		assertEquals(1, traces.size());
		TraceGeneric trace = traces.get(0);
		assertEquals(0, trace.getTrace());
		assertEquals(1.0d, trace.getScaleFactor(), 0);
		assertEquals("0", trace.toString());
	}
}