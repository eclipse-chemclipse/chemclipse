/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.msp;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.database.IDatabaseImportConverter;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.PathResolver;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class MSPImportConverter_4_ITest {

	private IMassSpectra massSpectra;

	@Before
	public void setUp() {

		File importFile = new File(PathResolver.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_LIB_4_MSP));
		IDatabaseImportConverter importConverter = new MSPDatabaseImportConverter();
		IProcessingInfo<IMassSpectra> processingInfo = importConverter.convert(importFile, new NullProgressMonitor());
		massSpectra = processingInfo.getProcessingResult();
	}

	@Test
	public void test_1() {

		assertEquals(1, massSpectra.size());
	}

	@Test
	public void test_2() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(1);
		assertEquals(5000, massSpectrum.getNumberOfIons());
	}
}
