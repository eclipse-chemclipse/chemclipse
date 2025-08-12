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
import org.eclipse.chemclipse.msd.model.core.ILibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class MSPImportConverter_3_ITest {

	private IMassSpectra massSpectra;

	@Before
	public void setUp() {

		File importFile = new File(PathResolver.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_LIB_3_MSP));
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
		ILibraryMassSpectrum libraryMassSpectrum = (ILibraryMassSpectrum)massSpectrum;
		assertEquals(649080, massSpectrum.getRetentionTime());
		assertEquals(0, massSpectrum.getRelativeRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertEquals("+EI Scan (rt: 10.818 min)", libraryMassSpectrum.getLibraryInformation().getName());
		assertEquals("", libraryMassSpectrum.getLibraryInformation().getCasNumber());
		assertEquals("365", libraryMassSpectrum.getLibraryInformation().getReferenceIdentifier());
		assertEquals("Lib3", libraryMassSpectrum.getLibraryInformation().getDatabase());
		assertEquals(65, massSpectrum.getNumberOfIons());
		assertEquals(0.80f, massSpectrum.getIon(50.0156d).getAbundance(), 0);
		assertEquals(0.07f, massSpectrum.getIon(50.0785d).getAbundance(), 0);
		assertEquals(0.04f, massSpectrum.getIon(55.2418d).getAbundance(), 0);
		assertEquals(5.66f, massSpectrum.getTotalSignal(), 0.01d);
	}
}
