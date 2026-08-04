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

public class ExcludedIons_4_Test {

	private IMarkedTraces<ITrace> excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);

	@Test
	public void testContains_1() {

		assertFalse(MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).contains(AbstractIon.getIon(5.0f)));
	}

	@Test
	public void testContains_2() {

		excludedIons.add(new TraceNominalMSD(5.2f));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).contains(AbstractIon.getIon(5.0f)));
	}

	@Test
	public void testContains_3() {

		excludedIons.add(new TraceNominalMSD(5.3f));
		excludedIons.remove(new TraceNominalMSD(5.2f));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).contains(AbstractIon.getIon(5.0f)));
	}

	@Test
	public void testContains_4() {

		excludedIons.add(new TraceNominalMSD(10.4f));
		excludedIons.add(new TraceNominalMSD(5.3f));
		excludedIons.add(new TraceNominalMSD(20.2f));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).contains(AbstractIon.getIon(20.3f)));
	}

	@Test
	public void testSize_9() {

		excludedIons.add(new TraceNominalMSD(58.4f));
		excludedIons.add(new TraceNominalMSD(48.3f));
		excludedIons.add(new TraceNominalMSD(372.2f));
		assertEquals(3, MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).size());
	}

	@Test
	public void testSize_10() {

		assertEquals(0, MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).size());
	}
}
