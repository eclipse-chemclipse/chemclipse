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
package org.eclipse.chemclipse.msd.model.core;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AbstractIon_2_Test {

	private double exactIon;

	@Before
	public void setUp() throws Exception {

		exactIon = 28.78749204f;
	}

	@Test
	public void testGetIon_1() {

		double ion = AbstractIon.getIon(exactIon, 1);
		assertEquals("Ion", 28.8d, ion, 0);
	}

	@Test
	public void testGetIon_2() {

		double ion = AbstractIon.getIon(exactIon, 2);
		assertEquals("Ion", 28.79d, ion, 0);
	}

	@Test
	public void testGetIon_3() {

		double ion = AbstractIon.getIon(exactIon, 3);
		assertEquals("Ion", 28.787d, ion, 0);
	}

	@Test
	public void testGetIon_4() {

		double ion = AbstractIon.getIon(exactIon, 4);
		assertEquals("Ion", 28.7875d, ion, 0);
	}

	@Test
	public void testGetIon_5() {

		double ion = AbstractIon.getIon(exactIon, 5);
		assertEquals("Ion", 28.78749d, ion, 0);
	}

	@Test
	public void testGetIon_6() {

		double ion = AbstractIon.getIon(exactIon, 6);
		assertEquals("Ion", 28.787493d, ion, 0);
	}

	@Test
	public void testGetIon_7() {

		double ion = AbstractIon.getIon(exactIon, 0);
		assertEquals("Ion", 28.8d, ion, 0);
	}

	@Test
	public void testGetIon_8() {

		double ion = AbstractIon.getIon(exactIon, -1);
		assertEquals("Ion", 28.8d, ion, 0);
	}

	@Test
	public void testGetIon_9() {

		double ion = AbstractIon.getIon(exactIon, 7);
		assertEquals("Ion", 28.8d, ion, 0);
	}
}
