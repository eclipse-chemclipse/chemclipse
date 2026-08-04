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
import static org.junit.jupiter.api.Assertions.assertFalse;
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
 * Two entries with the same name (Toluene).
 * Entry 1 is sparse: no formula, no smiles, no comments, each with one synonym.
 * Entry 2 is complete: fills the gaps and contributes another synonym.
 * Verifies fill-if-empty behaviour and that the shared name is not duplicated
 * into the synonym list.
 */
public class LibrarySupport_02_Test {

	private IRegularLibraryMassSpectrum merged;

	@BeforeEach
	public void setUp() throws Exception {

		RegularLibraryMassSpectrum entry1 = new RegularLibraryMassSpectrum();
		entry1.setRetentionTime(0);
		entry1.addIon(new Ion(91.0d, 1000.0f));
		entry1.addIon(new Ion(92.0d, 600.0f));
		ILibraryInformation info1 = new LibraryInformation();
		info1.setName("Toluene");
		info1.getSynonyms().add("Methylbenzene");
		entry1.setLibraryInformation(info1);

		RegularLibraryMassSpectrum entry2 = new RegularLibraryMassSpectrum();
		entry2.setRetentionTime(240000);
		entry2.addIon(new Ion(91.0d, 800.0f));
		entry2.addIon(new Ion(65.0d, 200.0f));
		ILibraryInformation info2 = new LibraryInformation();
		info2.setName("Toluene");
		info2.setFormula("C7H8");
		info2.setSmiles("Cc1ccccc1");
		info2.setComments("Aromatic solvent");
		info2.setRetentionTime(240000);
		info2.getSynonyms().add("Phenylmethane");
		entry2.setLibraryInformation(info2);

		List<IScanMSD> selected = new ArrayList<>();
		selected.add(entry1);
		selected.add(entry2);
		merged = LibrarySupport.merge(selected);
	}

	@Test
	public void testName() {

		assertEquals("Toluene", merged.getLibraryInformation().getName());
	}

	@Test
	public void testTargetNameNotInSynonyms() {

		assertFalse(merged.getLibraryInformation().getSynonyms().contains("Toluene"));
	}

	@Test
	public void testSynonymFromFirstEntry() {

		assertTrue(merged.getLibraryInformation().getSynonyms().contains("Methylbenzene"));
	}

	@Test
	public void testSynonymFromSecondEntry() {

		assertTrue(merged.getLibraryInformation().getSynonyms().contains("Phenylmethane"));
	}

	@Test
	public void testSynonymCount() {

		assertEquals(2, merged.getLibraryInformation().getSynonyms().size());
	}

	@Test
	public void testFormulaFilledFromSecondEntry() {

		assertEquals("C7H8", merged.getLibraryInformation().getFormula());
	}

	@Test
	public void testSmilesFilledFromSecondEntry() {

		assertEquals("Cc1ccccc1", merged.getLibraryInformation().getSmiles());
	}

	@Test
	public void testCommentsFilledFromSecondEntry() {

		assertEquals("Aromatic solvent", merged.getLibraryInformation().getComments());
	}

	@Test
	public void testRetentionTimeFilledFromSecondEntry() {

		assertEquals(240000, merged.getLibraryInformation().getRetentionTime());
	}

	@Test
	public void testIonCount() {

		assertEquals(3, merged.getNumberOfIons());
	}
}
