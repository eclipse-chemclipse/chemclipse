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
package org.eclipse.chemclipse.chromatogram.msd.filter.core.chromatogram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramFilterSupport_1_Test {

	private ChromatogramFilterSupportMSD support;
	private ChromatogramFilterSupplierMSD supplier;

	@Before
	public void setUp() throws Exception {

		support = new ChromatogramFilterSupportMSD();
		supplier = new ChromatogramFilterSupplierMSD();
		supplier.setId("net.first.supplier");
		supplier.setDescription("Filter Description");
		supplier.setFilterName("Test Filter Name");
		support.add(supplier);
	}

	@Test
	public void testGetAvailableFilterIds_1() {

		try {
			List<String> ids = support.getAvailableFilterIds();
			assertEquals("getId", "net.first.supplier", ids.get(0));
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			assertTrue("NoChromatogramFilterSupplierAvailableException", false);
		}
	}

	@Test
	public void testGetIntegratorId_1() {

		try {
			String name = support.getFilterId(0);
			assertEquals("Name", "net.first.supplier", name);
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			assertTrue("NoChromatogramFilterSupplierAvailableException", false);
		}
	}

	@Test
	public void testGetIntegratorSupplier_1() {

		IChromatogramFilterSupplierMSD supplier;
		try {
			supplier = support.getFilterSupplier("net.first.supplier");
			assertNotNull(supplier);
			assertEquals("Name", "Test Filter Name", supplier.getFilterName());
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			assertTrue("NoChromatogramFilterSupplierAvailableException", false);
		}
	}

	@Test
	public void testGetIntegratorNames_1() {

		try {
			String[] names = support.getFilterNames();
			assertEquals("length", 1, names.length);
			assertEquals("name", "Test Filter Name", names[0]);
		} catch(NoChromatogramFilterSupplierAvailableException e) {
			assertTrue("NoChromatogramFilterSupplierAvailableException", false);
		}
	}
}
