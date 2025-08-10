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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.converter.chromatogram.ChromatogramConverterSupport;
import org.eclipse.chemclipse.converter.chromatogram.ChromatogramSupplier;
import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.junit.Before;
import org.junit.Test;

/**
 * This TestCase analyses if the class ChromatogramConverterSupport methods work
 * in a correct way.
 */
public class ChromatogramConverterSupport_2_Test {

	private ChromatogramConverterSupport support = new ChromatogramConverterSupport(null);

	@Before
	public void setUp() throws Exception {

		ChromatogramSupplier supplier = new ChromatogramSupplier();
		supplier.setId("org.eclipse.chemclipse.msd.converter.supplier.agilent");
		supplier.setFileExtension(".MS");
		supplier.setDirectoryExtension(".D");
		supplier.setFilterName("Agilent Chromatogram (.D)");
		support.add(supplier);

		supplier = new ChromatogramSupplier();
		supplier.setId("org.eclipse.chemclipse.msd.converter.supplier.netCDF");
		supplier.setFileExtension(".netCDF");
		supplier.setDirectoryExtension("");
		supplier.setFilterName("ANDI/AIA (.netCDF)");
		support.add(supplier);

		supplier = new ChromatogramSupplier();
		supplier.setId("org.eclipse.chemclipse.xxd.converter.supplier.chemclipse");
		supplier.setFileExtension(".chrom");
		supplier.setDirectoryExtension("");
		supplier.setFilterName("ChemClipse Chromatogram (.chrom)");
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

		String id = support.getConverterId(0);
		assertEquals("id#0", "org.eclipse.chemclipse.msd.converter.supplier.agilent", id);
		id = support.getConverterId(1);
		assertEquals("id#1", "org.eclipse.chemclipse.msd.converter.supplier.netCDF", id);
		id = support.getConverterId(2);
		assertEquals("id#2", "org.eclipse.chemclipse.xxd.converter.supplier.chemclipse", id);
	}

	@Test
	public void testGetFilterExtensions_1() throws NoConverterAvailableException {

		String[] ids = support.getFilterExtensions();
		assertEquals("FilterExtension #0", "*.", ids[0]); // Important ... otherwise 'Save As...' fails
		assertEquals("FilterExtension #1", "*.netCDF;*.netcdf;*.NETCDF", ids[1]);
		assertEquals("FilterExtension #2", "*.chrom;*.CHROM", ids[2]);
	}

	@Test
	public void testGetFilterNames_1() throws NoConverterAvailableException {

		String[] names = support.getFilterNames();
		assertEquals("FilterName #0", "Agilent Chromatogram (.D)", names[0]);
		assertEquals("FilterName #1", "ANDI/AIA (.netCDF)", names[1]);
		assertEquals("FilterName #2", "ChemClipse Chromatogram (.chrom)", names[2]);
	}
}
