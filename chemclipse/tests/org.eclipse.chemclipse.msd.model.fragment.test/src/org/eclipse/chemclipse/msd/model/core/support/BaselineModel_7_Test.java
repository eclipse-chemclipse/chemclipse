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
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.junit.Before;
import org.junit.Test;

public class BaselineModel_7_Test {

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

		assertEquals("StartRetentionTime", 1000, chromatogram.getStartRetentionTime());
		assertEquals("StopRetentionTime", 100000, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testBaseline_1() {

		assertEquals("BackgroundAbundance", 100.0f, baselineModel.getBackground(5000), 0);
		assertEquals("BackgroundAbundance", 10000.0f, baselineModel.getBackground(50000), 0);
		assertEquals("BackgroundAbundance", 5600.0f, baselineModel.getBackground(30000), 0);
	}

	@Test
	public void testBaseline_2() {

		baselineModel.removeBaseline();
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(30000), 0);
	}

	@Test
	public void testBaseline_3() {

		/*
		 * Cut an existing segment into two peaces.
		 */
		baselineModel.removeBaseline(25000, 35000);
		assertEquals("BackgroundAbundance", 4499.78f, baselineModel.getBackground(24999), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(25000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(30000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(35000), 0);
		assertEquals("BackgroundAbundance", 6700.22f, baselineModel.getBackground(35001), 0);
	}

	@Test
	public void testBaseline_4() {

		/*
		 * Cut the ending part of an existing segment.
		 */
		baselineModel.removeBaseline(40000, 60000);
		assertEquals("BackgroundAbundance", 7799.78f, baselineModel.getBackground(39999), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(40000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(60000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(100000), 0);
	}

	@Test
	public void testBaseline_5() {

		/*
		 * Cut the beginning part of an existing segment.
		 */
		baselineModel.removeBaseline(1000, 10000);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(1000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(5000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(10000), 0);
		assertEquals("BackgroundAbundance", 1200.22f, baselineModel.getBackground(10001), 0);
	}

	@Test
	public void testBaseline_6() {

		/*
		 * The baseline will be removed after calling
		 * baselineModel.removeBaseline(1000, 10000). But now check that the
		 * segment is right in place.
		 */
		baselineModel.addBaseline(2000, 4000, 500, 500, true);
		assertEquals("BackgroundAbundance", 500.0f, baselineModel.getBackground(2000), 0);
		assertEquals("BackgroundAbundance", 500.0f, baselineModel.getBackground(2500), 0);
		assertEquals("BackgroundAbundance", 500.0f, baselineModel.getBackground(4000), 0);
		/*
		 * The baseline will be removed after calling
		 * baselineModel.removeBaseline(1000, 10000). But now check that the
		 * segment is right in place.
		 */
		baselineModel.addBaseline(4500, 8000, 800, 800, true);
		assertEquals("BackgroundAbundance", 800.0f, baselineModel.getBackground(4500), 0);
		assertEquals("BackgroundAbundance", 800.0f, baselineModel.getBackground(6500), 0);
		assertEquals("BackgroundAbundance", 800.0f, baselineModel.getBackground(8000), 0);
		/*
		 * If the segment is totally hidden by the segment to be inserted,
		 * remove it.
		 */
		baselineModel.removeBaseline(1000, 10000);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(1000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(2000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(2500), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(4000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(4500), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(6500), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(8000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(10000), 0);
		assertEquals("BackgroundAbundance", 1200.22f, baselineModel.getBackground(10001), 0);
	}

	@Test
	public void testBaseline_7() {

		/*
		 * Cut an existing segment into two peaces.
		 */
		// baselineModel.addBaseline(5000, 50000, 100, 10000, true);
		baselineModel.removeBaseline(5001, 49999);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(4999), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(5000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(5001), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(49999), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(50000), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(50001), 0);
	}

	@Test
	public void testBaseline_8() {

		/*
		 * Cut an existing segment into two peaces.
		 */
		// baselineModel.addBaseline(5000, 50000, 100, 10000, true);
		baselineModel.removeBaseline(5001, 49999);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(4999), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(5000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(5001), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(49999), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(50000), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(50001), 0);
		baselineModel.addBaseline(5000, 5000, 1f, 1f, true);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(5000), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(4999), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(5001), 0);
		baselineModel.addBaseline(5000, 5001, 1f, 1f, true);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(5000), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(4999), 0);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(5001), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(5002), 0);
	}

	@Test
	public void testBaseline_9() {

		/*
		 * Cut an existing segment into two peaces.
		 */
		// baselineModel.addBaseline(5000, 50000, 100, 10000, true);
		baselineModel.removeBaseline(5001, 49999);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(4999), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(5000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(5001), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(49999), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(50000), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(50001), 0);
		baselineModel.addBaseline(2000, 49999, 1f, 1f, true);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(4999), 0);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(5000), 0);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(49999), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(50000), 0);
	}

	@Test
	public void testBaseline_10() {

		/*
		 * Cut an existing segment into two peaces.
		 */
		// baselineModel.addBaseline(5000, 50000, 100, 10000, true);
		baselineModel.removeBaseline(5001, 49999);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(4999), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(5000), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(5001), 0);
		assertEquals("BackgroundAbundance", 0.0f, baselineModel.getBackground(49999), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(50000), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(50001), 0);
		baselineModel.addBaseline(2000, 49999, 1f, 1f, true);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(2000), 0);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(4999), 0);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(5000), 0);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(49999), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(50000), 0);
		baselineModel.addBaseline(4000, 60000, 2f, 2f, true);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(2000), 0);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(3999), 0);
		assertEquals("BackgroundAbundance", 2f, baselineModel.getBackground(4000), 0);
		assertEquals("BackgroundAbundance", 2f, baselineModel.getBackground(4001), 0);
		assertEquals("BackgroundAbundance", 2f, baselineModel.getBackground(50000), 0);
		assertEquals("BackgroundAbundance", 2f, baselineModel.getBackground(50001), 0);
		assertEquals("BackgroundAbundance", 2f, baselineModel.getBackground(60000), 0);
	}

	@Test
	public void testBaseline_11() {

		/*
		 * Cut an existing segment into two peaces.
		 */
		// baselineModel.addBaseline(5000, 50000, 100, 10000, true);
		ITotalScanSignals totalScanSignals = new TotalScanSignals(1, 2);
		totalScanSignals.add(new TotalScanSignal(5001, 1f, 1f));
		totalScanSignals.add(new TotalScanSignal(5002, 1f, 1f));
		baselineModel.addBaseline(totalScanSignals);
		assertEquals("BackgroundAbundance", 100f, baselineModel.getBackground(5000), 0);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(5001), 0);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(5002), 0);
		assertEquals("BackgroundAbundance", 100.66f, baselineModel.getBackground(5003), 0);
	}

	@Test
	public void testBaseline_12() {

		/*
		 * Cut an existing segment into two peaces.
		 */
		// baselineModel.addBaseline(5000, 50000, 100, 10000, true);
		baselineModel.removeBaseline(5001, 49999);
		ITotalScanSignals totalScanSignals = new TotalScanSignals(1, 2);
		totalScanSignals.add(new TotalScanSignal(5004, 1f, 1f));
		totalScanSignals.add(new TotalScanSignal(5005, 1f, 1f));
		baselineModel.addBaseline(totalScanSignals);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(5003), 0);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(5004), 0);
		assertEquals("BackgroundAbundance", 1f, baselineModel.getBackground(5005), 0);
		assertEquals("BackgroundAbundance", 0f, baselineModel.getBackground(5006), 0);
	}
}
