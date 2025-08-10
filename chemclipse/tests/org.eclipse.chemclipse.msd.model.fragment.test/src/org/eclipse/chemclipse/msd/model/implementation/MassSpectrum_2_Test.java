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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Equals, hashCode
 */
public class MassSpectrum_2_Test {

	private ScanMSD massSpectrum1;
	private ScanMSD massSpectrum2;

	@Before
	public void setUp() throws Exception {

		massSpectrum1 = new ScanMSD();
		massSpectrum1.addIon(new Ion(45.4f, 7830.4f));
		massSpectrum2 = new ScanMSD();
	}

	@Test
	public void testEquals_1() {

		assertEquals("equals", false, massSpectrum1.equals(massSpectrum2));
	}

	@Test
	public void testEquals_2() {

		assertEquals("equals", false, massSpectrum2.equals(massSpectrum1));
	}

	@Test
	public void testHashCode_1() {

		assertTrue("hashCode", massSpectrum1.hashCode() != massSpectrum2.hashCode());
	}

	@Test
	public void testHashCode_2() {

		assertTrue("hashCode", massSpectrum2.hashCode() != massSpectrum1.hashCode());
	}
}
