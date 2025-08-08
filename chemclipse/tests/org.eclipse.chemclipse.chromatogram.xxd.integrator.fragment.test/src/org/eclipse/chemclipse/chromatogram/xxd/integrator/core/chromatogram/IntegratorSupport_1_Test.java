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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.core.chromatogram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.exceptions.NoIntegratorAvailableException;
import org.junit.Before;
import org.junit.Test;

public class IntegratorSupport_1_Test {

	private ChromatogramIntegratorSupport support;
	private ChromatogramIntegratorSupplier supplier;

	@Before
	public void setUp() throws Exception {

		support = new ChromatogramIntegratorSupport();
		supplier = new ChromatogramIntegratorSupplier();
		supplier.setId("net.first.supplier");
		supplier.setDescription("Integrator Description");
		supplier.setIntegratorName("Test Integrator Name");
		support.add(supplier);
	}

	@Test
	public void testGetAvailableIntegratorIds_1() {

		List<String> ids;
		try {
			ids = support.getAvailableIntegratorIds();
			assertEquals("getId", "net.first.supplier", ids.get(0));
		} catch(NoIntegratorAvailableException e) {
			assertTrue("NoIntegratorAvailableException", false);
		}
	}

	@Test
	public void testGetIntegratorId_1() {

		try {
			String name = support.getIntegratorId(0);
			assertEquals("Name", "net.first.supplier", name);
		} catch(NoIntegratorAvailableException e) {
			assertTrue("NoIntegratorAvailableException", false);
		}
	}

	@Test
	public void testGetIntegratorSupplier_1() {

		IChromatogramIntegratorSupplier supplier;
		try {
			supplier = support.getIntegratorSupplier("net.first.supplier");
			assertNotNull(supplier);
			assertEquals("Name", "Test Integrator Name", supplier.getIntegratorName());
		} catch(NoIntegratorAvailableException e) {
			assertTrue("NoIntegratorAvailableException", false);
		}
	}

	@Test
	public void testGetIntegratorNames_1() {

		try {
			String[] names = support.getIntegratorNames();
			assertEquals("length", 1, names.length);
			assertEquals("name", "Test Integrator Name", names[0]);
		} catch(NoIntegratorAvailableException e) {
			assertTrue("NoIntegratorAvailableException", false);
		}
	}
}
