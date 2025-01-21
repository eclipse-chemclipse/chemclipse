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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.io;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.TestPathHelper;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.preferences.PreferenceSupplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramReader_4_DAD_1501_ITest extends ChromatogramReaderWSDTestCase {

	@Override
	@Before
	public void setUp() throws Exception {

		PreferenceSupplier.setForceLoadAlternateDetector(true);
		pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_4_DAD_1501);
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {

		PreferenceSupplier.setForceLoadAlternateDetector(false);
	}

	@Test
	public void testReader_1() {

		assertEquals(1443, chromatogram.getNumberOfScans());
	}

	@Test
	public void testReader_2() {

		assertEquals("Chromatogram4-1501", chromatogram.getName());
	}

	@Test
	public void testReader_3() {

		assertEquals(510325, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testReader_4() {

		assertEquals(1087125, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testReader_5() {

		assertEquals(3.54757344E8f, chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testReader_6() {

		assertEquals(4.2400872E7f, chromatogram.getMinSignal(), 0);
	}

	@Test
	public void testReader_7() {

		assertEquals(510325, chromatogram.getScanDelay());
	}

	@Test
	public void testReader_8() {

		assertEquals(400, chromatogram.getScanInterval());
	}

	@Test
	public void testReader_9() {

		IScanWSD scan = chromatogram.getSupplierScan(1);
		//
		assertEquals(226, scan.getScanSignals().size());
		assertEquals(5.9459736E7f, scan.getTotalSignal(), 0);
	}

	@Test
	public void testReader_10() {

		assertEquals(4, chromatogram.getPeaks().size());
	}

	@Test
	public void testReader_11() {

		assertEquals(4.057491060101555E10d, chromatogram.getPeakIntegratedArea(), 0);
	}

	@Test
	public void testReader_12() {

		assertEquals(0.0d, chromatogram.getChromatogramIntegratedArea(), 0);
	}

	@Test
	public void testReader_13() {

		assertEquals(0.0d, chromatogram.getBackgroundIntegratedArea(), 0);
	}

	@Test
	public void testReader_14() {

		assertEquals(0.0d, chromatogram.getSampleWeight(), 0);
	}
}
