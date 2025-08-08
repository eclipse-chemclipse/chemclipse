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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzml.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.TestPathHelper;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.versions.VersionConstants;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramImportExport110_ITest {

	protected IChromatogramMSD chromatogramImport;
	protected IChromatogramMSD chromatogram;
	protected String pathImport;
	protected String pathExport;
	protected File fileImport;
	protected File fileExport;
	protected String extensionPointImport;
	protected String extensionPointExportReimport;

	@Before
	public void setUp() {

		/*
		 * Import
		 */
		pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1);
		extensionPointImport = VersionConstants.CONVERTER_ID_CHROMATOGRAM;
		/*
		 * Export/Reimport
		 */
		File directory = new File(TestPathHelper.DIRECTORY_EXPORT_TEST);
		directory.mkdir();
		pathExport = TestPathHelper.getAbsolutePath(TestPathHelper.DIRECTORY_EXPORT_TEST) + File.separator + "Test.mzML";
		extensionPointExportReimport = "org.eclipse.chemclipse.msd.converter.supplier.mzml";
		/*
		 * Import the chromatogram.
		 */
		fileImport = new File(this.pathImport);
		IProcessingInfo<IChromatogramMSD> processingInfoImport = ChromatogramConverterMSD.getInstance().convert(fileImport, this.extensionPointImport, new NullProgressMonitor());
		chromatogramImport = processingInfoImport.getProcessingResult();
		/*
		 * Export the chromatogram.
		 */
		fileExport = new File(this.pathExport);
		IProcessingInfo<File> processingInfoExport = ChromatogramConverterMSD.getInstance().convert(fileExport, chromatogramImport, this.extensionPointExportReimport, new NullProgressMonitor());
		fileExport = processingInfoExport.getProcessingResult();
		/*
		 * Reimport the exported chromatogram.
		 */
		chromatogramImport = null;
		IProcessingInfo<IChromatogramMSD> processingInfo = ChromatogramConverterMSD.getInstance().convert(fileExport, this.extensionPointExportReimport, new NullProgressMonitor());
		chromatogram = processingInfo.getProcessingResult();
	}

	@After
	public void tearDown() throws Exception {

		fileExport.delete();
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
