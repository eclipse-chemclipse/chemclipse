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

import org.easymock.EasyMock;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramSelection_5_Test {

	private IChromatogramMSD chromatogram;
	private IChromatogramSelectionMSD selection;

	@Before
	public void setUp() throws Exception {

		/*
		 * Use createNiceMock if you use void methods that are not important to
		 * test.
		 */
		chromatogram = EasyMock.createNiceMock(IChromatogramMSD.class);
		EasyMock.expect(chromatogram.getStartRetentionTime()).andStubReturn(4500);
		EasyMock.expect(chromatogram.getStopRetentionTime()).andStubReturn(25000);
		EasyMock.expect(chromatogram.getMaxSignal()).andStubReturn(560000.0f);
		EasyMock.replay(chromatogram);
		/*
		 * Default values from IChromatogram will be chosen.
		 */
		selection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Test
	public void testChromatogram_1() {

		assertEquals("StartRetentionTime", 4500, chromatogram.getStartRetentionTime());
		assertEquals("StopRetentionTime", 25000, chromatogram.getStopRetentionTime());
		assertEquals("MaxSignal", 560000.0f, chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testGetChromatogram_1() {

		assertNotNull(selection.getChromatogram());
	}

	@Test
	public void testGetStartRetentionTime_1() {

		selection.setStartRetentionTime(25001);
		selection.reset();
		assertEquals("StartRetentionTime", 4500, selection.getStartRetentionTime());
	}

	@Test
	public void testGetStopRetentionTime_1() {

		selection.setStopRetentionTime(4499);
		selection.reset();
		assertEquals("StopRetentionTime", 25000, selection.getStopRetentionTime());
	}

	@Test
	public void testGetStartAbundance_1() {

		selection.setStartAbundance(560000.1f);
		selection.reset();
		assertEquals("StartAbundance", 0.0f, selection.getStartAbundance(), 0);
	}

	@Test
	public void testGetStopAbundance_1() {

		selection.setStopAbundance(-0.1f);
		selection.reset();
		assertEquals("StopAbundance", 560000.0f, selection.getStopAbundance(), 0);
	}
}
