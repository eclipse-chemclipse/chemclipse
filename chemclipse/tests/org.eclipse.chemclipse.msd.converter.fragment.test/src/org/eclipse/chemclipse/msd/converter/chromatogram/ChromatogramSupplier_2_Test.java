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
package org.eclipse.chemclipse.msd.converter.chromatogram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.converter.chromatogram.ChromatogramSupplier;
import org.junit.Test;

/**
 * Testing toString(), hashCode() and equals() of ChromatogramSupplier.
 */
public class ChromatogramSupplier_2_Test {

	private ChromatogramSupplier supplier = new ChromatogramSupplier();

	@Test
	public void testToString_1() {

		String test = "org.eclipse.chemclipse.converter.chromatogram.ChromatogramSupplier[id=,description=,filterName=,fileExtension=,fileName=,directoryExtension=,isExportable=false,isImportable=false]";
		assertEquals("toString", test, supplier.toString());
	}

	@Test
	public void testEquals_1() {

		ChromatogramSupplier anotherSupplier = new ChromatogramSupplier();
		assertEquals("equals", true, supplier.equals(anotherSupplier));
	}

	@Test
	public void testEquals_2() {

		ChromatogramSupplier anotherSupplier = new ChromatogramSupplier();
		anotherSupplier.setId("chemclipse");
		assertEquals("equals", false, supplier.equals(anotherSupplier));
		anotherSupplier.setId("");
		assertEquals("equals", true, supplier.equals(anotherSupplier));
		anotherSupplier.setDescription("chemclipse");
		assertEquals("equals", false, supplier.equals(anotherSupplier));
		anotherSupplier.setDescription("");
		assertEquals("equals", true, supplier.equals(anotherSupplier));
		anotherSupplier.setFilterName("chemclipse");
		assertEquals("equals", false, supplier.equals(anotherSupplier));
		anotherSupplier.setFilterName("");
		assertEquals("equals", true, supplier.equals(anotherSupplier));
		anotherSupplier.setFileExtension("chemclipse");
		assertEquals("equals", false, supplier.equals(anotherSupplier));
		anotherSupplier.setFileExtension("");
		assertEquals("equals", true, supplier.equals(anotherSupplier));
		anotherSupplier.setFileName("chemclipse");
		assertEquals("equals", false, supplier.equals(anotherSupplier));
		anotherSupplier.setFileName("");
		assertEquals("equals", true, supplier.equals(anotherSupplier));
		anotherSupplier.setDirectoryExtension("chemclipse");
		assertEquals("equals", false, supplier.equals(anotherSupplier));
		anotherSupplier.setDirectoryExtension("");
		assertEquals("equals", true, supplier.equals(anotherSupplier));
	}

	@Test
	public void testEquals_3() {

		ChromatogramSupplier anotherSupplier = new ChromatogramSupplier();
		anotherSupplier.setId("chemclipse");
		assertEquals("equals", false, anotherSupplier.equals(supplier));
		anotherSupplier.setId("");
		assertEquals("equals", true, anotherSupplier.equals(supplier));
		anotherSupplier.setDescription("chemclipse");
		assertEquals("equals", false, anotherSupplier.equals(supplier));
		anotherSupplier.setDescription("");
		assertEquals("equals", true, anotherSupplier.equals(supplier));
		anotherSupplier.setFilterName("chemclipse");
		assertEquals("equals", false, anotherSupplier.equals(supplier));
		anotherSupplier.setFilterName("");
		assertEquals("equals", true, anotherSupplier.equals(supplier));
		anotherSupplier.setFileExtension("chemclipse");
		assertEquals("equals", false, anotherSupplier.equals(supplier));
		anotherSupplier.setFileExtension("");
		assertEquals("equals", true, anotherSupplier.equals(supplier));
		anotherSupplier.setFileName("chemclipse");
		assertEquals("equals", false, anotherSupplier.equals(supplier));
		anotherSupplier.setFileName("");
		assertEquals("equals", true, anotherSupplier.equals(supplier));
		anotherSupplier.setDirectoryExtension("chemclipse");
		assertEquals("equals", false, anotherSupplier.equals(supplier));
		anotherSupplier.setDirectoryExtension("");
		assertEquals("equals", true, anotherSupplier.equals(supplier));
	}

	@Test
	public void testEquals_4() {

		ChromatogramSupplier anotherSupplier = supplier;
		assertEquals("equals", true, anotherSupplier.equals(supplier));
	}

	@Test
	public void testEquals_5() {

		assertEquals("equals", true, supplier.equals(supplier));
	}

	@Test
	public void testEquals_6() {

		ChromatogramSupplier anotherSupplier = null;
		assertEquals("equals", false, supplier.equals(anotherSupplier));
	}

	@Test
	public void testEquals_7() {

		Object anotherSupplier = new Object();
		assertEquals("equals", false, supplier.equals(anotherSupplier));
	}

	@Test
	public void testHashCode_1() {

		ChromatogramSupplier anotherSupplier = new ChromatogramSupplier();
		assertEquals("hashCode", anotherSupplier.hashCode(), supplier.hashCode());
	}

	@Test
	public void testHashCode_2() {

		ChromatogramSupplier anotherSupplier = new ChromatogramSupplier();
		anotherSupplier.setId("chemclipse");
		assertFalse("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setId("");
		assertTrue("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setDescription("chemclipse");
		assertFalse("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setDescription("");
		assertTrue("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setFilterName("chemclipse");
		assertFalse("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setFilterName("");
		assertTrue("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setFileExtension("chemclipse");
		assertFalse("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setFileExtension("");
		assertTrue("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setFileName("chemclipse");
		assertFalse("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setFileName("");
		assertTrue("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setDirectoryExtension("chemclipse");
		assertFalse("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setDirectoryExtension("");
		assertTrue("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setImportable(true);
		assertFalse("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setImportable(false);
		assertTrue("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setExportable(true);
		assertFalse("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
		anotherSupplier.setExportable(false);
		assertTrue("hashCode", anotherSupplier.hashCode() == supplier.hashCode());
	}
}
