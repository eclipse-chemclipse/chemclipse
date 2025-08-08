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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.internal.core;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.processor.BackgroundIntegrator;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.junit.Before;
import org.junit.Test;

public class TrapezoidBackgroundIntegrator_1_Test extends SimpleChromatogramTestCase {

	private BackgroundIntegrator integrator;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		integrator = new BackgroundIntegrator();
	}

	@Test
	public void testIntegrate_1() {

		IBaselineModel baselineModel = chromatogram.getBaselineModel();
		baselineModel.addBaseline(4500, 6500, 200, 200, true);
		double area = integrator.integrate(chromatogramSelection);
		assertEquals("Background", 4000.0d, area, 0);
	}

	@Test
	public void testIntegrate_2() {

		IBaselineModel baselineModel = chromatogram.getBaselineModel();
		baselineModel.addBaseline(4500, 6500, 400, 400, true);
		double area = integrator.integrate(chromatogramSelection);
		assertEquals("Background", 8000.0d, area, 0);
	}

	@Test
	public void testIntegrate_3() {

		IBaselineModel baselineModel = chromatogram.getBaselineModel();
		baselineModel.addBaseline(4500, 6500, 200, 400, true);
		double area = integrator.integrate(chromatogramSelection);
		assertEquals("Background", 6000.0d, area, 0);
	}
}
