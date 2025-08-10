/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.core.IIntegrationEntry;
import org.eclipse.chemclipse.model.core.ISignal;
import org.eclipse.chemclipse.model.implementation.IntegrationEntry;
import org.junit.Before;
import org.junit.Test;

public class IntegrationEntry_1_Test {

	private IIntegrationEntry integrationEntry;

	@Before
	public void setUp() throws Exception {

		integrationEntry = new IntegrationEntry(ISignal.TOTAL_INTENSITY, 2308934.78d);
	}

	@Test
	public void testGetIon_1() {

		assertEquals(ISignal.TOTAL_INTENSITY, integrationEntry.getSignal(), 0);
	}

	@Test
	public void testGetIntegratedArea_1() {

		assertEquals(2308934.78d, integrationEntry.getIntegratedArea(), 0);
	}
}
