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
 * Three entries with distinct names and distinct CAS numbers.
 * Verifies that the name from the first entry is preserved, the names of the
 * second and third entries are added as synonyms, and all three CAS numbers
 * are collected without duplication.
 * Also confirms that ions from all three entries contribute to the merged
 * spectrum.
 */
public class LibrarySupport_06_Test {

	private IRegularLibraryMassSpectrum merged;

	@BeforeEach
	public void setUp() throws Exception {

		RegularLibraryMassSpectrum entry1 = new RegularLibraryMassSpectrum();
		entry1.setRetentionTime(100000);
		entry1.addIon(new Ion(55.0d, 1000.0f));
		ILibraryInformation info1 = new LibraryInformation();
		info1.setName("Compound A");
		info1.setCasNumber("111-11-1");
		entry1.setLibraryInformation(info1);

		RegularLibraryMassSpectrum entry2 = new RegularLibraryMassSpectrum();
		entry2.setRetentionTime(105000);
		entry2.addIon(new Ion(57.0d, 900.0f));
		ILibraryInformation info2 = new LibraryInformation();
		info2.setName("Compound B");
		info2.setCasNumber("222-22-2");
		entry2.setLibraryInformation(info2);

		RegularLibraryMassSpectrum entry3 = new RegularLibraryMassSpectrum();
		entry3.setRetentionTime(110000);
		entry3.addIon(new Ion(59.0d, 800.0f));
		ILibraryInformation info3 = new LibraryInformation();
		info3.setName("Compound C");
		info3.setCasNumber("333-33-3");
		entry3.setLibraryInformation(info3);

		List<IScanMSD> selected = new ArrayList<>();
		selected.add(entry1);
		selected.add(entry2);
		selected.add(entry3);
		merged = LibrarySupport.merge(selected);
	}

	@Test
	public void testNameFromFirstEntry() {

		assertEquals("Compound A", merged.getLibraryInformation().getName());
	}

	@Test
	public void testSynonymFromSecondEntry() {

		assertTrue(merged.getLibraryInformation().getSynonyms().contains("Compound B"));
	}

	@Test
	public void testSynonymFromThirdEntry() {

		assertTrue(merged.getLibraryInformation().getSynonyms().contains("Compound C"));
	}

	@Test
	public void testSynonymCount() {

		assertEquals(2, merged.getLibraryInformation().getSynonyms().size());
	}

	@Test
	public void testCasNumberCount() {

		assertEquals(3, merged.getLibraryInformation().getCasNumbers().size());
	}

	@Test
	public void testCasNumberFromFirstEntry() {

		assertTrue(merged.getLibraryInformation().getCasNumbers().contains("111-11-1"));
	}

	@Test
	public void testCasNumberFromSecondEntry() {

		assertTrue(merged.getLibraryInformation().getCasNumbers().contains("222-22-2"));
	}

	@Test
	public void testCasNumberFromThirdEntry() {

		assertTrue(merged.getLibraryInformation().getCasNumbers().contains("333-33-3"));
	}

	@Test
	public void testRetentionTimeFromFirstEntry() {

		assertEquals(100000, merged.getRetentionTime());
	}

	@Test
	public void testIonCount() {

		assertEquals(3, merged.getNumberOfIons());
	}
}
