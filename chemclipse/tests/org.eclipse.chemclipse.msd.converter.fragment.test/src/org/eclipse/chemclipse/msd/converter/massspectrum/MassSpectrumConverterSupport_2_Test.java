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
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.junit.Before;
import org.junit.Test;

/**
 * This TestCase analyses if the class MassSpectrumConverterSupport methods work
 * in a correct way.
 */
public class MassSpectrumConverterSupport_2_Test {

	private MassSpectrumConverterSupport support = new MassSpectrumConverterSupport();

	@Before
	public void setUp() throws Exception {

		MassSpectrumSupplier supplier = new MassSpectrumSupplier();
		supplier.setId("org.eclipse.chemclipse.msd.converter.supplier.ascii");
		supplier.setFileExtension(".txt");
		supplier.setDirectoryExtension(".TXT");
		supplier.setFilterName("ASCII Text (.TXT)");
		support.add(supplier);

		supplier = new MassSpectrumSupplier();
		supplier.setId("org.eclipse.chemclipse.msd.converter.supplier.amdis");
		supplier.setFileExtension(".msl");
		supplier.setDirectoryExtension("");
		supplier.setFilterName("AMDIS Mass Spectra (*.msl)");
		support.add(supplier);

		supplier = new MassSpectrumSupplier();
		supplier.setId("org.eclipse.chemclipse.msd.converter.supplier.jcamp");
		supplier.setFileExtension(".jdx");
		supplier.setDirectoryExtension("");
		supplier.setFilterName("JCAMP-DX (*.jdx)");
		support.add(supplier);
	}

	@Test
	public void testGetConverterId_1() {

		assertThrows(NoConverterAvailableException.class, () -> {
			support.getConverterId(-1);
		});
	}

	@Test
	public void testGetConverterId_2() {

		assertThrows(NoConverterAvailableException.class, () -> {
			support.getConverterId(3);
		});
	}

	@Test
	public void testGetConverterId_3() throws NoConverterAvailableException {

		String id;
		id = support.getConverterId(0);
		assertEquals("id#0", "org.eclipse.chemclipse.msd.converter.supplier.ascii", id);
		id = support.getConverterId(1);
		assertEquals("id#1", "org.eclipse.chemclipse.msd.converter.supplier.amdis", id);
		id = support.getConverterId(2);
		assertEquals("id#2", "org.eclipse.chemclipse.msd.converter.supplier.jcamp", id);
	}

	@Test
	public void testGetFilterExtensions_1() throws NoConverterAvailableException {

		String[] ids;
		ids = support.getFilterExtensions();
		assertEquals("FilterExtension #0", "*.", ids[0]); // Important ... otherwise 'Save As...' fails
		assertEquals("FilterExtension #1", "*.msl;*.MSL", ids[1]);
		assertEquals("FilterExtension #2", "*.jdx;*.JDX", ids[2]);
	}

	@Test
	public void testGetFilterNames_1() throws NoConverterAvailableException {

		String[] names;
		names = support.getFilterNames();
		assertEquals("FilterName #0", "ASCII Text (.TXT)", names[0]);
		assertEquals("FilterName #1", "AMDIS Mass Spectra (*.msl)", names[1]);
		assertEquals("FilterName #2", "JCAMP-DX (*.jdx)", names[2]);
	}
}
