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
package org.eclipse.chemclipse.msd.model.core.support;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.eclipse.chemclipse.model.baseline.BaselineModel;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.junit.Before;
import org.junit.Test;

public class BaselineModel_4_Test {

	private IChromatogramMSD chromatogram;
	private IBaselineModel baselineModel;

	@Before
	public void setUp() throws Exception {

		chromatogram = EasyMock.createMock(IChromatogramMSD.class);
		EasyMock.expect(chromatogram.getStartRetentionTime()).andStubReturn(1000);
		EasyMock.expect(chromatogram.getStopRetentionTime()).andStubReturn(100000);
		EasyMock.replay(chromatogram);
		baselineModel = new BaselineModel(chromatogram);
		baselineModel.addBaseline(1500, 50000, 100, 100, true);
		baselineModel.addBaseline(1501, 49999, 200, 200, true);
	}

	@Test
	public void testChromatogram_1() {

		assertEquals("StartRetentionTime", 1000, chromatogram.getStartRetentionTime());
		assertEquals("StopRetentionTime", 100000, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testBaseline_1() {

		assertEquals("BackgroundAbundance", 100.0f, baselineModel.getBackgroundAbundance(1500), 0);
		assertEquals("BackgroundAbundance", 200.0f, baselineModel.getBackgroundAbundance(1501), 0);
		assertEquals("BackgroundAbundance", 200.0f, baselineModel.getBackgroundAbundance(49999), 0);
		assertEquals("BackgroundAbundance", 100.0f, baselineModel.getBackgroundAbundance(50000), 0);
	}
}
