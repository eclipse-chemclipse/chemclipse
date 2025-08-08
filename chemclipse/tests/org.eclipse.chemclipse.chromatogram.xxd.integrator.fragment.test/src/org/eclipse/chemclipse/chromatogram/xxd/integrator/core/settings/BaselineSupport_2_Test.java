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
import static org.junit.Assert.assertNotSame;

import org.eclipse.chemclipse.model.baseline.BaselineModel;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class BaselineSupport_2_Test {

	private IBaselineSupport baselineSupport;
	private IBaselineModel baselineModel;
	private IChromatogramMSD chromatogram;
	private IRegularMassSpectrum supplierMassSpectrum;

	@Before
	public void setUp() throws Exception {

		chromatogram = new ChromatogramMSD();
		chromatogram.setScanDelay(4500);
		chromatogram.setScanInterval(1000);
		for(int i = 1; i <= 100; i++) {
			supplierMassSpectrum = new VendorMassSpectrum();
			chromatogram.addScan(supplierMassSpectrum);
		}
		chromatogram.recalculateRetentionTimes();
		baselineModel = chromatogram.getBaselineModel();
		baselineModel.addBaseline(4500, 103500, 2000.0f, 2000.0f, true);
		baselineSupport = new BaselineSupport(baselineModel);
	}

	@Test
	public void testBaselineModel_1() {

		assertNotSame(baselineModel, baselineSupport.getBaselineModel());
	}

	@Test
	public void testBaselineBack_1() {

		float actual;
		baselineModel = baselineSupport.getBaselineModel();
		baselineModel.addBaseline(7000, 7500, 8000.0f, 8000.0f, true);
		baselineSupport.setBaselineBack(7500);
		actual = baselineSupport.getBackgroundAbundance(7500);
		assertEquals(8000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(4500);
		assertEquals(8000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(7501);
		assertEquals(2000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(103500);
		assertEquals(2000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(103501);
		assertEquals(0.0f, actual, 0);
	}

	@Test
	public void testBaselineHoldOn_1() {

		float actual;
		baselineModel = baselineSupport.getBaselineModel();
		baselineModel.addBaseline(7000, 10000, 8000.0f, 8000.0f, true);
		baselineSupport.setBaselineHoldOn(10000, 20000);
		actual = baselineSupport.getBackgroundAbundance(6999);
		assertEquals(2000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(10000);
		assertEquals(8000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(10001);
		assertEquals(8000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(20000);
		assertEquals(8000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(20001);
		assertEquals(2000.0f, actual, 0);
	}

	@Test
	public void testBaselineNow_1() {

		float actual;
		baselineModel = baselineSupport.getBaselineModel();
		baselineModel.addBaseline(7000, 10000, 8000.0f, 8000.0f, true);
		baselineSupport.setBaselineNow(6999);
		actual = baselineSupport.getBackgroundAbundance(6999);
		assertEquals(2000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(10000);
		assertEquals(2000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(10001);
		assertEquals(2000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(20000);
		assertEquals(2000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(103500);
		assertEquals(2000.0f, actual, 0);
	}

	@Test
	public void testSetBaselineModel_1() {

		float actual;
		IChromatogramMSD chromatogram = new ChromatogramMSD();
		IBaselineModel baselineModel = new BaselineModel(chromatogram);
		baselineSupport.setBaselineModel(baselineModel);
		actual = baselineSupport.getBackgroundAbundance(1500);
		assertEquals(0.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(2000);
		assertEquals(0.0f, actual, 0);
	}

	@Test
	public void testReset_1() {

		float actual;
		baselineModel = baselineSupport.getBaselineModel();
		baselineModel.addBaseline(7000, 10000, 8000.0f, 8000.0f, true);
		baselineSupport.reset();
		actual = baselineSupport.getBackgroundAbundance(4500);
		assertEquals(2000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(10001);
		assertEquals(2000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(20000);
		assertEquals(2000.0f, actual, 0);
		actual = baselineSupport.getBackgroundAbundance(103500);
		assertEquals(2000.0f, actual, 0);
	}
}
