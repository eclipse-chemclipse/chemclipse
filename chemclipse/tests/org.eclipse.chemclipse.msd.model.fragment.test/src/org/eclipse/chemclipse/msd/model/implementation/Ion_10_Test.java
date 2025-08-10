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

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * HashCode test.
 */
public class Ion_10_Test {

	private Ion ion1;
	private Ion ion2;

	@Before
	public void setUp() throws Exception {

		ion1 = new Ion(5.2f, 4746.3f);
	}

	@Test
	public void testConstructor_1() {

		ion2 = new Ion(ion1);
		assertTrue("hashCode", ion1.equals(ion2));
	}

	@Test
	public void testConstructor_2() {

		assertThrows(IllegalArgumentException.class, () -> ion2 = new Ion(null));
	}
}
