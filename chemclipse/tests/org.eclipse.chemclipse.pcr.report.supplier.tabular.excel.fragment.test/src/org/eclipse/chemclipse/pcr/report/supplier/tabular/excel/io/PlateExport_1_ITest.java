/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.pcr.report.supplier.tabular.excel.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.eclipse.chemclipse.pcr.converter.core.IPlateExportConverter;
import org.eclipse.chemclipse.pcr.model.core.IPlate;
import org.eclipse.chemclipse.pcr.model.core.Plate;
import org.eclipse.chemclipse.pcr.report.supplier.tabular.excel.PathResolver;
import org.eclipse.chemclipse.pcr.report.supplier.tabular.excel.core.PCRExportConverter;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class PlateExport_1_ITest {

	private static final String DIRECTORY_EXPORT_TEST = "testData/files/export";

	private IPlateExportConverter pcrExportConverter = new PCRExportConverter();

	private File file;

	@BeforeAll
	public void setUp() {

		new File(DIRECTORY_EXPORT_TEST).mkdirs();
		String path = PathResolver.getAbsolutePath(DIRECTORY_EXPORT_TEST);
		file = new File(path + File.separator + "ExcelReport.xlsx");
	}

	@AfterAll
	public void tearDown() {

		file.delete();
	}

	@Test
	public void testExport() {

		IPlate plate = new Plate();
		IProcessingInfo<File> processingInfo = pcrExportConverter.convert(file, plate, new NullProgressMonitor());
		assertTrue(processingInfo.getProcessingResult().length() > 0);
	}
}
