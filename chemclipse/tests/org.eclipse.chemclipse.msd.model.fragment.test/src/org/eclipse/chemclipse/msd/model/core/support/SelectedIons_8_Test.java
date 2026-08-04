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

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class SelectedIons_8_Test {

	private IMarkedTraces<ITrace> selectedIons;

	@BeforeAll
	public void setUp() {

		selectedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);
		selectedIons.add(new TraceNominalMSD(28.82849943f));
		selectedIons.add(new TraceNominalMSD(28.787f));
		selectedIons.add(new TraceNominalMSD(29));
		selectedIons.add(new TraceNominalMSD(29.267849f));
		selectedIons.add(new TraceNominalMSD(30.96f));
		selectedIons.add(new TraceNominalMSD(31));
	}

	@Test
	public void testSize_1() {

		assertEquals(6, selectedIons.size());
	}
}