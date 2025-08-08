/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.settings;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SupplierFilterSettings_2_Test {

	private ChromatogramFilterSettings settings = new ChromatogramFilterSettings();;

	@Test
	public void test1() {

		settings.setDerivative(5); // Other than 0 not supported
		assertEquals(5, settings.getDerivative());
	}

	@Test
	public void test2() {

		settings.setOrder(3);
		assertEquals(3, settings.getOrder());
	}

	@Test
	public void test3() {

		settings.setWidth(7);
		assertEquals(7, settings.getWidth());
	}
}
