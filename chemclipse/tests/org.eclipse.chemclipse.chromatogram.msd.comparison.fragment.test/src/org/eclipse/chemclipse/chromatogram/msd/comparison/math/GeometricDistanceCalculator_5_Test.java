/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.comparison.math;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.Before;
import org.junit.Test;

public class GeometricDistanceCalculator_5_Test extends MassSpectrumSetTestCase {

	private GeometricDistanceCalculator calculator = new GeometricDistanceCalculator();

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
	}

	@Test
	public void test1() {

		IScanMSD unknown = noMatchA1.getMassSpectrum();
		IScanMSD reference = noMatchA2.getMassSpectrum();
		assertEquals(0.0f, calculator.calculate(unknown, reference, unknown.getExtractedIonSignal().getIonRange()), 0);
	}

	@Test
	public void test2() {

		IScanMSD unknown = noMatchA2.getMassSpectrum();
		IScanMSD reference = noMatchA1.getMassSpectrum();
		assertEquals(0.0f, calculator.calculate(unknown, reference, unknown.getExtractedIonSignal().getIonRange()), 0);
	}

	@Test
	public void test3() {

		IScanMSD unknown = noMatchA2.getMassSpectrum();
		IScanMSD reference = noMatchA2.getMassSpectrum();
		assertEquals(1.0f, calculator.calculate(unknown, reference, unknown.getExtractedIonSignal().getIonRange()), 0);
	}
}
