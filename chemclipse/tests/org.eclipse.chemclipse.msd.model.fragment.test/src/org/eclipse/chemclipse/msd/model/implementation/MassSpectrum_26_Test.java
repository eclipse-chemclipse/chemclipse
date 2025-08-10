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

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.Test;

/**
 * Tests adjustTotalSignal(float totalSignal).
 */
public class MassSpectrum_26_Test {

	private IScanMSD massSpectrum = new ScanMSD();

	@Test
	public void testIdentifier_1() {

		assertEquals("Identifier", "", massSpectrum.getIdentifier());
	}

	@Test
	public void testIdentifier_2() {

		massSpectrum.setIdentifier(null);
		assertEquals("Identifier", "", massSpectrum.getIdentifier());
	}

	@Test
	public void testIdentifier_3() {

		massSpectrum.setIdentifier("MS-I");
		assertEquals("Identifier", "MS-I", massSpectrum.getIdentifier());
	}

	@Test
	public void testIdentifier_4() {

		massSpectrum.setIdentifier("MS-I");
		massSpectrum.setIdentifier(null);
		assertEquals("Identifier", "MS-I", massSpectrum.getIdentifier());
	}
}
