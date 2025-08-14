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

import java.util.List;

import org.junit.Test;

public class Traces_02_Test {

	private String content = "55 - 65";

	@Test
	public void test1() {

		List<? extends ITrace> traces = TraceFactory.parseTraces(content, TraceType.GENERIC.clazz());
		assertEquals(11, traces.size());
		assertEquals(55.0d, traces.get(0).getValue(), 0);
		assertEquals(65.0d, traces.get(10).getValue(), 0);
	}

	@Test
	public void test2() {

		List<? extends ITrace> traces = TraceFactory.parseTraces(content, TraceType.MSD_NOMINAL.clazz());
		assertEquals(11, traces.size());
		assertEquals(55.0d, traces.get(0).getValue(), 0);
		assertEquals(65.0d, traces.get(10).getValue(), 0);
	}

	@Test
	public void test3() {

		List<? extends ITrace> traces = TraceFactory.parseTraces(content, TraceType.MSD_TANDEM.clazz());
		assertTrue(traces.isEmpty());
	}

	@Test
	public void test4() {

		List<? extends ITrace> traces = TraceFactory.parseTraces(content, TraceType.MSD_HIGHRES.clazz());
		assertTrue(traces.isEmpty());
	}

	@Test
	public void test5() {

		List<? extends ITrace> traces = TraceFactory.parseTraces(content, TraceType.VSD_RASTERED.clazz());
		assertEquals(11, traces.size());
		assertEquals(55.0d, traces.get(0).getValue(), 0);
		assertEquals(65.0d, traces.get(10).getValue(), 0);
	}

	@Test
	public void test6() {

		List<? extends ITrace> traces = TraceFactory.parseTraces(content, TraceType.WSD_RASTERED.clazz());
		assertEquals(11, traces.size());
		assertEquals(55.0d, traces.get(0).getValue(), 0);
		assertEquals(65.0d, traces.get(10).getValue(), 0);
	}

	@Test
	public void test7() {

		List<? extends ITrace> traces = TraceFactory.parseTraces(content, TraceType.WSD_HIGHRES.clazz());
		assertTrue(traces.isEmpty());
	}
}