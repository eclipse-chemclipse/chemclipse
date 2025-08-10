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

import java.io.File;

import org.eclipse.chemclipse.msd.identifier.supplier.nist.TestPathHelper;
import org.junit.Before;
import org.junit.Test;

public class NistResultFileParser_4_Test {

	private NistResultFileParser nistResultFileParser;
	private File results;
	private Compounds compounds;
	private Compound compound;
	private Hit hit;

	@Before
	public void setUp() throws Exception {

		nistResultFileParser = new NistResultFileParser();
		results = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_NIST_SRCRESLT_3));
		compounds = nistResultFileParser.getCompounds(results);
		compound = compounds.getCompound(1);
		hit = compound.getHit(3);
	}

	@Test
	public void testGetIdentifier_1() {

		assertEquals("PEAK-I-NAME", compound.getIdentfier());
	}

	@Test
	public void testGetCompoundInLibraryFactor_1() {

		assertEquals("-158", compound.getCompoundInLibraryFactor());
	}

	@Test
	public void testGetName_1() {

		assertEquals("1-Pentanol, 3-methyl-", hit.getName());
	}

	@Test
	public void testGetFormula_1() {

		assertEquals("C6H14O", hit.getFormula());
	}

	@Test
	public void testGetMF_1() {

		assertEquals(75.0f, hit.getMatchFactor(), 0);
	}

	@Test
	public void testGetRMF_1() {

		assertEquals(75.9f, hit.getReverseMatchFactor(), 0);
	}

	@Test
	public void testGetProb_1() {

		assertEquals(3.89f, hit.getProbability(), 0);
	}

	@Test
	public void testGetCAS_1() {

		assertEquals("589-35-5", hit.getCAS());
	}

	@Test
	public void testGetMw_1() {

		assertEquals(102, hit.getMolecularWeight());
	}

	@Test
	public void testGetLib_1() {

		assertEquals("mainlib", hit.getLib());
	}

	@Test
	public void testGetId_1() {

		assertEquals(430, hit.getId());
	}
}
