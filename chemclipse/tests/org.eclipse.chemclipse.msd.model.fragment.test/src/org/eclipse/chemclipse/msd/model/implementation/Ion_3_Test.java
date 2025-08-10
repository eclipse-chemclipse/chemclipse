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
 * Equals test.
 * 
 * @author Philip Wenig
 */
public class Ion_3_Test {

	private Ion ion1;
	private Ion ion2;

	@Before
	public void setUp() throws Exception {

		ion1 = new Ion(2.2f, 4527.3f);
		ion2 = new Ion(2.2f, 4527.3f);
	}

	@Test
	public void testEquals_1() {

		assertEquals("equals", ion1, ion2);
	}

	@Test
	public void testEquals_2() {

		assertEquals("equals", ion2, ion1);
	}

	@Test
	public void testEquals_3() {

		assertEquals("equals", ion1, ion1);
	}

	@Test
	public void testEquals_4() {

		assertNotNull("equals", ion1);
	}

	@Test
	public void testEquals_5() {

		assertNotEquals("equals", ion2, new Object());
	}

	@Test
	public void testCompareTo_1() {

		assertEquals("equals", 0, ion1.compareTo(ion2));
	}

	@Test
	public void testCompareTo_2() {

		assertEquals("equals", 0, ion2.compareTo(ion1));
	}
}
