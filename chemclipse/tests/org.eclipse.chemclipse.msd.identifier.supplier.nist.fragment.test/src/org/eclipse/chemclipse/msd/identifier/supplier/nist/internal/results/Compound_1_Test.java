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
package org.eclipse.chemclipse.msd.identifier.supplier.nist.internal.results;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Compound_1_Test {

	private Compound compound;
	private Hit hit;

	@Before
	public void setUp() throws Exception {

		compound = new Compound();
		hit = new Hit();
		hit.setName("Styrene");
		compound.add(hit);
	}

	@Test
	public void testSize_1() {

		assertEquals(1, compound.size());
	}

	@Test
	public void testSize_2() {

		hit = new Hit();
		hit.setName("Styrene");
		compound.remove(hit);
		assertEquals(0, compound.size());
	}

	@Test
	public void testSize_3() {

		hit = new Hit();
		hit.setName("Styrene");
		compound.add(hit);
		assertEquals(2, compound.size());
	}
}
