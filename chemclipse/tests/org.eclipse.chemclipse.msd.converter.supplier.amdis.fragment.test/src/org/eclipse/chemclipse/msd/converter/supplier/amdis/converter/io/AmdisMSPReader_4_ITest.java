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
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.io.MSPReader;
import org.eclipse.chemclipse.msd.model.core.ILibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class AmdisMSPReader_4_ITest {

	private IMassSpectra massSpectra;

	@Before
	public void setUp() throws Exception {

		File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_PEAKS_4_MSP));
		MSPReader reader = new MSPReader();
		massSpectra = reader.read(file, new NullProgressMonitor());
	}

	@Test
	public void test1() {

		assertEquals(3, massSpectra.size());
	}

	@Test
	public void test2() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(1);
		if(massSpectrum instanceof ILibraryMassSpectrum libraryMassSpectrum) {
			ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
			assertEquals("Demo1", libraryInformation.getName());
			assertEquals("CHO", libraryInformation.getFormula());
			assertEquals(0.0d, libraryInformation.getMolWeight(), 0);
			assertEquals("", libraryInformation.getCasNumber());
			assertEquals("Comment1", libraryInformation.getComments());
		} else {
			/*
			 * It must be a library mass spectrum.
			 */
			assertTrue(false);
		}
		assertEquals(131, massSpectrum.getNumberOfIons());
		assertEquals(41.0d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(4804.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(362.0d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(300.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(360.0d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(9999.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(0, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertEquals(144811.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void test3() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(2);
		if(massSpectrum instanceof ILibraryMassSpectrum libraryMassSpectrum) {
			ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
			assertEquals("Demo2", libraryInformation.getName());
			assertEquals("COH", libraryInformation.getFormula());
			assertEquals(0.0d, libraryInformation.getMolWeight(), 0);
			assertEquals("", libraryInformation.getCasNumber());
			assertEquals("Comment2", libraryInformation.getComments());
		} else {
			/*
			 * It must be a library mass spectrum.
			 */
			assertTrue(false);
		}
		assertEquals(170, massSpectrum.getNumberOfIons());
		assertEquals(24.0d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(100.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(306.0d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(20.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(82.0d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(9999.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(0, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertEquals(84929.0f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void test4() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(3);
		if(massSpectrum instanceof ILibraryMassSpectrum libraryMassSpectrum) {
			ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
			assertEquals("Demo3", libraryInformation.getName());
			assertEquals("OHC", libraryInformation.getFormula());
			assertEquals(0.0d, libraryInformation.getMolWeight(), 0);
			assertEquals("", libraryInformation.getCasNumber());
			assertEquals("Comment3", libraryInformation.getComments());
		} else {
			/*
			 * It must be a library mass spectrum.
			 */
			assertTrue(false);
		}
		assertEquals(23, massSpectrum.getNumberOfIons());
		assertEquals(37.0d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(210.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(180.0d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(260.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(120.0d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(9999.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(0, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertEquals(29859.0f, massSpectrum.getTotalSignal(), 0);
	}
}
