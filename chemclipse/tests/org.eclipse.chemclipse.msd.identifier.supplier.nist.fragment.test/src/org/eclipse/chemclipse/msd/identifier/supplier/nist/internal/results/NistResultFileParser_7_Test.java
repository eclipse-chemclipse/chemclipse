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

public class NistResultFileParser_7_Test {

	private NistResultFileParser nistResultFileParser;
	private File results;
	private Compounds compounds;
	private Compound compound;
	private Hit hit;

	@Before
	public void setUp() throws Exception {

		nistResultFileParser = new NistResultFileParser();
		results = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_NIST_SRCRESLT_5));
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

		assertEquals("-117", compound.getCompoundInLibraryFactor());
	}

	@Test
	public void testGetName_1() {

		assertEquals("á-D-Allopyranose, 5TMS derivative", hit.getName());
	}

	@Test
	public void testGetFormula_1() {

		assertEquals("C21H52O6Si5", hit.getFormula());
	}

	@Test
	public void testGetMF_1() {

		assertEquals(92.1f, hit.getMatchFactor(), 0);
	}

	@Test
	public void testGetRMF_1() {

		assertEquals(92.2f, hit.getReverseMatchFactor(), 0);
	}

	@Test
	public void testGetProb_1() {

		assertEquals(8.85f, hit.getProbability(), 0);
	}

	@Test
	public void testGetCAS_1() {

		assertEquals("0-00-0", hit.getCAS());
	}

	@Test
	public void testGetMw_1() {

		assertEquals(540, hit.getMolecularWeight());
	}

	@Test
	public void testGetLib_1() {

		assertEquals("mainlib", hit.getLib());
	}

	@Test
	public void testGetId_1() {

		assertEquals(205524, hit.getId());
	}

	@Test
	public void testGetRI_1() {

		assertEquals(1829, hit.getRetentionIndex());
	}
}
