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
package org.eclipse.chemclipse.tsd.converter.core.matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TraceRangeMatcher_1_Test {

	private TraceRangeMatcher traceRangeMatcher = new TraceRangeMatcher();

	@Test
	public void test1() {

		assertFalse(traceRangeMatcher.isParseFully());
	}

	@Test
	public void test2() {

		traceRangeMatcher.setParseFully(true);
		assertTrue(traceRangeMatcher.isParseFully());
	}

	@Test
	public void test3() {

		traceRangeMatcher.addHighResMSD("", 0, 0);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	@Test
	public void test4() {

		traceRangeMatcher.addHighResMSD("", -1, 0);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	@Test
	public void test5() {

		traceRangeMatcher.addHighResMSD("", 0, -1);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	@Test
	public void test6() {

		traceRangeMatcher.addHighResMSD("", -1, -1);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	@Test
	public void test7() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, 0);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	@Test
	public void test8() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, Integer.MAX_VALUE);
		assertEquals(1, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	@Test
	public void test9() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, 0);
		traceRangeMatcher.addHighResMSD("150.15±0.05", 0, 0);
		assertEquals(0, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(0, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	@Test
	public void test10() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, Integer.MAX_VALUE);
		traceRangeMatcher.addHighResMSD("150.15±0.05", 0, 0);
		assertEquals(1, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	@Test
	public void test11() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, 0);
		traceRangeMatcher.addHighResMSD("150.15±0.05", 0, Integer.MAX_VALUE);
		assertEquals(1, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(1, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}

	@Test
	public void test12() {

		traceRangeMatcher.addHighResMSD("94.05±0.05", 0, Integer.MAX_VALUE);
		traceRangeMatcher.addHighResMSD("150.15±0.05", 0, Integer.MAX_VALUE);
		assertEquals(2, traceRangeMatcher.getTraceRanges(0).size());
		assertEquals(2, traceRangeMatcher.getTraceRanges(Integer.MAX_VALUE).size());
	}
}