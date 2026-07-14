/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Traces_22_Test {

	@Test
	public void testNominalMSD() {

		List<ITrace> traces = new ArrayList<>();
		traces.addAll(TraceFactory.parseTraces("50 55", TraceNominalMSD.class));
		assertEquals(2, traces.size());
		assertEquals(50, traces.get(0).getValue(), 0);
		assertEquals(55, traces.get(1).getValue(), 0);
	}

	@Test
	public void testTandemMSD_01() {

		List<ITrace> traces = new ArrayList<>();
		traces.addAll(TraceFactory.parseTraces("147 > 121.0 @20", TraceTandemMSD.class));
		assertEquals(1, traces.size());
		assertEquals("147 > 121.0 @20", traces.get(0).toString());
	}

	@Test
	public void testTandemMSD_02() {

		String content = "147 > 121.0 @20,155 > 130.0 @25";
		List<ITrace> traces = new ArrayList<>();
		traces.addAll(TraceFactory.parseTraces(content, TraceTandemMSD.class));
		assertEquals(2, traces.size());
		assertEquals("147 > 121.0 @20", traces.get(0).toString());
		assertEquals("155 > 130.0 @25", traces.get(1).toString());
	}

	@Test
	public void testTandemMSD_03() {

		List<ITrace> traces = new ArrayList<>();
		traces.addAll(TraceFactory.parseTraces("125 > 89.0 @17\n127 > 109.0 @10\n127 > 89.0 @17", TraceTandemMSD.class));
		assertEquals(3, traces.size());
		/*
		 * getTracesAsString produces comma-separated output.
		 * parseTraces must handle that correctly.
		 */
		String serialized = TraceFactory.getTracesAsString(traces);
		assertEquals("125 > 89.0 @17,127 > 109.0 @10,127 > 89.0 @17", serialized);
		List<ITrace> tracesRoundtrip = new ArrayList<>();
		tracesRoundtrip.addAll(TraceFactory.parseTraces(serialized, TraceTandemMSD.class));
		assertEquals(3, tracesRoundtrip.size());
		assertEquals("125 > 89.0 @17", tracesRoundtrip.get(0).toString());
		assertEquals("127 > 109.0 @10", tracesRoundtrip.get(1).toString());
		assertEquals("127 > 89.0 @17", tracesRoundtrip.get(2).toString());
	}

	@Test
	public void testHighResMSD_01() {

		List<ITrace> traces = new ArrayList<>();
		traces.addAll(TraceFactory.parseTraces("154.3546+-20ppm", TraceHighResMSD.class));
		assertEquals(1, traces.size());
		assertEquals(154.3546d, traces.get(0).getValue(), 0);
		assertEquals("154.3546±20ppm", traces.get(0).toString());
	}

	@Test
	public void testHighResMSD_02() {

		List<ITrace> traces = new ArrayList<>();
		traces.addAll(TraceFactory.parseTraces("154.3546+-20ppm,170.16±20ppm", TraceHighResMSD.class));
		assertEquals(2, traces.size());
		assertEquals(154.3546d, traces.get(0).getValue(), 0);
		assertEquals("154.3546±20ppm", traces.get(0).toString());
		assertEquals(170.16d, traces.get(1).getValue(), 0);
		assertEquals("170.16±20ppm", traces.get(1).toString());
	}

	@Test
	public void testHighResMSD_03() {

		List<ITrace> traces = new ArrayList<>();
		traces.addAll(TraceFactory.parseTraces("154.3546±20ppm,170.16±20ppm", TraceHighResMSD.class));
		assertEquals(2, traces.size());
		assertEquals(154.3546d, traces.get(0).getValue(), 0);
		assertEquals("154.3546±20ppm", traces.get(0).toString());
		assertEquals(170.16d, traces.get(1).getValue(), 0);
		assertEquals("170.16±20ppm", traces.get(1).toString());
	}

	@Test
	public void testHighResMSD_04() {

		List<ITrace> traces = new ArrayList<>();
		traces.addAll(TraceFactory.parseTraces("154.3546+-20ppm\n170.16±20ppm\n217.982±15ppm", TraceHighResMSD.class));
		assertEquals(3, traces.size());
		assertEquals(154.3546d, traces.get(0).getValue(), 0);
		assertEquals("154.3546±20ppm", traces.get(0).toString());
		assertEquals(170.16d, traces.get(1).getValue(), 0);
		assertEquals("170.16±20ppm", traces.get(1).toString());
		assertEquals(217.982d, traces.get(2).getValue(), 0);
		assertEquals("217.982±15ppm", traces.get(2).toString());
	}
}