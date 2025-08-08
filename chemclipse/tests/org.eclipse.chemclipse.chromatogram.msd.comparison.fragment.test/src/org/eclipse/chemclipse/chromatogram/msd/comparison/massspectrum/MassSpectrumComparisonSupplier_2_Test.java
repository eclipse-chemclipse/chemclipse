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

import org.junit.Before;
import org.junit.Test;

public class MassSpectrumComparisonSupplier_2_Test {

	private MassSpectrumComparisonSupplier supplier;
	private String id = "id";
	private String description = "description";
	private String comparatorName = "comparatorName";

	@Before
	public void setUp() throws Exception {

		supplier = new MassSpectrumComparisonSupplier();
		supplier.setId(id);
		supplier.setDescription(description);
		supplier.setComparatorName(comparatorName);
	}

	@Test
	public void testGetId_1() {

		assertEquals("id", id, supplier.getId());
		id = "newId";
		supplier.setId(id);
		assertEquals("id", id, supplier.getId());
	}

	@Test
	public void testGetDescription_1() {

		assertEquals("description", description, supplier.getDescription());
		description = "newDescription";
		supplier.setDescription(description);
		assertEquals("description", description, supplier.getDescription());
	}

	@Test
	public void testGetDetectorName_1() {

		assertEquals("comparatorName", comparatorName, supplier.getComparatorName());
		comparatorName = "newDetectorName";
		supplier.setComparatorName(comparatorName);
		assertEquals("comparatorName", comparatorName, supplier.getComparatorName());
	}
}
