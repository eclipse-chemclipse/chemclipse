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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.msd.model.core.PeakIonType;
import org.junit.Before;
import org.junit.Test;

public class PeakIon_1_Test {

	private PeakIon peakIon;

	@Before
	public void setUp() {

		peakIon = new PeakIon(45.5f, 2500.4f);
	}

	@Test
	public void testGetUncertaintyFactor_1() {

		assertEquals("GetUncertaintyFactor", 1.0f, peakIon.getUncertaintyFactor(), 0);
	}

	@Test
	public void testGetPeakIonType_1() {

		assertEquals("GetPeakIonType", PeakIonType.NO_TYPE, peakIon.getPeakIonType());
	}
}
