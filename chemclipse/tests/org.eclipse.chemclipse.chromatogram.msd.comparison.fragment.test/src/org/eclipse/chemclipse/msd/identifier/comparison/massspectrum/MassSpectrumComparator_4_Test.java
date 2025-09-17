/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.chemclipse.msd.identifier.comparison.IMassSpectrumComparator;
import org.eclipse.chemclipse.msd.identifier.comparison.IMassSpectrumComparisonSupplier;
import org.eclipse.chemclipse.msd.identifier.comparison.MassSpectrumComparator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class MassSpectrumComparator_4_Test {

	private IMassSpectrumComparator comparator;
	private IMassSpectrumComparisonSupplier supplier;

	@BeforeAll
	public void setUp() {

		comparator = MassSpectrumComparator.getMassSpectrumComparator("org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.cosine");
		supplier = comparator.getMassSpectrumComparisonSupplier();
	}

	@Test
	public void test_1() {

		assertNotNull(comparator);
	}

	@Test
	public void test_2() {

		assertNotNull(supplier);
	}

	@Test
	public void test_3() {

		assertEquals("org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.cosine", supplier.getId());
	}

	@Test
	public void test_4() {

		assertEquals("Cosine", supplier.getComparatorName());
	}

	@Test
	public void test_5() {

		assertEquals("The comparator uses the cosine match.", supplier.getDescription());
	}

	@Test
	public void test_6() {

		assertEquals(true, supplier.supportsNominalMS());
	}

	@Test
	public void test_7() {

		assertEquals(false, supplier.supportsTandemMS());
	}

	@Test
	public void test_8() {

		assertEquals(false, supplier.supportsHighResolutionMS());
	}
}
