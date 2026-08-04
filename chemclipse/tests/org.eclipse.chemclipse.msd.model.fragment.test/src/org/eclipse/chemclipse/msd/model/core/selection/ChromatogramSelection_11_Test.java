/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.util.MarkedTracesSupportMSD;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ChromatogramSelection_11_Test {

	private IChromatogramSelectionMSD chromatogramSelection;

	@BeforeAll
	public void setUp() {

		/*
		 * Use createNiceMock if you use void methods that are not important to
		 * test.
		 */
		IChromatogramMSD chromatogram = EasyMock.createNiceMock(IChromatogramMSD.class);
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

		List<ITrace> selectedIons = chromatogramSelection.getSelectedIons();
		assertNotNull(selectedIons);
	}

	@Test
	public void testGetSelectedIons_2() {

		List<ITrace> selectedIons;
		selectedIons = chromatogramSelection.getSelectedIons();
		selectedIons.add(new TraceNominalMSD(43));
		selectedIons.add(new TraceNominalMSD(104));
		selectedIons = chromatogramSelection.getSelectedIons();
		assertNotNull(selectedIons);
		Set<Integer> selectedIonsNominal = MarkedTracesSupportMSD.getTracesAsInteger(selectedIons);
		assertTrue(selectedIonsNominal.contains(43));
		assertTrue(selectedIonsNominal.contains(104));
		assertFalse(selectedIonsNominal.contains(42));
		assertFalse(selectedIonsNominal.contains(105));
	}

	@Test
	public void testGetExcludedIons_1() {

		List<ITrace> excludedIons = chromatogramSelection.getExcludedIons();
		assertNotNull(excludedIons);
	}

	@Test
	public void testGetExcludedIons_2() {

		List<ITrace> excludedIons;
		excludedIons = chromatogramSelection.getExcludedIons();
		excludedIons.add(new TraceNominalMSD(18));
		excludedIons.add(new TraceNominalMSD(28));
		excludedIons = chromatogramSelection.getExcludedIons();
		assertNotNull(excludedIons);
		Set<Integer> excludedIonsNominal = MarkedTracesSupportMSD.getTracesAsInteger(excludedIons);
		assertFalse(excludedIonsNominal.contains(43));
		assertFalse(excludedIonsNominal.contains(104));
		assertTrue(excludedIonsNominal.contains(18));
		assertTrue(excludedIonsNominal.contains(28));
	}
}
