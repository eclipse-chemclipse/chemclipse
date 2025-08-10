/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.misc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConverterMOL_Test {

	@Test
	public void test1() {

		assertEquals("19906720", ConverterMOL.extractCASNumber(" CAS rn = 19906720, "));
	}

	@Test
	public void test2() {

		assertEquals("19906-72-0", ConverterMOL.extractCASNumber(" CAS rn = 19906-72-0, "));
	}

	@Test
	public void test3() {

		assertEquals("", ConverterMOL.extractCASNumber(""));
	}

	@Test
	public void test4() {

		assertEquals("", ConverterMOL.extractCASNumber(null));
	}

	@Test
	public void test5() {

		assertEquals("", ConverterMOL.extractCASNumber(" CAS rn = ABC, "));
	}
}