/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.cml.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.msd.converter.supplier.cml.PathResolver;
import org.eclipse.chemclipse.msd.converter.supplier.cml.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.cml.model.VendorLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class MassSpectrumImportConverterSP03_ITest {

	private IScanMSD massSpectrum;

	@Before
	public void setUp() throws Exception {

		File file = new File(PathResolver.getAbsolutePath(TestPathHelper.TESTFILE_SP03));
		DatabaseImportConverter importConverter = new DatabaseImportConverter();
		IProcessingInfo<IMassSpectra> processingInfo = importConverter.convert(file, new NullProgressMonitor());
		massSpectrum = processingInfo.getProcessingResult().getMassSpectrum(1);
	}

	@Test
	public void testLoading() {

		assertNotNull(massSpectrum);
		assertEquals("sp03", massSpectrum.getIdentifier());
	}

	@Test
	public void testLibraryInformation() {

		VendorLibraryMassSpectrum libraryMassSpectrum = (VendorLibraryMassSpectrum)massSpectrum;
		ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
		assertEquals("D.HENNEBERG, MAX-PLANCK INSTITUTE, MULHEIM, WEST GERMANY", libraryInformation.getContributor());
		assertEquals("109-99-9", libraryInformation.getCasNumber());
		assertEquals("C 4 H 8 O 1", libraryInformation.getFormula());
	}

	@Test
	public void testSignals() {

		assertEquals(32, massSpectrum.getNumberOfIons());
		assertEquals(73d, massSpectrum.getHighestIon().getIon(), 0);
	}
}
