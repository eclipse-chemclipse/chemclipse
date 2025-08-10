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
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.junit.Before;
import org.junit.Test;

/**
 * The chromatogram and peak will be initialized in DefaultPeakTestCase.<br/>
 * The peak has 15 scans, starting at a retention time of 1500 milliseconds (ms)
 * and ends at a retention time of 15500 ms.<br/>
 * The chromatogram has 17 scans, starting at a retention time of 500 ms and
 * ends at a retention time of 16500 ms. It has a background of 1750 units.
 */
public class ChromatogramPeak_5_Test extends ChromatogramPeakTestCase {

	private IChromatogramPeakMSD peak;
	private IIdentificationTarget entry1;
	private IIdentificationTarget entry2;
	private ILibraryInformation libraryInformation;
	private IComparisonResult comparisonResult;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		peak = new ChromatogramPeakMSD(getPeakModel(), getChromatogram());
		libraryInformation = new LibraryInformation();
		comparisonResult = new ComparisonResult(0.8f, 0.95f, 0.0f, 0.0f);
		entry1 = new IdentificationTarget(libraryInformation, comparisonResult);
		entry1.setIdentifier("PBM");
		entry2 = new IdentificationTarget(libraryInformation, comparisonResult);
		entry2.setIdentifier("INCOS");
	}

	@Test
	public void testGetTargets_1() {

		Set<IIdentificationTarget> targets = peak.getTargets();
		assertNotNull("Targets", targets);
	}

	@Test
	public void testGetTargets_2() {

		Set<IIdentificationTarget> targets = peak.getTargets();
		assertEquals("Size", 0, targets.size());
	}

	@Test
	public void testTargets_1() {

		peak.getTargets().add(entry1);
		peak.getTargets().add(entry2);
		Set<IIdentificationTarget> targets = peak.getTargets();
		assertEquals("Size", 2, targets.size());
	}

	@Test
	public void testTargets_2() {

		peak.getTargets().add(entry1);
		peak.getTargets().add(entry2);
		peak.getTargets().add(entry1);
		Set<IIdentificationTarget> targets = peak.getTargets();
		assertEquals("Size", 2, targets.size());
	}

	@Test
	public void testTargets_3() {

		peak.getTargets().add(entry1);
		peak.getTargets().add(entry2);
		peak.getTargets().add(entry1);
		peak.getTargets().remove(entry1);
		Set<IIdentificationTarget> targets = peak.getTargets();
		assertEquals("Size", 1, targets.size());
	}

	@Test
	public void testTargets_4() {

		peak.getTargets().add(entry1);
		peak.getTargets().add(entry2);
		peak.getTargets().clear();
		Set<IIdentificationTarget> targets = peak.getTargets();
		assertEquals("Size", 0, targets.size());
	}
}
