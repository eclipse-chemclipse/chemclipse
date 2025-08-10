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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.junit.Before;
import org.junit.Test;

public class Chromatogram_19_Test {

	private IChromatogramMSD chromatogram;
	private IIdentificationTarget entry1;
	private IIdentificationTarget entry2;
	private ILibraryInformation libraryInformation;
	private IComparisonResult comparisonResult;

	@Before
	public void setUp() throws Exception {

		chromatogram = new ChromatogramMSD();
		libraryInformation = new LibraryInformation();
		comparisonResult = new ComparisonResult(0.8f, 0.95f, 0.0f, 0.0f);
		entry1 = new IdentificationTarget(libraryInformation, comparisonResult);
		entry1.setIdentifier("Test-Tools");
		entry2 = new IdentificationTarget(libraryInformation, comparisonResult);
		entry2.setIdentifier("DB-Tools");
	}

	@Test
	public void testGetTargets_1() {

		Set<IIdentificationTarget> targets = chromatogram.getTargets();
		assertNotNull("Targets", targets);
	}

	@Test
	public void testGetTargets_2() {

		Set<IIdentificationTarget> targets = chromatogram.getTargets();
		assertEquals("Size", 0, targets.size());
	}

	@Test
	public void testTargets_1() {

		chromatogram.getTargets().add(entry1);
		chromatogram.getTargets().add(entry2);
		Set<IIdentificationTarget> targets = chromatogram.getTargets();
		assertEquals("Size", 2, targets.size());
	}

	@Test
	public void testTargets_2() {

		chromatogram.getTargets().add(entry1);
		chromatogram.getTargets().add(entry2);
		chromatogram.getTargets().add(entry1);
		Set<IIdentificationTarget> targets = chromatogram.getTargets();
		assertEquals("Size", 2, targets.size());
	}

	@Test
	public void testTargets_3() {

		chromatogram.getTargets().add(entry1);
		chromatogram.getTargets().add(entry2);
		chromatogram.getTargets().add(entry1);
		chromatogram.getTargets().remove(entry1);
		Set<IIdentificationTarget> targets = chromatogram.getTargets();
		assertEquals("Size", 1, targets.size());
	}

	@Test
	public void testTargets_4() {

		chromatogram.getTargets().add(entry1);
		chromatogram.getTargets().add(entry2);
		chromatogram.getTargets().clear();
		Set<IIdentificationTarget> targets = chromatogram.getTargets();
		assertEquals("Size", 0, targets.size());
	}
}
