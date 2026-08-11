/*******************************************************************************
 * Copyright (c) 2011, 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.stream.Stream;

import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.versions.VersionConstants;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ms.numpress.MSNumpress;

@TestInstance(Lifecycle.PER_CLASS)
public class ChromatogramImportExport110_ITest {

	private static Stream<String> numpressPreferences() {

		return Stream.of("", MSNumpress.ACC_NUMPRESS_LINEAR, MSNumpress.ACC_NUMPRESS_PIC, MSNumpress.ACC_NUMPRESS_SLOF);
	}

	private IChromatogramMSD chromatogramImport;
	private File fileExport;

	@BeforeAll
	public void setUp() {

		String extensionPointImport = VersionConstants.CONVERTER_ID_CHROMATOGRAM;
		File fileImport = new File("testData/files/import/Chromatogram1.ocb");
		IProcessingInfo<IChromatogramMSD> processingInfoImport = ChromatogramConverterMSD.getInstance().convert(fileImport, extensionPointImport, new NullProgressMonitor());
		chromatogramImport = processingInfoImport.getProcessingResult();
	}

	@ParameterizedTest
	@MethodSource("numpressPreferences")
	public void testReimport(String numpressPreference) {

		File directory = new File("testData/files/export");
		directory.mkdir();
		String extensionPointExportReimport = "org.eclipse.chemclipse.msd.converter.supplier.mzml";
		/*
		 * Export the chromatogram.
		 */
		PreferenceSupplier.setSaveNumpress(numpressPreference);
		fileExport = new File("testData/files/export" + File.separator + "Test-" + numpressPreference + ".mzML");
		IProcessingInfo<File> processingInfoExport = ChromatogramConverterMSD.getInstance().convert(fileExport, chromatogramImport, extensionPointExportReimport, new NullProgressMonitor());
		fileExport = processingInfoExport.getProcessingResult();
		/*
		 * Reimport the exported chromatogram.
		 */
		IProcessingInfo<IChromatogramMSD> processingInfo = ChromatogramConverterMSD.getInstance().convert(fileExport, extensionPointExportReimport, new NullProgressMonitor());
		IChromatogramMSD chromatogram = processingInfo.getProcessingResult();

		assertNotNull(chromatogram);
		assertEquals(5726, chromatogram.getNumberOfScans());
	}

	@AfterEach
	public void tearDown() {

		fileExport.delete();
	}
}