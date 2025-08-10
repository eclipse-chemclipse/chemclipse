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

import org.junit.Before;
import org.junit.Test;

/**
 * Abundance and ion test.
 */
public class Ion_2_Test {

	private Ion ion;

	@Before
	public void setUp() throws Exception {

		ion = new Ion(2.2f, 4527.3f);
	}

	@Test
	public void testGetAbundance() {

		assertEquals("getAbundance", 4527.3f, ion.getAbundance(), 0);
	}

	@Test
	public void testGetIon() {

		assertEquals("getIon", 2.200000047683716d, ion.getIon(), 0);
	}
}
