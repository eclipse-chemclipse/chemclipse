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
 * Christoph Läubrich - Adjust to new API
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.chromatogram;

import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.converter.chromatogram.ChromatogramConverterSupport;
import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.junit.Test;

/**
 * This TestCase analyzes if the class ChromatogramConverterSupport methods work
 * in a correct way. The behavior after initialization is especially analyzed in
 * this TestCase.
 */
public class ChromatogramConverterSupport_1_Test {

	private ChromatogramConverterSupport support = new ChromatogramConverterSupport(null);

	@Test
	public void testGetConverterId_1() {

		assertThrows(NoConverterAvailableException.class, () -> {
			support.getConverterId(1);
		});
	}

	@Test
	public void testGetFilterExtensions_1() {

		assertThrows(NoConverterAvailableException.class, () -> {
			support.getFilterExtensions();
		});
	}

	@Test
	public void testGetFilterNames_1() {

		assertThrows(NoConverterAvailableException.class, () -> {
			support.getFilterNames();
		});
	}
}
