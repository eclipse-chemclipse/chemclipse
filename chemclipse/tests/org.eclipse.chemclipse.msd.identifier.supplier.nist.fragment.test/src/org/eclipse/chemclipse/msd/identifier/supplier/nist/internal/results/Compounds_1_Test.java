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

public class Compounds_1_Test {

	private Compounds compounds;
	private Compound compound;
	private Hit hit;

	@Before
	public void setUp() throws Exception {

		compounds = new Compounds();
		compound = new Compound();
		hit = new Hit();
		hit.setName("Styrene");
		compound.add(hit);
		compounds.add(compound);
	}

	@Test
	public void testSize_1() {

		assertEquals(1, compounds.size());
	}

	@Test
	public void testSize_2() {

		compound = new Compound();
		hit = new Hit();
		hit.setName("Styrene");
		compound.add(hit);
		compounds.remove(compound);
		assertEquals(0, compounds.size());
	}

	@Test
	public void testSize_3() {

		compound = new Compound();
		hit = new Hit();
		hit.setName("Styrene");
		compound.add(hit);
		compounds.add(compound);
		assertEquals(2, compounds.size());
	}
}
