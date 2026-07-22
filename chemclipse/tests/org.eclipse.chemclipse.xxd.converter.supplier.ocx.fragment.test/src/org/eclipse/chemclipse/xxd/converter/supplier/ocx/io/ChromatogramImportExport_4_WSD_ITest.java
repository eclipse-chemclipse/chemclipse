/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.eclipse.chemclipse.converter.chromatogram.IChromatogramConverter;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.IProcessingMessage;
import org.eclipse.chemclipse.wsd.converter.chromatogram.ChromatogramConverterWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramPeakWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.versions.VersionConstants;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ChromatogramImportExport_4_WSD_ITest {

	private IChromatogramWSD chromatogram;
	private IChromatogramWSD chromatogramImport;
	private File fileExport;

	@BeforeAll
	public void setUp() {

		String extensionPoint = VersionConstants.CONVERTER_ID_CHROMATOGRAM;
		IChromatogramConverter<IChromatogramPeakWSD, IChromatogramWSD> converter = ChromatogramConverterWSD.getInstance();
		String chromatogramFileName = "Chromatogram4-1501.ocb";
		/*
		 * Import
		 */
		File fileImport = new File("testData/files/import/" + chromatogramFileName);
		IProcessingInfo<IChromatogramWSD> processingInfoImport = converter.convert(fileImport, extensionPoint, new NullProgressMonitor());
		for(IProcessingMessage message : processingInfoImport.getMessages()) {
			System.out.println(message.getMessage());
		}
		chromatogramImport = processingInfoImport.getProcessingResult();
		/*
		 * Export
		 */
		File exportDirectory = new File("testData/files/export");
		exportDirectory.mkdir();
		fileExport = new File(exportDirectory.getAbsolutePath() + File.separator + chromatogramFileName);
		IProcessingInfo<File> processingInfoExport = converter.convert(fileExport, chromatogramImport, extensionPoint, new NullProgressMonitor());
		for(IProcessingMessage message : processingInfoExport.getMessages()) {
			System.out.println(message.getMessage());
		}
		fileExport = processingInfoExport.getProcessingResult();
		/*
		 * Reimport
		 */
		IProcessingInfo<IChromatogramWSD> processingInfo = converter.convert(fileExport, extensionPoint, new NullProgressMonitor());
		for(IProcessingMessage message : processingInfo.getMessages()) {
			System.out.println(message.getMessage());
		}
		chromatogram = processingInfo.getProcessingResult();
	}

	@AfterAll
	public void tearDown() {

		fileExport.delete();
	}

	@Test
	public void testImport() {

		assertNotNull(chromatogramImport);
	}

	@Test
	public void testExport() {

		assertTrue(fileExport.exists());
	}

	@Test
	public void testReimport() {

		assertNotNull(chromatogram);
		assertEquals(1443, chromatogram.getNumberOfScans());
		assertEquals(510325, chromatogram.getStartRetentionTime());
		assertEquals(1087125, chromatogram.getStopRetentionTime());
		assertFalse(chromatogram.getWavelengths().isEmpty());
	}
}