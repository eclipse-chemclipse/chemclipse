/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.traces;

import java.util.List;

import junit.framework.TestCase;

public class Trace_Generic_02_Test extends TestCase {

	public void test1() {

		List<TraceGeneric> traces = TraceFactory.parseTraces("0 - 0", TraceGeneric.class);
		assertEquals(1, traces.size());
		TraceGeneric trace = traces.get(0);
		assertEquals(0, trace.getTrace());
		assertEquals(1.0d, trace.getScaleFactor());
		assertEquals("0", trace.toString());
	}
}