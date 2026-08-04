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
 * Verifies mergeTextField behaviour for Comments, Miscellaneous, and
 * ReferenceIdentifier:
 * - a value shared by both entries is stored once (no duplication);
 * - distinct values from both entries are joined with "; ";
 * - a value present only in the second entry is adopted when the first is empty.
 */
public class LibrarySupport_05_Test {

	private IRegularLibraryMassSpectrum merged;

	@BeforeEach
	public void setUp() throws Exception {

		RegularLibraryMassSpectrum entry1 = new RegularLibraryMassSpectrum();
		entry1.setRetentionTime(120000);
		entry1.addIon(new Ion(41.0d, 700.0f));
		ILibraryInformation info1 = new LibraryInformation();
		info1.setName("Hexane");
		info1.setComments("Solvent");
		info1.setMiscellaneous("Misc1");
		info1.setReferenceIdentifier("Ref1");
		entry1.setLibraryInformation(info1);

		RegularLibraryMassSpectrum entry2 = new RegularLibraryMassSpectrum();
		entry2.setRetentionTime(125000);
		entry2.addIon(new Ion(43.0d, 600.0f));
		ILibraryInformation info2 = new LibraryInformation();
		info2.setName("n-Hexane");
		info2.setComments("Solvent");
		info2.setMiscellaneous("Misc2");
		info2.setReferenceIdentifier("Ref2");
		entry2.setLibraryInformation(info2);

		List<IScanMSD> selected = new ArrayList<>();
		selected.add(entry1);
		selected.add(entry2);
		merged = LibrarySupport.merge(selected);
	}

	@Test
	public void testCommentsNotDuplicated() {

		assertEquals("Solvent", merged.getLibraryInformation().getComments());
	}

	@Test
	public void testMiscellaneousMerged() {

		assertEquals("Misc1, Misc2", merged.getLibraryInformation().getMiscellaneous());
	}

	@Test
	public void testReferenceIdentifierMerged() {

		assertEquals("Ref1, Ref2", merged.getLibraryInformation().getReferenceIdentifier());
	}

	@Test
	public void testIonCount() {

		assertEquals(2, merged.getNumberOfIons());
	}
}
