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
package org.eclipse.chemclipse.converter.io.support;

import org.junit.Test;

import junit.framework.TestCase;

public class ArrayWriter_2_Test extends TestCase {

	private ArrayWriterTestImplementation arrayWriter = new ArrayWriterTestImplementation(new byte[1000]);

	@Test
	public void test1() {

		byte[] bytes = arrayWriter.getBytesStringNullTerminated(11, "Hello World");
		assertEquals(22, bytes.length);
	}

	@Test
	public void test2() {

		byte[] bytes = arrayWriter.getBytesStringNullTerminated(5, "Hello World");
		assertEquals(10, bytes.length);
	}

	@Test
	public void test3() {

		byte[] bytes = arrayWriter.getBytesStringNullTerminated(5, "Hello World");
		assertEquals(10, bytes.length);
		assertEquals(72, bytes[0]);
		assertEquals(0, bytes[1]);
		assertEquals(101, bytes[2]);
		assertEquals(0, bytes[3]);
		assertEquals(108, bytes[4]);
		assertEquals(0, bytes[5]);
		assertEquals(108, bytes[6]);
		assertEquals(0, bytes[7]);
		assertEquals(111, bytes[8]);
		assertEquals(0, bytes[9]);
	}
}
