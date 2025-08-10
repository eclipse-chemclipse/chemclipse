/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.core;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.junit.Before;
import org.junit.Test;

public class IonBounds_3_Test {

	private IIonBounds ionBounds;
	private Ion ion1;
	private Ion ion2;

	@Before
	public void setUp() throws Exception {

		ion1 = new Ion(25.5f, 3452.4f);
		ion2 = new Ion(27.2f, 4785.6f);
		ionBounds = new IonBounds(ion1, ion2);
	}

	@Test
	public void testGetLowestIon_1() {

		assertEquals("GetLowestIon Ion", 25.5d, ionBounds.getLowestIon().getIon(), 0);
	}

	@Test
	public void testGetLowestIon_2() {

		assertEquals("GetLowestIon Abundance", 3452.4f, ionBounds.getLowestIon().getAbundance(), 0);
	}

	@Test
	public void testGetHighestIon_1() {

		assertEquals("GetHighestIon Ion", 27.200000762939453d, ionBounds.getHighestIon().getIon(), 0);
	}

	@Test
	public void testGetHighestIon_2() {

		assertEquals("GetHighestIon Abundance", 4785.6f, ionBounds.getHighestIon().getAbundance(), 0);
	}
}
