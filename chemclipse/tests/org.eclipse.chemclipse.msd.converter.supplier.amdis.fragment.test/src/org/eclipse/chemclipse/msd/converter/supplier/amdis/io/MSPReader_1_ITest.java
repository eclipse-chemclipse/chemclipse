/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.amdis.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.eclipse.chemclipse.msd.model.core.ILibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class MSPReader_1_ITest {

	private MSPReader reader;
	private File file;
	private IMassSpectra massSpectra;

	@Before
	public void setUp() throws Exception {

		reader = new MSPReader();
		String pathname = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_SYNONYMS);
		file = new File(pathname);
		massSpectra = reader.read(file, new NullProgressMonitor());
	}

	@Test
	public void testRead_1() {

		assertNotNull(massSpectra);
	}

	@Test
	public void testRead_2() {

		assertEquals(1, massSpectra.size());
	}

	@Test
	public void testRead_3() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(1);
		assertNotNull(massSpectrum);
	}

	@Test
	public void testRead_4() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(1);
		assertTrue(massSpectrum instanceof ILibraryMassSpectrum);
	}

	@Test
	public void testRead_5() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(1);
		if(massSpectrum instanceof ILibraryMassSpectrum libraryMassSpectrum) {
			Set<String> synonyms = libraryMassSpectrum.getLibraryInformation().getSynonyms();
			assertEquals(6, synonyms.size());
			assertTrue(synonyms.contains("test1"));
			assertTrue(synonyms.contains("test2"));
			assertTrue(synonyms.contains("test4"));
			assertTrue(synonyms.contains("test6"));
			assertTrue(synonyms.contains("UN 500"));
			assertTrue(synonyms.contains("UN 600"));
		}
	}
}
