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

import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.processor.ChromatogramIntegrator;
import org.junit.Before;
import org.junit.Test;

public class TrapezoidChromatogramIntegrator_1_Test extends SimpleChromatogramTestCase {

	private ChromatogramIntegrator integrator;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		integrator = new ChromatogramIntegrator();
	}

	@Test
	public void testIntegrate_1() {

		double area = integrator.integrate(chromatogramSelection);
		assertEquals("", 30000.0d, area, 0);
	}
}
