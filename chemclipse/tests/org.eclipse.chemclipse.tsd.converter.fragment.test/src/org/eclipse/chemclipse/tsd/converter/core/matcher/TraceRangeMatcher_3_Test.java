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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class TraceRangeMatcher_3_Test extends TestCase {

	private TraceRangeMatcher traceRangeMatcher = new TraceRangeMatcher();
	private Map<Integer, Double> valueMap = new HashMap<>();

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		//
		traceRangeMatcher.addHighResMSD("94.05±0.05", 500, 1000);
		traceRangeMatcher.addHighResMSD("150.15±0.05", 750, 1500);
		//
		valueMap.put(79, 93.95);
		valueMap.put(80, 94.0);
		valueMap.put(81, 94.05);
		valueMap.put(82, 94.10);
		valueMap.put(83, 94.15);
		valueMap.put(1201, 150.05);
		valueMap.put(1202, 150.10);
		valueMap.put(1203, 150.15);
		valueMap.put(1204, 150.20);
		valueMap.put(1205, 150.25);
		traceRangeMatcher.applyTraceIndices(valueMap);
	}

	public void test1a() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(0, 80, 82).size());
	}

	public void test1b() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(0, 1202, 1204).size());
	}

	public void test2a() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(499, 80, 82).size());
	}

	public void test2b() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(499, 1202, 1204).size());
	}

	public void test3a() {

		assertEquals(3, traceRangeMatcher.getTraceIndices(500, 80, 82).size());
	}

	public void test3b() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(500, 1202, 1204).size());
	}

	public void test4a() {

		assertEquals(3, traceRangeMatcher.getTraceIndices(749, 80, 82).size());
	}

	public void test4b() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(749, 1202, 1204).size());
	}

	public void test5a() {

		assertEquals(3, traceRangeMatcher.getTraceIndices(750, 80, 82).size());
	}

	public void test5b() {

		assertEquals(3, traceRangeMatcher.getTraceIndices(750, 1202, 1204).size());
	}

	public void test6a() {

		assertEquals(3, traceRangeMatcher.getTraceIndices(1000, 80, 82).size());
	}

	public void test6b() {

		assertEquals(3, traceRangeMatcher.getTraceIndices(1000, 1202, 1204).size());
	}

	public void test7a() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(1001, 80, 82).size());
	}

	public void test7b() {

		assertEquals(3, traceRangeMatcher.getTraceIndices(1001, 1202, 1204).size());
	}

	public void test8a() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(1500, 80, 82).size());
	}

	public void test8b() {

		assertEquals(3, traceRangeMatcher.getTraceIndices(1500, 1202, 1204).size());
	}

	public void test9a() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(1501, 80, 82).size());
	}

	public void test9b() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(1501, 1202, 1204).size());
	}

	public void test10() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(499, 80, 1204).size());
	}

	public void test11() {

		assertEquals(3, traceRangeMatcher.getTraceIndices(500, 80, 1204).size());
	}

	public void test12() {

		assertEquals(6, traceRangeMatcher.getTraceIndices(750, 80, 1204).size());
	}

	public void test13() {

		assertEquals(6, traceRangeMatcher.getTraceIndices(1000, 80, 1204).size());
	}

	public void test14() {

		assertEquals(3, traceRangeMatcher.getTraceIndices(1500, 80, 1204).size());
	}

	public void test15() {

		assertEquals(0, traceRangeMatcher.getTraceIndices(1501, 80, 1204).size());
	}
}