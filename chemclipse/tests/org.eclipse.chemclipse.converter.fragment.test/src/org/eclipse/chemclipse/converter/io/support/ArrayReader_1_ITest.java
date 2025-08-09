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

public class ArrayReader_1_ITest {

	/*
	 * Big Endian
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
	public void testRead1BUShortBE_1() {

		assertEquals(240, arrayReader.read1BUShortBE());
	}

	@Test
	public void testRead1BShortBE_1() {

		assertEquals(-16, arrayReader.read1BShortBE());
	}

	@Test
	public void testRead2BShortBE_1() {

		assertEquals(-3929, arrayReader.read2BShortBE());
	}

	@Test
	public void testRead2BUIntegerBE_1() {

		assertEquals(61607, arrayReader.read2BUIntegerBE());
	}

	@Test
	public void testRead2BIntegerBE_1() {

		assertEquals(-3929, arrayReader.read2BIntegerBE());
	}

	@Test
	public void testRead4BIntegerBE_1() {

		assertEquals(-257441525, arrayReader.read4BIntegerBE());
	}

	@Test
	public void testRead4BULongBE_1() {

		assertEquals(4037525771L, arrayReader.read4BULongBE());
	}

	@Test
	public void testRead4BLongBE_1() {

		assertEquals(-257441525, arrayReader.read4BLongBE());
	}

	@Test
	public void testRead8BULongBE_1() {

		assertEquals(8117669106424938811L, arrayReader.read8BULongBE());
	}

	@Test
	public void testRead8BLongBE_1() {

		assertEquals(-1105702930429836997L, arrayReader.read8BLongBE());
	}

	// ---------------------------------------------------------------
	@Test
	public void testRead8BUDoubleBE_1() {

		assertEquals(4.720464669257224E234, arrayReader.read8BUDoubleBE(), 0);
	}

	@Test
	public void testRead8BDoubleBE_1() {

		assertEquals(-4.720464669257224E234, arrayReader.read8BDoubleBE(), 0);
	}

	// ---------------------------------------------------------------
	@Test
	public void testReadULongBE_1() {

		assertEquals(240, arrayReader.readULongBE(1));
	}

	@Test
	public void testReadLongBE_1() {

		assertEquals(240, arrayReader.readLongBE(1));
	}

	@Test
	public void testReadULongBE_2() {

		assertEquals(61607, arrayReader.readULongBE(2));
	}

	@Test
	public void testReadLongBE_2() {

		assertEquals(61607, arrayReader.readLongBE(2));
	}

	@Test
	public void testReadULongBE_3() {

		assertEquals(4037525771L, arrayReader.readULongBE(4));
	}

	@Test
	public void testReadLongBE_3() {

		assertEquals(4037525771L, arrayReader.readLongBE(4));
	}

	@Test
	public void testReadULongBE_4() {

		assertEquals(8117669106424938811L, arrayReader.readULongBE(8));
	}

	@Test
	public void testReadLongBE_4() {

		assertEquals(-1105702930429836997L, arrayReader.readLongBE(8));
	}

	@Test
	public void testReadULongBE_5() {

		assertEquals(0, arrayReader.readULongBE(-1));
	}

	@Test
	public void testReadLongBE_5() {

		assertEquals(0, arrayReader.readLongBE(-1));
	}

	@Test
	public void testReadULongBE_6() {

		assertEquals(0, arrayReader.readULongBE(0));
	}

	@Test
	public void testReadLongBE_6() {

		assertEquals(0, arrayReader.readLongBE(0));
	}

	@Test
	public void testReadULongBE_7() {

		assertEquals(0, arrayReader.readULongBE(9));
	}

	@Test
	public void testReadLongBE_7() {

		assertEquals(0, arrayReader.readLongBE(9));
	}
	// ---------------------------------------------------------------
}
