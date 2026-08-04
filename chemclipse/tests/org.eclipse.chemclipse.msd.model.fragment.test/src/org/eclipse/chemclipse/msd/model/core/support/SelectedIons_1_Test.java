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

public class SelectedIons_1_Test {

	private IMarkedTraces<ITrace> selectedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);

	@Test
	public void testContains_1() {

		assertFalse(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(5));
	}

	@Test
	public void testContains_2() {

		selectedIons.add(new TraceNominalMSD(5));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(5));
	}

	@Test
	public void testContains_3() {

		selectedIons.add(new TraceNominalMSD(5));
		selectedIons.remove(new TraceNominalMSD(5));
		assertFalse(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(5));
	}

	@Test
	public void testContains_4() {

		selectedIons.add(new TraceNominalMSD(10));
		selectedIons.add(new TraceNominalMSD(5));
		selectedIons.add(new TraceNominalMSD(20));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(20));
	}

	@Test
	public void testContains_5() {

		MarkedTracesSupportMSD.add(selectedIons, 10, 12);
		Set<Integer> selectedIonsNominal = MarkedTracesSupportMSD.getTracesAsInteger(selectedIons);
		assertFalse(selectedIonsNominal.contains(20));
		assertTrue(selectedIonsNominal.contains(10));
		assertTrue(selectedIonsNominal.contains(11));
		assertTrue(selectedIonsNominal.contains(12));
	}

	@Test
	public void testContains_6() {

		MarkedTracesSupportMSD.add(selectedIons, 12, 10);
		Set<Integer> selectedIonsNominal = MarkedTracesSupportMSD.getTracesAsInteger(selectedIons);
		assertFalse(selectedIonsNominal.contains(20));
		assertTrue(selectedIonsNominal.contains(10));
		assertTrue(selectedIonsNominal.contains(11));
		assertTrue(selectedIonsNominal.contains(12));
	}

	@Test
	public void testContains_7() {

		MarkedTracesSupportMSD.add(selectedIons, 12, 12);
		Set<Integer> selectedIonsNominal = MarkedTracesSupportMSD.getTracesAsInteger(selectedIons);
		assertFalse(selectedIonsNominal.contains(20));
		assertFalse(selectedIonsNominal.contains(10));
		assertFalse(selectedIonsNominal.contains(11));
		assertTrue(selectedIonsNominal.contains(12));
	}

	@Test
	public void testSize_8() {

		MarkedTracesSupportMSD.add(selectedIons, 12, 12);
		assertEquals(1, MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).size());
	}

	@Test
	public void testSize_9() {

		selectedIons.add(new TraceNominalMSD(58));
		selectedIons.add(new TraceNominalMSD(48));
		selectedIons.add(new TraceNominalMSD(372));
		assertEquals(3, MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).size());
	}

	@Test
	public void testSize_10() {

		assertEquals(0, MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).size());
	}
}