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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.util.MarkedTracesSupportMSD;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class SelectedIons_3_Test {

	private IMarkedTraces<ITrace> selectedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);

	@Test
	public void testContains_1() {

		selectedIons.add(new TraceNominalMSD((int)IIon.TIC_ION));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(0));
	}

	@Test
	public void testContains_2() {

		selectedIons.add(new TraceNominalMSD((int)IIon.TIC_ION));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(selectedIons).contains(AbstractIon.getIon(0.0f)));
	}
}