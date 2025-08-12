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

import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.junit.Test;

public class SupplierMassSpectrum_1_Test {

	private IRegularMassSpectrum massSpectrum = new VendorMassSpectrum();

	@Test
	public void testRetentionTime_1() {

		assertEquals("Retention Time", 0, massSpectrum.getRetentionTime());
	}

	@Test
	public void testRetentionTime_2() {

		massSpectrum.setRetentionTime(5000);
		assertEquals("Retention Time", 5000, massSpectrum.getRetentionTime());
	}

	@Test
	public void testRetentionIndex_1() {

		assertEquals("Retention Index", 0.0f, massSpectrum.getRetentionIndex(), 0);
	}

	@Test
	public void testRetentionIndex_2() {

		massSpectrum.setRetentionIndex(56.3f);
		assertEquals("Retention Index", 56.3f, massSpectrum.getRetentionIndex(), 0);
	}

	@Test
	public void testRetentionIndex_3() {

		massSpectrum.setRetentionIndex(-1.0f);
		assertEquals("Retention Index", 0.0f, massSpectrum.getRetentionIndex(), 0);
	}

	@Test
	public void testScanNumber_1() {

		assertEquals("Scan Number", 0, massSpectrum.getScanNumber());
	}

	@Test
	public void testScanNumber_2() {

		massSpectrum.setScanNumber(78);
		assertEquals("Scan Number", 78, massSpectrum.getScanNumber());
	}
}
