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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ArrayWriter_1_Test {

	private ArrayWriterTestImplementation arrayWriter = new ArrayWriterTestImplementation(new byte[1000]);

	@Test
	public void test1() {

		byte[] bytes = arrayWriter.getBytesStringTerminated(20, "2");
		assertEquals(20, bytes.length);
		assertEquals(1, bytes[0]);
		assertEquals(50, bytes[1]);
	}

	@Test
	public void test2() {

		byte[] bytes = arrayWriter.getBytesStringTerminated(6, "Hello World");
		assertEquals(6, bytes.length);
		assertEquals(5, bytes[0]);
		assertEquals(72, bytes[1]);
		assertEquals(101, bytes[2]);
		assertEquals(108, bytes[3]);
		assertEquals(108, bytes[4]);
		assertEquals(111, bytes[5]);
	}
}
