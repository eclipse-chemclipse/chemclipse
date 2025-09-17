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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.chemclipse.msd.identifier.comparison.MassSpectrumComparisonSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class MassSpectrumComparisonSupplier_3_Test {

	private MassSpectrumComparisonSupplier supplier1;
	private MassSpectrumComparisonSupplier supplier2;

	@BeforeAll
	public void setUp() {

		supplier1 = new MassSpectrumComparisonSupplier();
		supplier1.setId("id");
		supplier1.setDescription("description");
		supplier1.setComparatorName("comparatorName");

		supplier2 = new MassSpectrumComparisonSupplier();
		supplier2.setId("id");
		supplier2.setDescription("description");
		supplier2.setComparatorName("comparatorName");
	}

	@Test
	public void testEquals_1() {

		assertEquals(supplier1, supplier2);
	}

	@Test
	public void testEquals_2() {

		assertEquals(supplier2, supplier1);
	}

	@Test
	public void testEquals_3() {

		assertNotNull(supplier1);
	}

	@Test
	public void testEquals_4() {

		assertNotEquals(supplier1, new Object());
	}

	@Test
	public void testHashCode_1() {

		assertEquals(supplier1.hashCode(), supplier2.hashCode());
	}
}
