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
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.io;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.TestPathHelper;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramReader_3_FID_1002_ITest extends ChromatogramReaderFIDTestCase {

	@Override
	@Before
	public void setUp() throws Exception {

		pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_3_FID_1002);
		super.setUp();
	}

	@Test
	public void testReader_1() {

		assertEquals(1549, chromatogram.getNumberOfScans());
	}

	@Test
	public void testReader_2() {

		assertEquals("Chromatogram3-1002", chromatogram.getName());
	}

	@Test
	public void testReader_3() {

		assertEquals(7, chromatogram.getPeaks().size());
	}

	@Test
	public void testReader_4() {

		assertEquals(2330900, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testReader_5() {

		assertEquals(2408300, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testReader_6() {

		assertEquals(137045.0f, chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testReader_7() {

		assertEquals(11251.0f, chromatogram.getMinSignal(), 0);
	}

	@Test
	public void testReader_8() {

		assertEquals(2330900, chromatogram.getScanDelay());
	}

	@Test
	public void testReader_9() {

		assertEquals(50, chromatogram.getScanInterval());
	}

	@Test
	public void testReader_10() {

		assertEquals(4.0808584E7f, chromatogram.getTotalSignal(), 0);
	}

	@Test
	public void testReader_11() {

		IScan scan = chromatogram.getScan(1);
		assertEquals(2330900, scan.getRetentionTime());
		assertEquals(0.0f, scan.getRetentionIndex(), 0);
		assertEquals(11463.0f, scan.getTotalSignal(), 0);
	}

	@Test
	public void testReader_12() {

		IScan scan = chromatogram.getScan(593);
		assertEquals(2360500, scan.getRetentionTime());
		assertEquals(0.0f, scan.getRetentionIndex(), 0);
		assertEquals(27412.0f, scan.getTotalSignal(), 0);
	}

	@Test
	public void testReader_13() {

		IScan scan = chromatogram.getScan(1549);
		assertEquals(2408300, scan.getRetentionTime());
		assertEquals(0.0f, scan.getRetentionIndex(), 0);
		assertEquals(16266.0f, scan.getTotalSignal(), 0);
	}
}
