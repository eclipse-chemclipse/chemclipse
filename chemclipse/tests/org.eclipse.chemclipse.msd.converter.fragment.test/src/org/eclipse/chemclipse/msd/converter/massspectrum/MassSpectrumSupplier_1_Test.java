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

import org.junit.Test;

/**
 * Testing all methods of MassSpectrumSupplier and its specification.
 */
public class MassSpectrumSupplier_1_Test {

	private MassSpectrumSupplier supplier = new MassSpectrumSupplier();

	@Test
	public void testId_1() {

		assertEquals("id", "", supplier.getId());
		supplier.setId(null);
		assertEquals("id", "", supplier.getId());
		supplier.setId("org.eclipse.chemclipse.msd.converter.supplier.agilent");
		assertEquals("id", "org.eclipse.chemclipse.msd.converter.supplier.agilent", supplier.getId());
		supplier.setId("");
		assertEquals("id", "", supplier.getId());
	}

	@Test
	public void testDescription_1() {

		assertEquals("desciption", "", supplier.getDescription());
		supplier.setDescription(null);
		assertEquals("desciption", "", supplier.getDescription());
		supplier.setDescription("This is the agilent data format.");
		assertEquals("desciption", "This is the agilent data format.", supplier.getDescription());
		supplier.setDescription("");
		assertEquals("desciption", "", supplier.getDescription());
	}

	@Test
	public void testFilterName_1() {

		assertEquals("desciption", "", supplier.getDescription());
		supplier.setDescription(null);
		assertEquals("desciption", "", supplier.getDescription());
		supplier.setDescription("This is the agilent data format.");
		assertEquals("desciption", "This is the agilent data format.", supplier.getDescription());
		supplier.setDescription("");
		assertEquals("desciption", "", supplier.getDescription());
	}

	@Test
	public void testFileExtension_1() {

		assertEquals("fileExtension", "", supplier.getFileExtension());
		supplier.setFileExtension(null);
		assertEquals("fileExtension", "", supplier.getFileExtension());
		supplier.setFileExtension("MS");
		assertEquals("fileExtension", "MS", supplier.getFileExtension());
		supplier.setFileExtension(".MS");
		assertEquals("fileExtension", ".MS", supplier.getFileExtension());
		supplier.setFileExtension("");
		assertEquals("fileExtension", "", supplier.getFileExtension());
	}

	@Test
	public void testFileName_1() {

		assertEquals("fileName", "", supplier.getFileName());
		supplier.setFileName(null);
		assertEquals("fileName", "", supplier.getFileName());
		supplier.setFileName("DATA");
		assertEquals("fileName", "DATA", supplier.getFileName());
		supplier.setFileName("");
		assertEquals("fileName", "", supplier.getFileName());
	}

	@Test
	public void testDirectoryExtension_1() {

		assertEquals("directoryExtension", "", supplier.getDirectoryExtension());
		supplier.setDirectoryExtension(null);
		assertEquals("directoryExtension", "", supplier.getDirectoryExtension());
		supplier.setDirectoryExtension("D");
		assertEquals("directoryExtension", ".D", supplier.getDirectoryExtension());
		supplier.setDirectoryExtension(".D");
		assertEquals("directoryExtension", ".D", supplier.getDirectoryExtension());
		supplier.setDirectoryExtension("");
		assertEquals("directoryExtension", "", supplier.getDirectoryExtension());
	}

	@Test
	public void testIsImportable_1() {

		assertEquals("isImportable", false, supplier.isImportable());
		supplier.setImportable(false);
		assertEquals("isImportable", false, supplier.isImportable());
		supplier.setImportable(true);
		assertEquals("isImportable", true, supplier.isImportable());
	}

	@Test
	public void testIsExportable_1() {

		assertEquals("isExportable", false, supplier.isExportable());
		supplier.setExportable(false);
		assertEquals("isExportable", false, supplier.isExportable());
		supplier.setExportable(true);
		assertEquals("isExportable", true, supplier.isExportable());
	}
}
