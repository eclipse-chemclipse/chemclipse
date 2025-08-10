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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the methods equals and hashCode.
 * 
 * @author Philip Wenig
 */
public class Chromatogram_1_Test {

	private ChromatogramMSD chromatogram1;
	private ChromatogramMSD chromatogram2;

	@Before
	public void setUp() throws Exception {

		chromatogram1 = new ChromatogramMSD();
		chromatogram2 = new ChromatogramMSD();
	}

	@Test
	public void testEquals_1() {

		assertEquals(chromatogram1, chromatogram2);
	}

	@Test
	public void testEquals_2() {

		assertEquals(chromatogram2, chromatogram1);
	}

	@Test
	public void testEquals_3() {

		assertNotNull(chromatogram1);
	}

	@Test
	public void testEquals_4() {

		assertNotNull(chromatogram2);
	}

	@Test
	public void testEquals_5() {

		assertNotEquals(chromatogram1, new Object());
	}

	@Test
	public void testEquals_6() {

		assertNotEquals(chromatogram2, new Object());
	}

	@Test
	public void testHashCode_1() {

		assertEquals(chromatogram1.hashCode(), chromatogram2.hashCode());
	}

	@Test
	public void testHashCode_2() {

		assertEquals(chromatogram2.hashCode(), chromatogram1.hashCode());
	}
}
