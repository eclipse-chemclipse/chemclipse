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

import org.junit.Test;

public class Hit_1_Test {

	private Hit hit = new Hit();

	@Test
	public void testGetName_1() {

		assertEquals("", hit.getName());
	}

	@Test
	public void testGetFormula_1() {

		assertEquals("", hit.getFormula());
	}

	@Test
	public void testGetMF_1() {

		assertEquals(0.0f, hit.getMatchFactor(), 0);
	}

	@Test
	public void testGetRMF_1() {

		assertEquals(0.0f, hit.getReverseMatchFactor(), 0);
	}

	@Test
	public void testGetProb_1() {

		assertEquals(0.0f, hit.getProbability(), 0);
	}

	@Test
	public void testGetCAS_1() {

		assertEquals("", hit.getCAS());
	}

	@Test
	public void testGetMw_1() {

		assertEquals(0, hit.getMolecularWeight());
	}

	@Test
	public void testGetLib_1() {

		assertEquals("", hit.getLib());
	}

	@Test
	public void testGetId_1() {

		assertEquals(0, hit.getId());
	}

	@Test
	public void testGetRi_1() {

		assertEquals(0, hit.getRetentionIndex());
	}
}
