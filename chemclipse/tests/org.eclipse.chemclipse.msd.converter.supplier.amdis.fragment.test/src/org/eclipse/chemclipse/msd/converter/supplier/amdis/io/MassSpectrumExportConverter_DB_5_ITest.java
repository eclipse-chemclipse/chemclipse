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
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.io;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.amdis.PathResolver;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

public class MassSpectrumExportConverter_DB_5_ITest extends MassSpectrumExportConverterTestCase {

	@Override
	public void setUp() throws Exception {

		exportFile = new File(PathResolver.getAbsolutePath(TestPathHelper.TESTDIR_EXPORT) + File.separator + TestPathHelper.TESTFILE_EXPORT_DB_MSL_IDENTIFIER);
		importFile = new File(PathResolver.getAbsolutePath(TestPathHelper.TESTDIR_EXPORT) + File.separator + TestPathHelper.TESTFILE_EXPORT_DB_MSL_IDENTIFIER);
		super.setUp();
	}

	@Override
	public void tearDown() throws Exception {

		super.tearDown();
	}

	@Test
	public void testExport_1() {

		IScanMSD ms;
		ms = new ScanMSD();
		for(int j = 1; j <= 6; j++) {
			ms.addIon(new Ion(j, j * 10));
		}
		ms.setIdentifier("ID-1"); // <- IMPORTANT

		exportConverter.convert(exportFile, ms, false, new NullProgressMonitor());

		IProcessingInfo<?> processingInfo = importConverter.convert(importFile, new NullProgressMonitor());
		massSpectra = (IMassSpectra)processingInfo.getProcessingResult();
		assertEquals(1, massSpectra.size());
		IRegularLibraryMassSpectrum massSpectrum = (IRegularLibraryMassSpectrum)massSpectra.getMassSpectrum(1);
		assertEquals("ID-1", massSpectrum.getLibraryInformation().getName());
	}
}
