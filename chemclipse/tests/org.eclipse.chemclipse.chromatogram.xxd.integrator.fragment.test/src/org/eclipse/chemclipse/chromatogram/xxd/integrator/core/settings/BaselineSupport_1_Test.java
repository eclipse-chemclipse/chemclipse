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

import org.eclipse.chemclipse.model.baseline.BaselineModel;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.junit.Test;

import junit.framework.TestCase;

public class BaselineSupport_1_Test extends TestCase {

	private IBaselineSupport baselineSupport = new BaselineSupport();

	@Test
	public void testBaselineBack_1() {

		float actual;
		baselineSupport.setBaselineBack(1500);
		actual = baselineSupport.getBackgroundAbundance(1500);
		assertEquals(0.0f, actual);
		actual = baselineSupport.getBackgroundAbundance(1);
		assertEquals(0.0f, actual);
	}

	@Test
	public void testBaselineHoldOn_1() {

		float actual;
		baselineSupport.setBaselineHoldOn(1500, 2500);
		actual = baselineSupport.getBackgroundAbundance(1500);
		assertEquals(0.0f, actual);
		actual = baselineSupport.getBackgroundAbundance(2500);
		assertEquals(0.0f, actual);
	}

	@Test
	public void testBaselineNow_1() {

		float actual;
		baselineSupport.setBaselineNow(1500);
		actual = baselineSupport.getBackgroundAbundance(1500);
		assertEquals(0.0f, actual);
		actual = baselineSupport.getBackgroundAbundance(2000);
		assertEquals(0.0f, actual);
	}

	@Test
	public void testSetBaselineModel_1() {

		float actual;
		IChromatogramMSD chromatogram = new ChromatogramMSD();
		IBaselineModel baselineModel = new BaselineModel(chromatogram);
		baselineSupport.setBaselineModel(baselineModel);
		actual = baselineSupport.getBackgroundAbundance(1500);
		assertEquals(0.0f, actual);
		actual = baselineSupport.getBackgroundAbundance(2000);
		assertEquals(0.0f, actual);
	}

	@Test
	public void testReset_1() {

		float actual;
		baselineSupport.setBaselineNow(1500);
		baselineSupport.reset();
		actual = baselineSupport.getBackgroundAbundance(1500);
		assertEquals(0.0f, actual);
		actual = baselineSupport.getBackgroundAbundance(2000);
		assertEquals(0.0f, actual);
	}
}
