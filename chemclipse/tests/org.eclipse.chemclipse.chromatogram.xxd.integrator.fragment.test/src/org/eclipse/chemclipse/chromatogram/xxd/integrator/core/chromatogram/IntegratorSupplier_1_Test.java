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

import org.junit.Before;
import org.junit.Test;

public class IntegratorSupplier_1_Test {

	private ChromatogramIntegratorSupplier supplier;

	@Before
	public void setUp() throws Exception {

		supplier = new ChromatogramIntegratorSupplier();
		supplier.setId("org.eclipse.chemclipse.test");
		supplier.setDescription("This is a description.");
		supplier.setIntegratorName("Integrator Name");
	}

	@Test
	public void testGetId_1() {

		assertEquals("Id", "org.eclipse.chemclipse.test", supplier.getId());
	}

	@Test
	public void testGetDescription_1() {

		assertEquals("Description", "This is a description.", supplier.getDescription());
	}

	@Test
	public void testGetFilterName_1() {

		assertEquals("Integrator Name", "Integrator Name", supplier.getIntegratorName());
	}
}
