/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
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

import java.io.File;

import org.eclipse.chemclipse.msd.identifier.supplier.nist.TestPathHelper;
import org.junit.Before;
import org.junit.Test;

public class NistResultFileParser_6_Test {

	private NistResultFileParser nistResultFileParser;
	private File results;
	private Compounds compounds;
	private Compound compound;
	private Hit hit;

	@Before
	public void setUp() throws Exception {

		nistResultFileParser = new NistResultFileParser();
		results = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_NIST_SRCRESLT_4));
		compounds = nistResultFileParser.getCompounds(results);
		compound = compounds.getCompound(2);
		hit = compound.getHit(2);
	}

	@Test
	public void testGetIdentifier_1() {

		assertEquals("ID-2", compound.getIdentfier());
	}

	@Test
	public void testGetCompoundInLibraryFactor_1() {

		assertEquals("300", compound.getCompoundInLibraryFactor());
	}

	@Test
	public void testGetName_1() {

		assertEquals("Ethane, 1,2-diethoxy-", hit.getName());
	}

	@Test
	public void testGetFormula_1() {

		assertEquals("C6H14O2", hit.getFormula());
	}

	@Test
	public void testGetMF_1() {

		assertEquals(82.1f, hit.getMatchFactor(), 0);
	}

	@Test
	public void testGetRMF_1() {

		assertEquals(82.7f, hit.getReverseMatchFactor(), 0);
	}

	@Test
	public void testGetProb_1() {

		assertEquals(12.82f, hit.getProbability(), 0);
	}

	@Test
	public void testGetCAS_1() {

		assertEquals("629-14-1", hit.getCAS());
	}

	@Test
	public void testGetMw_1() {

		assertEquals(118, hit.getMolecularWeight());
	}

	@Test
	public void testGetLib_1() {

		assertEquals("mainlib", hit.getLib());
	}

	@Test
	public void testGetId_1() {

		assertEquals(32113, hit.getId());
	}

	@Test
	public void testGetRI_1() {

		assertEquals(750, hit.getRetentionIndex());
	}
}
