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
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.io;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.xxd.converter.supplier.ocx.TestPathHelper;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.preferences.PreferenceSupplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramReader_1_FID_1006_ITest extends ChromatogramReaderFIDTestCase {

	@Override
	@Before
	public void setUp() throws Exception {

		PreferenceSupplier.setForceLoadAlternateDetector(true);
		pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1_MSD_1006);
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {

		PreferenceSupplier.setForceLoadAlternateDetector(false);
	}

	@Test
	public void testReader_1() {

		assertEquals(110, chromatogram.getNumberOfScans());
	}

	@Test
	public void testReader_2() {

		assertEquals("Chromatogram1-1006-fromMSD", chromatogram.getName());
	}

	@Test
	public void testReader_3() {

		assertEquals(841111, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testReader_4() {

		assertEquals(918652, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testReader_5() {

		assertEquals(442733.0f, chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testReader_6() {

		assertEquals(21543.0f, chromatogram.getMinSignal(), 0);
	}

	@Test
	public void testReader_7() {

		assertEquals(841111, chromatogram.getScanDelay());
	}

	@Test
	public void testReader_8() {

		assertEquals(710, chromatogram.getScanInterval());
	}
}
