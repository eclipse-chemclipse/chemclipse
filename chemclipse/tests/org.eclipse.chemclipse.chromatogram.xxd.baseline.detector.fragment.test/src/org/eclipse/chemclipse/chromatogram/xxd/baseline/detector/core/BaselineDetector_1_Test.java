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
package org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.exceptions.NoBaselineDetectorAvailableException;
import org.junit.Test;

/**
 * Test the IBaselineDetectorSupport.
 * 
 * @author Philip Wenig
 */
public class BaselineDetector_1_Test {

	IBaselineDetectorSupport support = BaselineDetector.getBaselineDetectorSupport();

	@Test
	public void testGetMassSpectrumComparatorSupport_1() throws NoBaselineDetectorAvailableException {

		int count = 0;
		String[] names = support.getDetectorNames();
		String[] rcs = new String[1];
		rcs[0] = "Threshold (TIC) Baseline Detector"; // Was removed
		for(String name : names) {
			for(String rc : rcs) {
				if(name.equals(rc)) {
					count++;
				}
			}
		}
		assertEquals("Registered Detector Names", 0, count);
	}

	@Test
	public void testGetMassSpectrumComparatorSupport_2() throws NoBaselineDetectorAvailableException {

		int count = 0;
		List<String> ids = support.getAvailableDetectorIds();
		String[] rcs = new String[1];
		rcs[0] = "org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.supplier.tic"; // Was removed
		for(String id : ids) {
			for(String rc : rcs) {
				if(id.equals(rc)) {
					count++;
				}
			}
		}
		assertEquals("Registered Detector Ids", 0, count);
	}

	@Test
	public void testGetMassSpectrumComparatorSupport_3() throws NoBaselineDetectorAvailableException {

		List<String> ids = support.getAvailableDetectorIds();
		List<String> rcs = new ArrayList<String>();
		for(String id : ids) {
			rcs.add(id);
		}
		String id;
		for(int i = 0; i < rcs.size(); i++) {
			id = support.getDetectorId(i);
			assertEquals("getDetectorId", id, rcs.get(i));
		}
	}

	@Test
	public void testGetMassSpectrumComparisonSupplier_1() {

		try {
			support.getBaselineDetectorSupplier("");
		} catch(NoBaselineDetectorAvailableException e) {
			assertTrue("NoBaselineDetectorAvailableException", true);
		}
	}

	@Test
	public void testGetMassSpectrumComparisonSupplier_2() {

		try {
			support.getBaselineDetectorSupplier(null);
		} catch(NoBaselineDetectorAvailableException e) {
			assertTrue("NoBaselineDetectorAvailableException", true);
		}
	}

	@Test
	public void testGetMassSpectrumComparisonSupplier_3() throws NoBaselineDetectorAvailableException {

		String id = "org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.supplier.tic";
		try {
			IBaselineDetectorSupplier supplier = support.getBaselineDetectorSupplier(id);
			assertNull(supplier);
		} catch(Exception e) {
			assertTrue(true); // The detector was removed
		}
	}
}
