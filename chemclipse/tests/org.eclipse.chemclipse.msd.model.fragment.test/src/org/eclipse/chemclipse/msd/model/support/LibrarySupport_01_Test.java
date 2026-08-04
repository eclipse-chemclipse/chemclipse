/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.RegularLibraryMassSpectrum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Two entries sharing the same CAS number but different names.
 * Entry 1 (Acetone) carries full library data.
 * Entry 2 (2-Propanone) leaves formula empty and adds a second comment.
 */
public class LibrarySupport_01_Test {

	private IRegularLibraryMassSpectrum merged;

	@BeforeEach
	public void setUp() throws Exception {

		RegularLibraryMassSpectrum entry1 = new RegularLibraryMassSpectrum();
		entry1.setRetentionTime(300000);
		entry1.addIon(new Ion(43.0d, 1000.0f));
		entry1.addIon(new Ion(58.0d, 500.0f));
		ILibraryInformation info1 = new LibraryInformation();
		info1.setName("Acetone");
		info1.setCasNumber("67-64-1");
		info1.setFormula("C3H6O");
		info1.setMolWeight(58.08);
		info1.setComments("Solvent");
		info1.setRetentionTime(300000);
		entry1.setLibraryInformation(info1);

		RegularLibraryMassSpectrum entry2 = new RegularLibraryMassSpectrum();
		entry2.setRetentionTime(310000);
		entry2.addIon(new Ion(43.0d, 800.0f));
		entry2.addIon(new Ion(15.0d, 300.0f));
		ILibraryInformation info2 = new LibraryInformation();
		info2.setName("2-Propanone");
		info2.setCasNumber("67-64-2"); // Wrong CAS but for demo
		info2.setComments("Common solvent");
		info2.setRetentionTime(310000);
		entry2.setLibraryInformation(info2);

		List<IScanMSD> selected = new ArrayList<>();
		selected.add(entry1);
		selected.add(entry2);
		merged = LibrarySupport.merge(selected);
	}

	@Test
	public void testNotNull() {

		assertNotNull(merged);
	}

	@Test
	public void testName() {

		assertEquals("Acetone", merged.getLibraryInformation().getName());
	}

	@Test
	public void testSynonymAdded() {

		assertTrue(merged.getLibraryInformation().getSynonyms().contains("2-Propanone"));
	}

	@Test
	public void testCasNumberCount() {

		assertEquals(2, merged.getLibraryInformation().getCasNumbers().size());
	}

	@Test
	public void testCasNumber() {

		assertEquals("67-64-1", merged.getLibraryInformation().getCasNumber());
		merged.getLibraryInformation().getCasNumbers().contains("67-64-2");
	}

	@Test
	public void testFormulaFromFirstEntry() {

		assertEquals("C3H6O", merged.getLibraryInformation().getFormula());
	}

	@Test
	public void testMolWeight() {

		assertEquals(58.08, merged.getLibraryInformation().getMolWeight(), 0.001);
	}

	@Test
	public void testCommentsMerged() {

		assertEquals("Solvent, Common solvent", merged.getLibraryInformation().getComments());
	}

	@Test
	public void testRetentionTimeFromFirstEntry() {

		assertEquals(300000, merged.getRetentionTime());
	}

	@Test
	public void testIonCount() {

		assertEquals(3, merged.getNumberOfIons());
	}
}
