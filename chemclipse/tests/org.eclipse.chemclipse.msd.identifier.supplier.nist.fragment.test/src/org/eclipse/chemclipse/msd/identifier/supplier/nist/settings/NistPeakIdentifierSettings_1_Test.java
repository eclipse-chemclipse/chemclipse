/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.identifier.supplier.nist.settings;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NistPeakIdentifierSettings_1_Test {

	private PeakIdentifierSettings settings = new PeakIdentifierSettings();

	@Test
	public void testGetNumberOfTargets_1() {

		assertEquals(15, settings.getNumberOfTargets());
	}

	@Test
	public void testGetNumberOfTargets_2() {

		settings.setNumberOfTargets((byte)1);
		assertEquals(1, settings.getNumberOfTargets());
	}

	@Test
	public void testGetNumberOfTargets_3() {

		settings.setNumberOfTargets((byte)100);
		assertEquals(100, settings.getNumberOfTargets());
	}

	@Test
	public void testGetNumberOfTargets_4() {

		settings.setNumberOfTargets((byte)0);
		assertEquals(15, settings.getNumberOfTargets());
	}

	@Test
	public void testGetNumberOfTargets_5() {

		settings.setNumberOfTargets((byte)101);
		assertEquals(15, settings.getNumberOfTargets());
	}
}
