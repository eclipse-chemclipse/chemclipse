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
 * Fill-if-empty for fields not covered by other test classes:
 * InChI, InChIKey, MolWeight (zero triggers fill), ExactMass (zero triggers fill),
 * Database, Contributor, CompoundClass, MoleculeStructure, and RetentionIndex
 * (zero triggers fill from second entry).
 * Entry 1 has only a name; all other fields are left at their defaults.
 * Entry 2 provides values for every field that entry 1 left empty.
 */
public class LibrarySupport_04_Test {

	private IRegularLibraryMassSpectrum merged;

	@BeforeEach
	public void setUp() throws Exception {

		RegularLibraryMassSpectrum entry1 = new RegularLibraryMassSpectrum();
		entry1.setRetentionTime(0);
		entry1.addIon(new Ion(94.0d, 500.0f));
		ILibraryInformation info1 = new LibraryInformation();
		info1.setName("Phenol");
		entry1.setLibraryInformation(info1);

		RegularLibraryMassSpectrum entry2 = new RegularLibraryMassSpectrum();
		entry2.setRetentionTime(240000);
		entry2.addIon(new Ion(66.0d, 300.0f));
		ILibraryInformation info2 = new LibraryInformation();
		info2.setName("Phenol");
		info2.setInChI("InChI=1S/C6H6O/c7-6-4-2-1-3-5-6/h1-5,7H");
		info2.setInChIKey("ISWSIDIOOBJBQZ-UHFFFAOYSA-N");
		info2.setMolWeight(94.11);
		info2.setExactMass(94.042);
		info2.setDatabase("PubChem");
		info2.setContributor("Lab B");
		info2.setCompoundClass("Phenolic");
		info2.setMoleculeStructure("hydroxybenzene");
		info2.setRetentionTime(240000);
		info2.setRetentionIndex(1050.0f);
		entry2.setLibraryInformation(info2);

		List<IScanMSD> selected = new ArrayList<>();
		selected.add(entry1);
		selected.add(entry2);
		merged = LibrarySupport.merge(selected);
	}

	@Test
	public void testInChIFilledFromSecondEntry() {

		assertEquals("InChI=1S/C6H6O/c7-6-4-2-1-3-5-6/h1-5,7H", merged.getLibraryInformation().getInChI());
	}

	@Test
	public void testInChIKeyFilledFromSecondEntry() {

		assertEquals("ISWSIDIOOBJBQZ-UHFFFAOYSA-N", merged.getLibraryInformation().getInChIKey());
	}

	@Test
	public void testMolWeightFilledFromSecondEntry() {

		assertEquals(94.11, merged.getLibraryInformation().getMolWeight(), 0.001);
	}

	@Test
	public void testExactMassFilledFromSecondEntry() {

		assertEquals(94.042, merged.getLibraryInformation().getExactMass(), 0.001);
	}

	@Test
	public void testDatabaseFilledFromSecondEntry() {

		assertEquals("PubChem", merged.getLibraryInformation().getDatabase());
	}

	@Test
	public void testContributorFilledFromSecondEntry() {

		assertEquals("Lab B", merged.getLibraryInformation().getContributor());
	}

	@Test
	public void testCompoundClassFilledFromSecondEntry() {

		assertEquals("Phenolic", merged.getLibraryInformation().getCompoundClass());
	}

	@Test
	public void testMoleculeStructureFilledFromSecondEntry() {

		assertEquals("hydroxybenzene", merged.getLibraryInformation().getMoleculeStructure());
	}

	@Test
	public void testRetentionIndexFilledFromSecondEntry() {

		assertEquals(1050.0f, merged.getLibraryInformation().getRetentionIndex(), 0.01f);
	}

	@Test
	public void testRetentionTimeFilledFromSecondEntry() {

		assertEquals(240000, merged.getLibraryInformation().getRetentionTime());
	}

	@Test
	public void testIonCount() {

		assertEquals(2, merged.getNumberOfIons());
	}
}
