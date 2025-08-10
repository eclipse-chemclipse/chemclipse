/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.core;

import org.eclipse.chemclipse.converter.core.IConverterSupportSetter;
import org.eclipse.chemclipse.converter.core.ISupplierSetter;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class AbstractConverterTestCase {

	private IConverterSupportSetter converterSupport = new DefaultConverterSupport();
	private ISupplierSetter supplier = new DefaultSupplier();

	@Before
	public void setUp() throws Exception {

		// Supplier I
		supplier.setId("org.eclipse.chemclipse.msd.converter.supplier.agilent");
		supplier.setDescription("Reads Agilent Chromatograms.");
		supplier.setFilterName("Agilent Chromatogram (*.D/DATA.MS)");
		supplier.setExportable(false);
		supplier.setImportable(true);
		supplier.setDirectoryExtension(".D");
		supplier.setFileExtension(".MS");
		supplier.setFileName("DATA");
		converterSupport.add(supplier);
		// Supplier II
		supplier = new DefaultSupplier();
		supplier.setId("org.eclipse.chemclipse.msd.converter.supplier.agilent.msd1");
		supplier.setDescription("Reads Agilent Chromatograms (MSD1).");
		supplier.setFilterName("Agilent Chromatogram (*.D/MSD1.MS)");
		supplier.setExportable(false);
		supplier.setImportable(true);
		supplier.setDirectoryExtension(".D");
		supplier.setFileExtension(".MSD1");
		supplier.setFileName("DATA");
		converterSupport.add(supplier);
		// Supplier III
		supplier = new DefaultSupplier();
		supplier.setId("net.openchrom.msd.converter.supplier.cdf");
		supplier.setDescription("Reads an writes ANDI/AIA CDF Chromatograms.");
		supplier.setFilterName("ANDI/AIA CDF Chromatogram (*.CDF)");
		supplier.setExportable(true);
		supplier.setImportable(true);
		supplier.setDirectoryExtension("");
		supplier.setFileExtension(".CDF");
		supplier.setFileName("");
		converterSupport.add(supplier);
		// Supplier IV
		supplier = new DefaultSupplier();
		supplier.setId("org.eclipse.chemclipse.msd.converter.supplier.excel");
		supplier.setDescription("Exports Chromatograms to Microsoft Excel 2007.");
		supplier.setFilterName("Excel Chromatogram (*.xlsx)");
		supplier.setExportable(true);
		supplier.setImportable(false);
		supplier.setDirectoryExtension("");
		supplier.setFileExtension(".xlsx");
		supplier.setFileName("");
		converterSupport.add(supplier);
		// Supplier V
		supplier = new DefaultSupplier();
		supplier.setId("org.eclipse.chemclipse.msd.converter.supplier.test");
		supplier.setDescription("Exports Test Chromatograms to Directories.");
		supplier.setFilterName("Test Chromatogram (*.C/CHROM.MS)");
		supplier.setExportable(true);
		supplier.setImportable(true);
		supplier.setDirectoryExtension(".C");
		supplier.setFileExtension(".MS");
		supplier.setFileName("CHROM");
		converterSupport.add(supplier);
	}

	public IConverterSupportSetter getConverterSupport() {

		return converterSupport;
	}
}
