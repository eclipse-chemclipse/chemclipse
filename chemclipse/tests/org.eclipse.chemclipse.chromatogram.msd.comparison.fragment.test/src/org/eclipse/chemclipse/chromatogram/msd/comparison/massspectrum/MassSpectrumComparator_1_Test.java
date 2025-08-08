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
package org.eclipse.chemclipse.chromatogram.msd.comparison.massspectrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.comparison.exceptions.NoMassSpectrumComparatorAvailableException;
import org.junit.Test;

public class MassSpectrumComparator_1_Test {

	IMassSpectrumComparatorSupport support = MassSpectrumComparator.getMassSpectrumComparatorSupport();

	@Test
	public void testGetMassSpectrumComparatorSupport_1() throws NoMassSpectrumComparatorAvailableException {

		int count = 0;
		String[] names = support.getComparatorNames();
		String[] rcs = new String[2];
		rcs[0] = "Cosine";
		rcs[1] = "Alfassi Geometric Distance";
		for(String name : names) {
			for(String rc : rcs) {
				if(name.equals(rc)) {
					count++;
				}
			}
		}
		assertTrue(count >= 2);
	}

	@Test
	public void testGetMassSpectrumComparatorSupport_2() throws NoMassSpectrumComparatorAvailableException {

		int count = 0;
		List<String> ids = support.getAvailableComparatorIds();
		String[] rcs = new String[2];
		rcs[0] = "org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.cosine";
		rcs[1] = "org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.alfassi.geometric";
		for(String id : ids) {
			for(String rc : rcs) {
				if(id.equals(rc)) {
					count++;
				}
			}
		}
		assertTrue(count >= 2);
	}

	@Test
	public void testGetMassSpectrumComparatorSupport_3() throws NoMassSpectrumComparatorAvailableException {

		List<String> ids = support.getAvailableComparatorIds();
		List<String> rcs = new ArrayList<String>();
		for(String id : ids) {
			rcs.add(id);
		}
		String id;
		for(int i = 0; i < rcs.size(); i++) {
			id = support.getComparatorId(i);
			assertEquals("getComparatorId", id, rcs.get(i));
		}
	}

	@Test
	public void testGetMassSpectrumComparisonSupplier_1() {

		try {
			support.getMassSpectrumComparisonSupplier("");
		} catch(NoMassSpectrumComparatorAvailableException e) {
			assertTrue("NoMassSpectrumComparatorAvailableException", true);
		}
	}

	@Test
	public void testGetMassSpectrumComparisonSupplier_2() {

		try {
			support.getMassSpectrumComparisonSupplier(null);
		} catch(NoMassSpectrumComparatorAvailableException e) {
			assertTrue("NoMassSpectrumComparatorAvailableException", true);
		}
	}

	@Test
	public void testGetMassSpectrumComparisonSupplier_4() throws NoMassSpectrumComparatorAvailableException {

		String comparatorName = "Alfassi Geometric Distance";
		String description = "This comparator calculates the similarity between two mass spectra with the alfassi geomtric distance algorithm.";
		String id = "org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.alfassi.geometric";
		IMassSpectrumComparisonSupplier supplier = support.getMassSpectrumComparisonSupplier(id);
		assertEquals("ComparatorName", comparatorName, supplier.getComparatorName());
		assertEquals("Description", description, supplier.getDescription());
		assertEquals("Id", id, supplier.getId());
	}
}
