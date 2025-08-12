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

public class MSPImportConverter_2_ITest {

	private IMassSpectra massSpectra;

	@Before
	public void setUp() {

		File importFile = new File(PathResolver.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_LIB_2_MSP));
		IDatabaseImportConverter importConverter = new MSPDatabaseImportConverter();
		IProcessingInfo<IMassSpectra> processingInfo = importConverter.convert(importFile, new NullProgressMonitor());
		massSpectra = processingInfo.getProcessingResult();
	}

	@Test
	public void test_1() {

		assertEquals(2, massSpectra.size());
	}

	@Test
	public void test_2() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(1);
		ILibraryMassSpectrum libraryMassSpectrum = (ILibraryMassSpectrum)massSpectrum;
		assertEquals(630000, massSpectrum.getRetentionTime());
		assertEquals(1212000, massSpectrum.getRelativeRetentionTime());
		assertEquals(400.7f, massSpectrum.getRetentionIndex(), 0);
		assertEquals("Ethane", libraryMassSpectrum.getLibraryInformation().getName());
		assertEquals("74-84-0", libraryMassSpectrum.getLibraryInformation().getCasNumber());
		assertEquals(14, massSpectrum.getNumberOfIons());
		assertEquals(2.00020e-03f, massSpectrum.getIon(2).getAbundance(), 0);
		assertEquals(4.00040e-03f, massSpectrum.getIon(12).getAbundance(), 0);
		assertEquals(1.00010e-02f, massSpectrum.getIon(13).getAbundance(), 0);
		assertEquals(3.00030e-02f, massSpectrum.getIon(14).getAbundance(), 0);
		assertEquals(4.40044e-02f, massSpectrum.getIon(15).getAbundance(), 0);
		assertEquals(1.00010e-03f, massSpectrum.getIon(16).getAbundance(), 0);
		assertEquals(5.00050e-03f, massSpectrum.getIon(24).getAbundance(), 0);
		assertEquals(3.50035e-02f, massSpectrum.getIon(25).getAbundance(), 0);
		assertEquals(2.32223e-01f, massSpectrum.getIon(26).getAbundance(), 0);
		assertEquals(3.32333e-01f, massSpectrum.getIon(27).getAbundance(), 0);
		assertEquals(1.00000e+00f, massSpectrum.getIon(28).getAbundance(), 0);
		assertEquals(2.15222e-01f, massSpectrum.getIon(29).getAbundance(), 0);
		assertEquals(2.62226e-01f, massSpectrum.getIon(30).getAbundance(), 0);
		assertEquals(5.00050e-03f, massSpectrum.getIon(31).getAbundance(), 0);
	}

	@Test
	public void test_3() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(2);
		ILibraryMassSpectrum libraryMassSpectrum = (ILibraryMassSpectrum)massSpectrum;
		assertEquals(672000, massSpectrum.getRetentionTime());
		assertEquals(1218000, massSpectrum.getRelativeRetentionTime());
		assertEquals(500.5f, massSpectrum.getRetentionIndex(), 0);
		assertEquals("Ethylene", libraryMassSpectrum.getLibraryInformation().getName());
		assertEquals("74-85-1", libraryMassSpectrum.getLibraryInformation().getCasNumber());
		assertEquals(12, massSpectrum.getNumberOfIons());
		assertEquals(1.00010e-03f, massSpectrum.getIon(2).getAbundance(), 0);
		assertEquals(5.00050e-03f, massSpectrum.getIon(12).getAbundance(), 0);
		assertEquals(9.00090e-03f, massSpectrum.getIon(13).getAbundance(), 0);
		assertEquals(2.10021e-02f, massSpectrum.getIon(14).getAbundance(), 0);
		assertEquals(3.00030e-03f, massSpectrum.getIon(15).getAbundance(), 0);
		assertEquals(2.30023e-02f, massSpectrum.getIon(24).getAbundance(), 0);
		assertEquals(7.81078e-02f, massSpectrum.getIon(25).getAbundance(), 0);
		assertEquals(5.29553e-01f, massSpectrum.getIon(26).getAbundance(), 0);
		assertEquals(6.23662e-01f, massSpectrum.getIon(27).getAbundance(), 0);
		assertEquals(1.00000e+00f, massSpectrum.getIon(28).getAbundance(), 0);
		assertEquals(2.30023e-02f, massSpectrum.getIon(29).getAbundance(), 0);
		assertEquals(1.00010e-03f, massSpectrum.getIon(30).getAbundance(), 0);
	}
}
