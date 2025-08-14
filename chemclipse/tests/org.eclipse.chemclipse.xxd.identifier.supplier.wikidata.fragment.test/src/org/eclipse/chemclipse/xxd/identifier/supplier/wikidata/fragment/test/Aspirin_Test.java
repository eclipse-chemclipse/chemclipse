/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.identifier.supplier.wikidata.fragment.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.xxd.identifier.supplier.wikidata.query.QueryStructuralFormula;
import org.junit.Test;

public class Aspirin_Test {

	@Test
	public void testName() {

		String url = QueryStructuralFormula.fromName("acetylsalicylic acid");
		assertEquals("http://commons.wikimedia.org/wiki/Special:FilePath/Acetylsalicyls%C3%A4ure2.svg", url);
	}

	@Test
	public void testCAS() {

		String url = QueryStructuralFormula.fromCAS("50-78-2");
		assertEquals("http://commons.wikimedia.org/wiki/Special:FilePath/Acetylsalicyls%C3%A4ure2.svg", url);
	}

	@Test
	public void testSMILES() {

		String url = QueryStructuralFormula.fromSMILES("CC(=O)OC1=CC=CC=C1C(=O)O");
		assertEquals("http://commons.wikimedia.org/wiki/Special:FilePath/Acetylsalicyls%C3%A4ure2.svg", url);
	}

	@Test
	public void testInChI() {

		String url = QueryStructuralFormula.fromInChI("InChI=1S/C9H8O4/c1-6(10)13-8-5-3-2-4-7(8)9(11)12/h2-5H,1H3,(H,11,12)");
		assertEquals("http://commons.wikimedia.org/wiki/Special:FilePath/Acetylsalicyls%C3%A4ure2.svg", url);
	}

	@Test
	public void testInChIKey() {

		String url = QueryStructuralFormula.fromInChIKey("BSYNRYMUTXBXSQ-UHFFFAOYSA-N");
		assertEquals("http://commons.wikimedia.org/wiki/Special:FilePath/Acetylsalicyls%C3%A4ure2.svg", url);
	}
}
