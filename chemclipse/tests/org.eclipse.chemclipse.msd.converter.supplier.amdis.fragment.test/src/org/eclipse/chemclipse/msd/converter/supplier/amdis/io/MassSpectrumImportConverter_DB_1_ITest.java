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
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.model.IVendorLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.Before;
import org.junit.Test;

public class MassSpectrumImportConverter_DB_1_ITest extends ImportConverterMslTestCase {

	@Override
	@Before
	public void setUp() throws Exception {

		importFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_DB_1));
		super.setUp();
	}

	@Test
	public void testImport_1() {

		assertEquals("MassSpectra", 6, massSpectra.size());
	}

	@Test
	public void testImport_2() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(1);
		IVendorLibraryMassSpectrum ms = null;
		if(massSpectrum instanceof IVendorLibraryMassSpectrum vendorLibraryMassSpectrum) {
			ms = vendorLibraryMassSpectrum;
		}
		assertNotNull("IAmdisMassSpectrum", ms);
		assertEquals("Name", "0.5203 min, OP17760", ms.getLibraryInformation().getName());
		assertEquals("CAS Number", "OP17760-N1001", ms.getLibraryInformation().getCasNumber());
		assertEquals("Comments", "0.5203 min, OP17760", ms.getLibraryInformation().getComments());
		assertEquals("Retention Time", 31218, ms.getRetentionTime());
		assertEquals("Retention Index", 0.0f, ms.getRetentionIndex(), 0);
		assertEquals("Ion", 6, ms.getNumberOfIons());
		assertEquals("Lowest Ion", 16.0d, ms.getLowestIon().getIon(), 0);
		assertEquals("Lowest Ion Abundance", 13.0f, ms.getLowestIon().getAbundance(), 0);
		assertEquals("Highest Abundance Ion", 28.0d, ms.getHighestAbundance().getIon(), 0);
		assertEquals("Highest Abundance", 999.0f, ms.getHighestAbundance().getAbundance(), 0);
	}

	@Test
	public void testImport_3() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(6);
		IVendorLibraryMassSpectrum ms = null;
		if(massSpectrum instanceof IVendorLibraryMassSpectrum vendorLibraryMassSpectrum) {
			ms = vendorLibraryMassSpectrum;
		}
		assertNotNull("IAmdisMassSpectrum", ms);
		assertEquals("Name", "1.5763 min, OP17760", ms.getLibraryInformation().getName());
		assertEquals("CAS Number", "OP17760-N1006", ms.getLibraryInformation().getCasNumber());
		assertEquals("Comments", "1.5763 min, OP17760", ms.getLibraryInformation().getComments());
		assertEquals("Retention Time", 94578, ms.getRetentionTime());
		assertEquals("Retention Index", 0.0f, ms.getRetentionIndex(), 0);
		assertEquals("Ion", 27, ms.getNumberOfIons());
		assertEquals("Lowest Ion", 15.0d, ms.getLowestIon().getIon(), 0);
		assertEquals("Lowest Ion Abundance", 29.0f, ms.getLowestIon().getAbundance(), 0);
		assertEquals("Highest Abundance Ion", 41.0d, ms.getHighestAbundance().getIon(), 0);
		assertEquals("Highest Abundance", 999.0f, ms.getHighestAbundance().getAbundance(), 0);
	}

	@Test
	public void testImport_4() {

		IScanMSD massSpectrum = massSpectra.getMassSpectrum(4);
		IVendorLibraryMassSpectrum ms = null;
		if(massSpectrum instanceof IVendorLibraryMassSpectrum vendorLibraryMassSpectrum) {
			ms = vendorLibraryMassSpectrum;
		}
		assertNotNull("IAmdisMassSpectrum", ms);
		assertEquals("Name", "1.3982 min, OP17760", ms.getLibraryInformation().getName());
		assertEquals("CAS Number", "OP17760-N1004", ms.getLibraryInformation().getCasNumber());
		assertEquals("Comments", "1.3982 min, OP17760", ms.getLibraryInformation().getComments());
		assertEquals("Retention Time", 83892, ms.getRetentionTime());
		assertEquals("Retention Index", 0.0f, ms.getRetentionIndex(), 0);
		assertEquals("Ion", 6, ms.getNumberOfIons());
		assertEquals("Lowest Ion", 16.0d, ms.getLowestIon().getIon(), 0);
		assertEquals("Lowest Ion Abundance", 20.0f, ms.getLowestIon().getAbundance(), 0);
		assertEquals("Highest Abundance Ion", 44.0d, ms.getHighestAbundance().getIon(), 0);
		assertEquals("Highest Abundance", 999.0f, ms.getHighestAbundance().getAbundance(), 0);
	}

	@Test
	public void testImport_5() {

		IScanMSD massSpectrum;
		List<Integer> numberOfIons = new ArrayList<>();
		numberOfIons.add(0); // first is 0, because massSpectra starts
								// with index 1
		numberOfIons.add(6);
		numberOfIons.add(12);
		numberOfIons.add(11);
		numberOfIons.add(6);
		numberOfIons.add(10);
		numberOfIons.add(27);
		for(int i = 1; i <= massSpectra.size(); i++) {
			massSpectrum = massSpectra.getMassSpectrum(i);
			assertEquals("Ions", (int)numberOfIons.get(i), massSpectrum.getNumberOfIons());
		}
	}
}
