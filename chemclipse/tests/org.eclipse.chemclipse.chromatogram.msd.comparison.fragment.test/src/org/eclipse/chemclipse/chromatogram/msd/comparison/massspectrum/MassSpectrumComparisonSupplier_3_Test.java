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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class MassSpectrumComparisonSupplier_3_Test {

	private MassSpectrumComparisonSupplier supplier1;
	private MassSpectrumComparisonSupplier supplier2;

	@Before
	public void setUp() throws Exception {

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

		assertEquals("equals", supplier1, supplier2);
	}

	@Test
	public void testEquals_2() {

		assertEquals("equals", supplier2, supplier1);
	}

	@Test
	public void testEquals_3() {

		assertNotNull("equals", supplier1);
	}

	@Test
	public void testEquals_4() {

		assertNotEquals("equals", supplier1, new Object());
	}

	@Test
	public void testHashCode_1() {

		assertEquals("hashCode", supplier1.hashCode(), supplier2.hashCode());
	}
}
