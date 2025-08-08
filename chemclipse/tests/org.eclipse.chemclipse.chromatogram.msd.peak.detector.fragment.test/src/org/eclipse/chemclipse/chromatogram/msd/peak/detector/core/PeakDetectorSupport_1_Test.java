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
package org.eclipse.chemclipse.chromatogram.msd.peak.detector.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.peak.detector.core.IPeakDetectorSupplier;
import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;
import org.junit.Before;
import org.junit.Test;

public class PeakDetectorSupport_1_Test {

	private PeakDetectorMSDSupport support = new PeakDetectorMSDSupport();

	@Before
	public void setUp() throws Exception {

		PeakDetectorMSDSupplier supplier = new PeakDetectorMSDSupplier("net.first.supplier", "Integrator Description", "Peak Detector Name");
		support.add(supplier);
	}

	@Test
	public void testGetAvailableIntegratorIds_1() {

		List<String> ids;
		try {
			ids = support.getAvailablePeakDetectorIds();
			assertEquals("getId", "net.first.supplier", ids.get(0));
		} catch(NoPeakDetectorAvailableException e) {
			assertTrue("NoPeakDetectorAvailableException", false);
		}
	}

	@Test
	public void testGetIntegratorId_1() {

		try {
			String name = support.getPeakDetectorId(0);
			assertEquals("Name", "net.first.supplier", name);
		} catch(NoPeakDetectorAvailableException e) {
			assertTrue("NoPeakDetectorAvailableException", false);
		}
	}

	@Test
	public void testGetIntegratorSupplier_1() {

		IPeakDetectorSupplier supplier;
		try {
			supplier = support.getPeakDetectorSupplier("net.first.supplier");
			assertNotNull(supplier);
			assertEquals("Name", "Peak Detector Name", supplier.getPeakDetectorName());
		} catch(NoPeakDetectorAvailableException e) {
			assertTrue("NoPeakDetectorAvailableException", false);
		}
	}

	@Test
	public void testGetIntegratorNames_1() {

		try {
			String[] names = support.getPeakDetectorNames();
			assertEquals("length", 1, names.length);
			assertEquals("name", "Peak Detector Name", names[0]);
		} catch(NoPeakDetectorAvailableException e) {
			assertTrue("NoPeakDetectorAvailableException", false);
		}
	}
}
