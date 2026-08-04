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
 * Single entry — verifies that all first-entry-block fields are copied
 * faithfully to the merged result (identity case, no subsequent entries).
 * Also covers InChI, InChIKey, ExactMass, Miscellaneous, ReferenceIdentifier,
 * Database, Contributor, CompoundClass, MoleculeStructure, RetentionIndex,
 * and synonyms carried from the source entry.
 */
public class LibrarySupport_03_Test {

	private IRegularLibraryMassSpectrum merged;

	@BeforeEach
	public void setUp() throws Exception {

		RegularLibraryMassSpectrum entry = new RegularLibraryMassSpectrum();
		entry.setRetentionTime(180000);
		entry.addIon(new Ion(78.0d, 999.0f));
		entry.addIon(new Ion(51.0d, 450.0f));
		ILibraryInformation info = new LibraryInformation();
		info.setName("Benzene");
		info.setCasNumber("71-43-2");
		info.setFormula("C6H6");
		info.setSmiles("c1ccccc1");
		info.setInChI("InChI=1S/C6H6/c1-2-4-6-5-3-1/h1-6H");
		info.setInChIKey("UHOVQNZJYSORNB-UHFFFAOYSA-N");
		info.setMolWeight(78.11);
		info.setExactMass(78.047);
		info.setComments("Aromatic compound");
		info.setMiscellaneous("Flammable");
		info.setReferenceIdentifier("NIST-78");
		info.setDatabase("NIST");
		info.setContributor("Lab A");
		info.setCompoundClass("Aromatic hydrocarbon");
		info.setMoleculeStructure("ring");
		info.setRetentionTime(180000);
		info.setRetentionIndex(1000.0f);
		info.getSynonyms().add("Benzol");
		entry.setLibraryInformation(info);
		List<IScanMSD> selected = new ArrayList<>();
		selected.add(entry);
		merged = LibrarySupport.merge(selected);
	}

	@Test
	public void testName() {

		assertEquals("Benzene", merged.getLibraryInformation().getName());
	}

	@Test
	public void testCasNumber() {

		assertEquals("71-43-2", merged.getLibraryInformation().getCasNumber());
	}

	@Test
	public void testFormula() {

		assertEquals("C6H6", merged.getLibraryInformation().getFormula());
	}

	@Test
	public void testSmiles() {

		assertEquals("c1ccccc1", merged.getLibraryInformation().getSmiles());
	}

	@Test
	public void testInChI() {

		assertEquals("InChI=1S/C6H6/c1-2-4-6-5-3-1/h1-6H", merged.getLibraryInformation().getInChI());
	}

	@Test
	public void testInChIKey() {

		assertEquals("UHOVQNZJYSORNB-UHFFFAOYSA-N", merged.getLibraryInformation().getInChIKey());
	}

	@Test
	public void testMolWeight() {

		assertEquals(78.11, merged.getLibraryInformation().getMolWeight(), 0.001);
	}

	@Test
	public void testExactMass() {

		assertEquals(78.047, merged.getLibraryInformation().getExactMass(), 0.001);
	}

	@Test
	public void testComments() {

		assertEquals("Aromatic compound", merged.getLibraryInformation().getComments());
	}

	@Test
	public void testMiscellaneous() {

		assertEquals("Flammable", merged.getLibraryInformation().getMiscellaneous());
	}

	@Test
	public void testReferenceIdentifier() {

		assertEquals("NIST-78", merged.getLibraryInformation().getReferenceIdentifier());
	}

	@Test
	public void testDatabase() {

		assertEquals("NIST", merged.getLibraryInformation().getDatabase());
	}

	@Test
	public void testContributor() {

		assertEquals("Lab A", merged.getLibraryInformation().getContributor());
	}

	@Test
	public void testCompoundClass() {

		assertEquals("Aromatic hydrocarbon", merged.getLibraryInformation().getCompoundClass());
	}

	@Test
	public void testMoleculeStructure() {

		assertEquals("ring", merged.getLibraryInformation().getMoleculeStructure());
	}

	@Test
	public void testRetentionTimeLibraryInfo() {

		assertEquals(180000, merged.getLibraryInformation().getRetentionTime());
	}

	@Test
	public void testRetentionTimeScanLevel() {

		assertEquals(180000, merged.getRetentionTime());
	}

	@Test
	public void testRetentionIndex() {

		assertEquals(1000.0f, merged.getLibraryInformation().getRetentionIndex(), 0.01f);
	}

	@Test
	public void testSynonymFromSourceEntry() {

		assertTrue(merged.getLibraryInformation().getSynonyms().contains("Benzol"));
	}

	@Test
	public void testTargetNameNotInSynonyms() {

		assertFalse(merged.getLibraryInformation().getSynonyms().contains("Benzene"));
	}

	@Test
	public void testIonCount() {

		assertEquals(2, merged.getNumberOfIons());
	}
}
