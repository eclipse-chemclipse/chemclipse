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

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.msd.model.util.MarkedTracesSupportMSD;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.Test;

public class SelectedIons_4_Test {

	private IMarkedTraces<ITrace> selectedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);

	@Test
	public void testContains_1() {

		assertFalse(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(AbstractIon.getIon(5.2f)));
	}

	@Test
	public void testContains_2() {

		selectedIons.add(new TraceNominalMSD(5.2f));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(AbstractIon.getIon(5.3f)));
	}

	@Test
	public void testContains_3() {

		selectedIons.add(new TraceNominalMSD(5.2f));
		selectedIons.remove(new TraceNominalMSD(5.3f));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(AbstractIon.getIon(5.0f)));
	}

	@Test
	public void testContains_4() {

		selectedIons.add(new TraceNominalMSD(10.2f));
		selectedIons.add(new TraceNominalMSD(5.3f));
		selectedIons.add(new TraceNominalMSD(20.4f));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(AbstractIon.getIon(20.4f)));
	}

	@Test
	public void testSize_9() {

		selectedIons.add(new TraceNominalMSD(58.3f));
		selectedIons.add(new TraceNominalMSD(48.2f));
		selectedIons.add(new TraceNominalMSD(372.4f));
		assertEquals(3, MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).size());
	}

	@Test
	public void testSize_10() {

		assertEquals(0, MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).size());
	}
}