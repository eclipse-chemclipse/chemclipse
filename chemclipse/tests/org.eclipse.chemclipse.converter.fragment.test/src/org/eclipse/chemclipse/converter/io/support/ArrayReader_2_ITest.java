/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.converter.io.support;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.converter.TestPathHelper;
import org.junit.Before;
import org.junit.Test;

public class ArrayReader_2_ITest {

	/*
	 * Little Endian
	 */
	private IArrayReader arrayReader;

	@Before
	public void setUp() throws Exception {

		/*
		 * IMPORT_BIN_TEST:
		 * F0 A7 C1 0B 04 9F 01 3B
		 * 11110000 10100111 11000001 00001011 00000100 10011111 00000001 00111011
		 */
		File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_BIN_TEST));
		arrayReader = new ArrayReaderTestImplementation(file);
	}

	@Test
	public void testRead1BUShortLE_1() {

		assertEquals(240, arrayReader.read1BUShortLE());
	}

	@Test
	public void testRead1BShortLE_1() {

		assertEquals(-16, arrayReader.read1BShortLE());
	}

	@Test
	public void testRead2BShortLE_1() {

		assertEquals(-22544, arrayReader.read2BShortLE());
	}

	@Test
	public void testRead2BUIntegerLE_1() {

		assertEquals(42992, arrayReader.read2BUIntegerLE());
	}

	@Test
	public void testRead2BIntegerLE_1() {

		assertEquals(-22544, arrayReader.read2BIntegerLE());
	}

	@Test
	public void testRead4BIntegerLE_1() {

		assertEquals(197240816, arrayReader.read4BIntegerLE());
	}

	@Test
	public void testRead4BULongLE_1() {

		assertEquals(197240816, arrayReader.read4BULongLE());
	}

	@Test
	public void testRead4BLongLE_1() {

		assertEquals(197240816, arrayReader.read4BLongLE());
	}

	@Test
	public void testRead8BULongLE_1() {

		assertEquals(4251854362940385264L, arrayReader.read8BULongLE());
	}

	@Test
	public void testRead8BLongLE_1() {

		assertEquals(4251854362940385264L, arrayReader.read8BLongLE());
	}

	// ---------------------------------------------------------------
	@Test
	public void testRead8BUDoubleLE_1() {

		assertEquals(1.8219847735894905E-24, arrayReader.read8BUDoubleLE(), 0);
	}

	@Test
	public void testRead8BDoubleLE_1() {

		assertEquals(1.8219847735894905E-24, arrayReader.read8BDoubleLE(), 0);
	}

	// ---------------------------------------------------------------
	@Test
	public void testReadULongLE_1() {

		assertEquals(240, arrayReader.readULongLE(1));
	}

	@Test
	public void testReadLongLE_1() {

		assertEquals(240, arrayReader.readLongLE(1));
	}

	@Test
	public void testReadULongLE_2() {

		assertEquals(42992, arrayReader.readULongLE(2));
	}

	@Test
	public void testReadLongLE_2() {

		assertEquals(42992, arrayReader.readLongLE(2));
	}

	public void testReadULongLE_3() {

		assertEquals(197240816, arrayReader.readULongLE(4));
	}

	@Test
	public void testReadLongLE_3() {

		assertEquals(197240816, arrayReader.readLongLE(4));
	}

	@Test
	public void testReadULongLE_4() {

		assertEquals(4251854362940385264L, arrayReader.readULongLE(8));
	}

	@Test
	public void testReadLongLE_4() {

		assertEquals(4251854362940385264L, arrayReader.readLongLE(8));
	}

	@Test
	public void testReadULongLE_5() {

		assertEquals(0, arrayReader.readULongLE(-1));
	}

	@Test
	public void testReadLongLE_5() {

		assertEquals(0, arrayReader.readLongLE(-1));
	}

	@Test
	public void testReadULongLE_6() {

		assertEquals(0, arrayReader.readULongLE(0));
	}

	@Test
	public void testReadLongLE_6() {

		assertEquals(0, arrayReader.readLongLE(0));
	}

	@Test
	public void testReadULongLE_7() {

		assertEquals(0, arrayReader.readULongLE(9));
	}

	@Test
	public void testReadLongLE_7() {

		assertEquals(0, arrayReader.readLongLE(9));
	}
	// ---------------------------------------------------------------
}
