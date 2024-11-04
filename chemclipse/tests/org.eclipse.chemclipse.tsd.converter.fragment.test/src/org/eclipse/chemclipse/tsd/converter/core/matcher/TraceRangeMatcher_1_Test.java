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

public class TraceRangeMatcher_1_Test extends TestCase {

	private TraceRangeMatcher traceRangeMatcher = new TraceRangeMatcher();

	@Override
	protected void setUp() throws Exception {

		super.setUp();
	}

	public void test1() {

		assertFalse(traceRangeMatcher.isParseFully());
	}

	public void test2() {

		traceRangeMatcher.setParseFully(true);
		assertTrue(traceRangeMatcher.isParseFully());
	}

	public void test3() {

		traceRangeMatcher.addHighResMSD("", 0, 0);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	public void test4() {

		traceRangeMatcher.addHighResMSD("", -1, 0);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	public void test5() {

		traceRangeMatcher.addHighResMSD("", 0, -1);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	public void test6() {

		traceRangeMatcher.addHighResMSD("", -1, -1);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	public void test7() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, 0);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	public void test8() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, Integer.MAX_VALUE);
		assertEquals(1, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	public void test9() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, 0);
		traceRangeMatcher.addHighResMSD("150.15±0.05", 0, 0);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	public void test10() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, Integer.MAX_VALUE);
		traceRangeMatcher.addHighResMSD("150.15±0.05", 0, 0);
		assertEquals(1, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	public void test11() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, 0);
		traceRangeMatcher.addHighResMSD("150.15±0.05", 0, Integer.MAX_VALUE);
		assertEquals(1, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	public void test12() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, Integer.MAX_VALUE);
		traceRangeMatcher.addHighResMSD("150.15±0.05", 0, Integer.MAX_VALUE);
		assertEquals(2, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(2, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}
}