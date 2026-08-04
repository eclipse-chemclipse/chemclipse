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
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.msd.model.util.MarkedTracesSupportMSD;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.Test;

public class SelectedIons_2_Test {

	private IMarkedTraces<ITrace> selectedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);

	@Test
	public void testContains_1() {

		assertFalse(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(AbstractIon.getIon(4.9f)));
	}

	@Test
	public void testContains_2() {

		selectedIons.add(new TraceNominalMSD(5));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(AbstractIon.getIon(4.9f)));
	}

	@Test
	public void testContains_3() {

		selectedIons.add(new TraceNominalMSD(5));
		selectedIons.remove(new TraceNominalMSD(5));
		assertFalse(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(AbstractIon.getIon(5.4f)));
	}

	@Test
	public void testContains_4() {

		selectedIons.add(new TraceNominalMSD(10));
		selectedIons.add(new TraceNominalMSD(5));
		selectedIons.add(new TraceNominalMSD(20));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(AbstractIon.getIon(20.2f)));
	}

	@Test
	public void testContains_5() {

		MarkedTracesSupportMSD.add(selectedIons, 50, 60);
		Set<Integer> list = MarkedTracesSupportMSD.getTracesAsInteger(selectedIons);
		assertEquals(11, list.size());
		for(int i = 50; i <= 60; i++) {
			assertTrue(list.contains(i), "ion " + i);
		}
	}
}