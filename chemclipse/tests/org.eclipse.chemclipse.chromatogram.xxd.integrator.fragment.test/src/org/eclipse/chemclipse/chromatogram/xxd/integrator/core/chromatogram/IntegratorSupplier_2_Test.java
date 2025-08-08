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

import org.junit.Test;

public class IntegratorSupplier_2_Test {

	private ChromatogramIntegratorSupplier supplier = new ChromatogramIntegratorSupplier();

	@Test
	public void testGetId_1() {

		assertEquals("Id", "", supplier.getId());
	}

	@Test
	public void testGetDescription_1() {

		assertEquals("Description", "", supplier.getDescription());
	}

	@Test
	public void testGetFilterName_1() {

		assertEquals("Filter Name", "", supplier.getIntegratorName());
	}
}
