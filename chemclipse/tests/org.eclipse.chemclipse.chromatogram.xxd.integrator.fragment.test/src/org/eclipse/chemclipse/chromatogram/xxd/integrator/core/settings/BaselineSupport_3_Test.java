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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.baseline.BaselineModel;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class BaselineSupport_3_Test {

	private IBaselineSupport baselineSupport;
	private IChromatogramMSD chromatogram;
	private IRegularMassSpectrum ms;
	private IBaselineModel baselineModel;

	@Before
	public void setUp() throws Exception {

		baselineSupport = new BaselineSupport();
		chromatogram = new ChromatogramMSD();
		chromatogram.setScanDelay(500);
		chromatogram.setScanInterval(1000);
		for(int scan = 1; scan <= 100; scan++) {
			ms = new VendorMassSpectrum();
			chromatogram.addScan(ms);
		}
		chromatogram.recalculateRetentionTimes();
		baselineModel = new BaselineModel(chromatogram);
		baselineModel.addBaseline(500, 99500, 4000.0f, 4000.0f, true);
		baselineSupport.setBaselineModel(baselineModel);
	}

	@Test
	public void testSetBaselineModel_1() {

		assertEquals("BM", 0.0f, baselineModel.getBackgroundAbundance(400), 0);
		assertEquals("BS", 0.0f, baselineSupport.getBackgroundAbundance(400), 0);
	}

	@Test
	public void testSetBaselineModel_2() {

		assertEquals("BM", 4000.0f, baselineModel.getBackgroundAbundance(500), 0);
		assertEquals("BS", 4000.0f, baselineSupport.getBackgroundAbundance(500), 0);
	}

	@Test
	public void testSetBaselineModel_3() {

		assertEquals("BM", 4000.0f, baselineModel.getBackgroundAbundance(18500), 0);
		assertEquals("BS", 4000.0f, baselineSupport.getBackgroundAbundance(18500), 0);
	}

	@Test
	public void testSetBaselineModel_4() {

		assertEquals("BM", 4000.0f, baselineModel.getBackgroundAbundance(99500), 0);
		assertEquals("BS", 4000.0f, baselineSupport.getBackgroundAbundance(99500), 0);
	}

	@Test
	public void testSetBaselineModel_5() {

		assertEquals("BM", 0.0f, baselineModel.getBackgroundAbundance(100000), 0);
		assertEquals("BS", 0.0f, baselineSupport.getBackgroundAbundance(100000), 0);
	}
}
