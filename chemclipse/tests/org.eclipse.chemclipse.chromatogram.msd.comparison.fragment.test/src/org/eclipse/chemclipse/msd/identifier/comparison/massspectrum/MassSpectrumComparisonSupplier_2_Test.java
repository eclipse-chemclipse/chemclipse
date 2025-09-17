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
package org.eclipse.chemclipse.msd.identifier.comparison.massspectrum;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.msd.identifier.comparison.MassSpectrumComparisonSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class MassSpectrumComparisonSupplier_2_Test {

	private MassSpectrumComparisonSupplier supplier;
	private String id = "id";
	private String description = "description";
	private String comparatorName = "comparatorName";

	@BeforeAll
	public void setUp() {

		supplier = new MassSpectrumComparisonSupplier();
		supplier.setId(id);
		supplier.setDescription(description);
		supplier.setComparatorName(comparatorName);
	}

	@Test
	public void testGetId_1() {

		assertEquals(id, supplier.getId());
		id = "newId";
		supplier.setId(id);
		assertEquals(id, supplier.getId());
	}

	@Test
	public void testGetDescription_1() {

		assertEquals(description, supplier.getDescription());
		description = "newDescription";
		supplier.setDescription(description);
		assertEquals(description, supplier.getDescription());
	}

	@Test
	public void testGetDetectorName_1() {

		assertEquals(comparatorName, supplier.getComparatorName());
		comparatorName = "newDetectorName";
		supplier.setComparatorName(comparatorName);
		assertEquals(comparatorName, supplier.getComparatorName());
	}
}
