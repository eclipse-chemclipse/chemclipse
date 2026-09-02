/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.model.core.IIntegrationEntry;
import org.eclipse.chemclipse.model.implementation.IntegrationEntry;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationEntry_1_Test {

	private IIntegrationEntry integrationEntry;

	@BeforeAll
	public void setUp() {

		integrationEntry = new IntegrationEntry(ITrace.TOTAL_INTENSITY, 2308934.78d);
	}

	@Test
	public void testGetIon_1() {

		assertEquals(ITrace.TOTAL_INTENSITY, integrationEntry.getSignal(), 0);
	}

	@Test
	public void testGetIntegratedArea_1() {

		assertEquals(2308934.78d, integrationEntry.getIntegratedArea(), 0);
	}
}
