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
package org.eclipse.chemclipse.msd.model.core.selection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.easymock.EasyMock;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIon;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramSelection_11_Test {

	private IChromatogramMSD chromatogram;
	private IChromatogramSelectionMSD chromatogramSelection;

	@Before
	public void setUp() throws Exception {

		/*
		 * Use createNiceMock if you use void methods that are not important to
		 * test.
		 */
		chromatogram = EasyMock.createNiceMock(IChromatogramMSD.class);
		EasyMock.expect(chromatogram.getStartRetentionTime()).andStubReturn(1);
		EasyMock.expect(chromatogram.getStopRetentionTime()).andStubReturn(100);
		EasyMock.expect(chromatogram.getMaxSignal()).andStubReturn(127500.0f);
		EasyMock.replay(chromatogram);
		/*
		 * Default values from IChromatogram will be chosen.
		 */
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Test
	public void testGetSelectedIons_1() {

		IMarkedIons selectedIons = chromatogramSelection.getSelectedIons();
		assertNotNull(selectedIons);
	}

	@Test
	public void testGetSelectedIons_2() {

		IMarkedIons selectedIons;
		selectedIons = chromatogramSelection.getSelectedIons();
		selectedIons.add(new MarkedIon(43));
		selectedIons.add(new MarkedIon(104));
		selectedIons = chromatogramSelection.getSelectedIons();
		assertNotNull(selectedIons);
		Set<Integer> selectedIonsNominal = selectedIons.getIonsNominal();
		assertEquals("Contains", true, selectedIonsNominal.contains(43));
		assertEquals("Contains", true, selectedIonsNominal.contains(104));
		assertEquals("Contains", false, selectedIonsNominal.contains(42));
		assertEquals("Contains", false, selectedIonsNominal.contains(105));
	}

	@Test
	public void testGetExcludedIons_1() {

		IMarkedIons excludedIons = chromatogramSelection.getExcludedIons();
		assertNotNull(excludedIons);
	}

	@Test
	public void testGetExcludedIons_2() {

		IMarkedIons excludedIons;
		excludedIons = chromatogramSelection.getExcludedIons();
		excludedIons.add(new MarkedIon(18));
		excludedIons.add(new MarkedIon(28));
		excludedIons = chromatogramSelection.getExcludedIons();
		assertNotNull(excludedIons);
		Set<Integer> excludedIonsNominal = excludedIons.getIonsNominal();
		assertEquals("Contains", false, excludedIonsNominal.contains(43));
		assertEquals("Contains", false, excludedIonsNominal.contains(104));
		assertEquals("Contains", true, excludedIonsNominal.contains(18));
		assertEquals("Contains", true, excludedIonsNominal.contains(28));
	}
}
