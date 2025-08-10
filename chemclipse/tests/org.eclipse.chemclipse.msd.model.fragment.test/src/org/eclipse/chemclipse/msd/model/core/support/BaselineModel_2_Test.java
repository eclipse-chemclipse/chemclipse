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

public class BaselineModel_2_Test {

	private IChromatogramMSD chromatogram;
	private IBaselineModel baselineModel;

	@Before
	public void setUp() throws Exception {

		chromatogram = EasyMock.createMock(IChromatogramMSD.class);
		EasyMock.expect(chromatogram.getStartRetentionTime()).andStubReturn(1000);
		EasyMock.expect(chromatogram.getStopRetentionTime()).andStubReturn(100000);
		EasyMock.replay(chromatogram);
		baselineModel = new BaselineModel(chromatogram);
		baselineModel.addBaseline(5000, 50000, 100, 10000, true);
	}

	@Test
	public void testChromatogram_1() {

		assertEquals("StartRetentionTime", 1000, chromatogram.getStartRetentionTime(), 0);
		assertEquals("StopRetentionTime", 100000, chromatogram.getStopRetentionTime(), 0);
	}

	@Test
	public void testBaseline_1() {

		assertEquals("BackgroundAbundance", 100.0f, baselineModel.getBackgroundAbundance(5000), 0);
		assertEquals("BackgroundAbundance", 10000.0f, baselineModel.getBackgroundAbundance(50000), 0);
		assertEquals("BackgroundAbundance", 5600.0f, baselineModel.getBackgroundAbundance(30000), 0);
	}

	@Test
	public void testBaseline_2() {

		baselineModel.removeBaseline();
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(30000), 0);
	}

	@Test
	public void testBaseline_3() {

		/*
		 * Cut an existing segment into two peaces.
		 */
		baselineModel.removeBaseline(25000, 35000);
		assertEquals("BackgroundAbundance", 4499.78f, baselineModel.getBackgroundAbundance(24999), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(25000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(30000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(35000), 0);
		assertEquals("BackgroundAbundance", 6700.22f, baselineModel.getBackgroundAbundance(35001), 0);
	}

	@Test
	public void testBaseline_4() {

		/*
		 * Cut the ending part of an existing segment.
		 */
		baselineModel.removeBaseline(40000, 60000);
		assertEquals("BackgroundAbundance", 7799.78f, baselineModel.getBackgroundAbundance(39999), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(40000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(60000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(100000), 0);
	}

	@Test
	public void testBaseline_5() {

		/*
		 * Cut the beginning part of an existing segment.
		 */
		baselineModel.removeBaseline(1000, 10000);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(1000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(5000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(10000), 0);
		assertEquals("BackgroundAbundance", 1200.22f, baselineModel.getBackgroundAbundance(10001), 0);
	}

	@Test
	public void testBaseline_6() {

		/*
		 * The baseline will be removed after calling
		 * baselineModel.removeBaseline(1000, 10000). But now check that the
		 * segment is right in place.
		 */
		baselineModel.addBaseline(2000, 4000, 500, 500, true);
		assertEquals("BackgroundAbundance", 500.0f, baselineModel.getBackgroundAbundance(2000), 0);
		assertEquals("BackgroundAbundance", 500.0f, baselineModel.getBackgroundAbundance(2500), 0);
		assertEquals("BackgroundAbundance", 500.0f, baselineModel.getBackgroundAbundance(4000), 0);
		/*
		 * The baseline will be removed after calling
		 * baselineModel.removeBaseline(1000, 10000). But now check that the
		 * segment is right in place.
		 */
		baselineModel.addBaseline(4500, 8000, 800, 800, true);
		assertEquals("BackgroundAbundance", 800.0f, baselineModel.getBackgroundAbundance(4500), 0);
		assertEquals("BackgroundAbundance", 800.0f, baselineModel.getBackgroundAbundance(6500), 0);
		assertEquals("BackgroundAbundance", 800.0f, baselineModel.getBackgroundAbundance(8000), 0);
		/*
		 * If the segment is totally hidden by the segment to be inserted,
		 * remove it.
		 */
		baselineModel.removeBaseline(1000, 10000);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(1000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(2000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(2500), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(4000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(4500), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(6500), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(8000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackgroundAbundance(10000), 0);
		assertEquals("BackgroundAbundance", 1200.22f, baselineModel.getBackgroundAbundance(10001), 0);
	}
}
