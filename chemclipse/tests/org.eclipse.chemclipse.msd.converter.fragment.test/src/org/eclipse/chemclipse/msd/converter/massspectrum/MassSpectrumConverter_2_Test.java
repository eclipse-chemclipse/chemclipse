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

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.msd.converter.database.DatabaseConverter;
import org.eclipse.chemclipse.msd.converter.database.DatabaseConverterSupport;
import org.junit.Test;

/**
 * Testing the method getMassSpectrumConverterSupport() in
 * MassSpectrumConverter.
 */
public class MassSpectrumConverter_2_Test {

	private DatabaseConverterSupport support = DatabaseConverter.getDatabaseConverterSupport();

	@Test
	public void testFilterNames_1() throws NoConverterAvailableException {

		String[] filterNames = support.getFilterNames();
		String result = "";
		for(String name : filterNames) {
			result += name + ";";
		}
		assertEquals("FilterName", true, result.contains("AMDIS Mass Spectra (*.msl)"));
		assertEquals("FilterName", true, result.contains("NIST Text (*.msp)"));
		// and others
	}
}
