/*******************************************************************************
 * Copyright (c) 2008, 2016 Philip (eselmeister) Wenig.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip (eselmeister) Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.internal.core;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.internal.core.ChromatogramIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.internal.core.IChromatogramIntegrator;

public class TrapezoidChromatogramIntegrator_1_Test extends SimpleChromatogramTestCase {

	private IChromatogramIntegrator integrator;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		integrator = new ChromatogramIntegrator();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void testIntegrate_1() {

		double area = integrator.integrate(chromatogramSelection);
		assertEquals("", 30000.0d, area);
	}
}
