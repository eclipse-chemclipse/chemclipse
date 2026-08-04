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
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.util.MarkedTracesSupportMSD;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.Test;

public class ExcludedIons_3_Test {

	private IMarkedTraces<ITrace> excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);

	@Test
	public void testContains_1() {

		excludedIons.add(new TraceNominalMSD((int)IIon.TIC_ION));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).contains(0));
	}

	@Test
	public void testContains_2() {

		excludedIons.add(new TraceNominalMSD((int)IIon.TIC_ION));
		assertTrue(MarkedTracesSupportMSD.getTracesAsInteger(excludedIons).contains(0));
	}
}