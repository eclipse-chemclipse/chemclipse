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
import java.util.Set;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.io.MSPReader;
import org.eclipse.chemclipse.msd.model.core.ILibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class AmdisMSPReader_5_ITest {

	private IMassSpectra massSpectra;

	@Before
	public void setUp() throws Exception {

		File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_PEAKS_5_MSP));
		MSPReader reader = new MSPReader();
		massSpectra = reader.read(file, new NullProgressMonitor());
	}

	@Test
	public void test1() {

		assertEquals(1, massSpectra.size());
	}

	@Test
	public void test2() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(1);
		if(massSpectrum instanceof ILibraryMassSpectrum libraryMassSpectrum) {
			ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
			assertEquals("Demo1", libraryInformation.getName());
			assertEquals("N2", libraryInformation.getFormula());
			assertEquals(100.0d, libraryInformation.getMolWeight(), 0);
			assertEquals("222-111-3333", libraryInformation.getCasNumber());
			assertEquals("Comment1", libraryInformation.getComments());
			Set<String> synonyms = libraryInformation.getSynonyms();
			assertEquals(3, synonyms.size());
			assertTrue(synonyms.contains("Synonym1"));
			assertTrue(synonyms.contains("Synonym2"));
			assertTrue(synonyms.contains("Synonym3"));
		} else {
			/*
			 * It must be a library mass spectrum.
			 */
			assertTrue(false);
		}
		assertEquals(2, massSpectrum.getNumberOfIons());
		assertEquals(1.0d, massSpectrum.getLowestIon().getIon(), 0);
		assertEquals(21.0f, massSpectrum.getLowestIon().getAbundance(), 0);
		assertEquals(2.0d, massSpectrum.getHighestIon().getIon(), 0);
		assertEquals(999.0f, massSpectrum.getHighestIon().getAbundance(), 0);
		assertEquals(2.0d, massSpectrum.getHighestAbundance().getIon(), 0);
		assertEquals(999.0f, massSpectrum.getHighestAbundance().getAbundance(), 0);
		assertEquals(0, massSpectrum.getRetentionTime());
		assertEquals(0.0f, massSpectrum.getRetentionIndex(), 0);
		assertEquals(1020.0f, massSpectrum.getTotalSignal(), 0);
	}
}
