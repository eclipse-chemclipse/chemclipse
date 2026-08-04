/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.core.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.msd.model.util.MarkedTracesSupportMSD;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.Test;

public class ExcludedIons_1_Test {

	private IMarkedTraces<ITrace> excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);

	@Test
	public void testContains_1() {

		assertFalse(MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).contains(5));
	}

	@Test
	public void testContains_2() {

		excludedIons.add(new TraceNominalMSD(5));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).contains(5));
	}

	@Test
	public void testContains_3() {

		excludedIons.add(new TraceNominalMSD(5));
		excludedIons.remove(new TraceNominalMSD(5));
		assertFalse(MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).contains(5));
	}

	@Test
	public void testContains_4() {

		excludedIons.add(new TraceNominalMSD(10));
		excludedIons.add(new TraceNominalMSD(5));
		excludedIons.add(new TraceNominalMSD(20));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).contains(20));
	}

	@Test
	public void testContains_5() {

		MarkedTracesSupportMSD.add(excludedIons, 10, 12);
		Set<Integer> excludedIonsNominal = MarkedTracesSupportMSD.getTracesAsInteger(excludedIons);
		assertFalse(excludedIonsNominal.contains(20));
		assertTrue(excludedIonsNominal.contains(10));
		assertTrue(excludedIonsNominal.contains(11));
		assertTrue(excludedIonsNominal.contains(12));
	}

	@Test
	public void testContains_6() {

		MarkedTracesSupportMSD.add(excludedIons, 12, 10);
		Set<Integer> excludedIonsNominal = MarkedTracesSupportMSD.getTracesAsInteger(excludedIons);
		assertFalse(excludedIonsNominal.contains(20));
		assertTrue(excludedIonsNominal.contains(10));
		assertTrue(excludedIonsNominal.contains(11));
		assertTrue(excludedIonsNominal.contains(12));
	}

	@Test
	public void testContains_7() {

		MarkedTracesSupportMSD.add(excludedIons, 12, 12);
		Set<Integer> excludedIonsNominal = MarkedTracesSupportMSD.getTracesAsInteger(excludedIons);
		assertFalse(excludedIonsNominal.contains(20));
		assertFalse(excludedIonsNominal.contains(10));
		assertFalse(excludedIonsNominal.contains(11));
		assertTrue(excludedIonsNominal.contains(12));
	}

	@Test
	public void testSize_8() {

		MarkedTracesSupportMSD.add(excludedIons, 12, 12);
		assertEquals(1, MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).size());
	}

	@Test
	public void testSize_9() {

		excludedIons.add(new TraceNominalMSD(58));
		excludedIons.add(new TraceNominalMSD(48));
		excludedIons.add(new TraceNominalMSD(372));
		assertEquals(3, MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).size());
	}

	@Test
	public void testSize_10() {

		assertEquals(0, MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).size());
	}
}