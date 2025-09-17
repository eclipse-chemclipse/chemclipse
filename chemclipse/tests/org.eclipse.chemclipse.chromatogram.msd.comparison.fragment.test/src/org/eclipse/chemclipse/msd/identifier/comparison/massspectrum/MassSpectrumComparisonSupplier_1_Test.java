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
import org.junit.jupiter.api.Test;

public class MassSpectrumComparisonSupplier_1_Test {

	private MassSpectrumComparisonSupplier supplier = new MassSpectrumComparisonSupplier();

	@Test
	public void testGetId_1() {

		assertEquals("", supplier.getId());
	}

	@Test
	public void testGetDescription_1() {

		assertEquals("", supplier.getDescription());
	}

	@Test
	public void testGetComparatorName_1() {

		assertEquals("", supplier.getComparatorName());
	}
}
