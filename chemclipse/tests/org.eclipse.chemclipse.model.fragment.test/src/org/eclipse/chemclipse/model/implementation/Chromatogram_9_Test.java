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
package org.eclipse.chemclipse.model.implementation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Chromatogram_9_Test {

	private Chromatogram chromatogram;

	@Before
	public void setUp() throws Exception {

		chromatogram = new Chromatogram();
		chromatogram.setConverterId("hello.world.converter");
	}

	@Test
	public void test1() {

		assertEquals("hello.world.converter", chromatogram.getConverterId());
	}

	@Test
	public void test2() {

		chromatogram.setFinalized(true);
		assertEquals("", chromatogram.getConverterId());
	}

	@Test
	public void test3() {

		chromatogram.setFinalized(true);
		chromatogram.setFinalized(false);
		assertEquals("hello.world.converter", chromatogram.getConverterId());
	}
}