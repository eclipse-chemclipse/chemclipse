/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.msp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.io.ImportConverterMspTestCase;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.junit.Before;
import org.junit.Test;

public class MassBankMS2ImportConverter_ITest extends ImportConverterMspTestCase {

	@Override
	@Before
	public void setUp() throws Exception {

		importFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_MASSBANK_TEST_MSP));
		super.setUp();
	}

	@Test
	public void testMassSpectra() {

		assertNotNull(massSpectra);
		assertEquals(1, massSpectra.size());
	}

	@Test
	public void testMassSpectrum() {

		IRegularLibraryMassSpectrum regularLibraryMassSpectrum = (IRegularLibraryMassSpectrum)massSpectra.getMassSpectrum(1);
		assertNotNull(regularLibraryMassSpectrum);
		assertEquals((short)2, regularLibraryMassSpectrum.getMassSpectrometer());
		assertEquals(288.1225d, regularLibraryMassSpectrum.getPrecursorIon(), 0);
		assertEquals(287.11575d, regularLibraryMassSpectrum.getNeutralMass(), 0);
		assertEquals("30(NCE)", regularLibraryMassSpectrum.getProperty(IRegularLibraryMassSpectrum.PROPERTY_COLLISION_ENERGY));
		assertEquals(Polarity.POSITIVE, regularLibraryMassSpectrum.getPolarity());
		assertEquals("[M+H]+", regularLibraryMassSpectrum.getProperty(IRegularLibraryMassSpectrum.PROPERTY_PRECURSOR_TYPE));
		assertEquals("LC-ESI-ITFT", regularLibraryMassSpectrum.getProperty(IRegularLibraryMassSpectrum.PROPERTY_INSTRUMENT_TYPE));
		assertEquals("Q-Exactive Orbitrap Thermo Scientific", regularLibraryMassSpectrum.getProperty(IRegularLibraryMassSpectrum.PROPERTY_INSTRUMENT_NAME));
	}

	@Test
	public void testLibraryInformation() {

		IRegularLibraryMassSpectrum massSpectrum = (IRegularLibraryMassSpectrum)massSpectra.getMassSpectrum(1);
		ILibraryInformation libraryInformation = massSpectrum.getLibraryInformation();
		assertNotNull(libraryInformation);
		assertEquals("Pyrophen", libraryInformation.getName());
		assertEquals("InChI=1S/C16H17NO4/c1-11(18)17-14(8-12-6-4-3-5-7-12)15-9-13(20-2)10-16(19)21-15/h3-7,9-10,14H,8H2,1-2H3,(H,17,18)/t14-/m0/s1", libraryInformation.getInChI());
		assertEquals("VFMQMACUYWGDOJ-AWEZNQCLSA-N", libraryInformation.getInChIKey());
		assertEquals("CC(=O)N[C@@H](CC1=CC=CC=C1)C2=CC(=CC(=O)O2)OC", libraryInformation.getSmiles());
		assertEquals("N-[(1S)-1-(4-methoxy-6-oxopyran-2-yl)-2-phenylethyl]acetamide", libraryInformation.getSynonyms().iterator().next());
		assertEquals("MSBNK-AAFC-AC000854", libraryInformation.getReferenceIdentifier());
		assertEquals("MassBank", libraryInformation.getDatabase());
	}
}
