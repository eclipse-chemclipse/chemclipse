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
package org.eclipse.chemclipse.tsd.converter.core.matcher;

import junit.framework.TestCase;

public class TraceRangeMatcher_2_Test extends TestCase {

	private TraceRangeMatcher traceRangeMatcher = new TraceRangeMatcher();

	@Override
	protected void setUp() throws Exception {

		super.setUp();
	}

	public void test1() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, 1000);
		//
		assertEquals(1, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(1000).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(1001).size());
	}

	public void test2() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 500, 1000);
		//
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(499).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(500).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(1000).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(1001).size());
	}

	public void test3() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 500, 1000);
		traceRangeMatcher.addHighResMSD("150.15±0.05", 750, 1500);
		//
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(499).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(500).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(749).size());
		assertEquals(2, traceRangeMatcher.getTraceRanges(750).size());
		assertEquals(2, traceRangeMatcher.getTraceRanges(1000).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(1001).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(1500).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(1501).size());
	}
}