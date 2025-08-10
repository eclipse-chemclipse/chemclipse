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
package org.eclipse.chemclipse.msd.converter.supplier.amdis.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AmdisMassSpectrum_1_Test {

	private IVendorLibraryMassSpectrum massSpectrum = new VendorLibraryMassSpectrum();

	@Test
	public void testGetSource_1() {

		assertEquals("Source", "", massSpectrum.getSource());
	}

	@Test
	public void testGetSource_2() {

		massSpectrum.setSource("file something");
		assertEquals("Source", "file something", massSpectrum.getSource());
	}

	@Test
	public void testGetSource_3() {

		massSpectrum.setSource(null);
		assertEquals("Source", "", massSpectrum.getSource());
	}
}
