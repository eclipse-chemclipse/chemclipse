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
package org.eclipse.chemclipse.msd.converter.massspectrum;

import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.junit.Test;

/**
 * This TestCase analyses if the class MassSpectrumConverterSupport methods work
 * in a correct way. The behaviour after initialization is especially analysed
 * in this TestCase.
 */
public class MassSpectrumConverterSupport_1_Test {

	private MassSpectrumConverterSupport support = new MassSpectrumConverterSupport();

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
