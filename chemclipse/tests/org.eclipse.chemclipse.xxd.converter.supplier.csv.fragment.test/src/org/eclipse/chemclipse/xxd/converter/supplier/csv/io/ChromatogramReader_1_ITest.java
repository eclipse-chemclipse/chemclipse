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
package org.eclipse.chemclipse.xxd.converter.supplier.csv.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.eclipse.chemclipse.model.settings.Delimiter;
import org.eclipse.chemclipse.xxd.converter.supplier.csv.TestPathHelper;
import org.eclipse.chemclipse.xxd.converter.supplier.csv.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.versions.VersionConstants;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramReader_1_ITest extends ChromatogramWriterTestCase {

	@Override
	@Before
	public void setUp() {

		PreferenceSupplier.setImportDelimiter(Delimiter.COMMA);
		PreferenceSupplier.setImportZeroMarker("0.0");
		/*
		 * Import
		 */
		pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1);
		extensionPointImport = VersionConstants.CONVERTER_ID_CHROMATOGRAM;
		/*
		 * Export/Reimport
		 */
		pathExport = TestPathHelper.getAbsolutePath(TestPathHelper.DIRECTORY_EXPORT_TEST) + File.separator + "Test.csv";
		extensionPointExportReimport = "org.eclipse.chemclipse.msd.converter.supplier.csv";
		super.setUp();
	}

	@Test
	public void testReimport_1() {

		assertNotNull(chromatogram);
	}

	@Test
	public void testReimport_2() {

		assertEquals(5726, chromatogram.getNumberOfScans());
	}
}