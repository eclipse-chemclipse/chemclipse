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
package org.eclipse.chemclipse.msd.identifier.comparison.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.jupiter.api.Test;

public class GeometricDistanceCalculator_4_Test extends MassSpectrumSetTestCase {

	private GeometricDistanceCalculator calculator = new GeometricDistanceCalculator();

	@Test
	public void test1() {

		IScanMSD unknown = sinapylAclohol.getMassSpectrum();
		IScanMSD reference = phenolBenzimidazolyl.getMassSpectrum();
		assertEquals(0.55392367f, calculator.calculate(unknown, reference, unknown.getExtractedIonSignal().getIonRange()), 0);
	}

	@Test
	public void test2() {

		IScanMSD unknown = phenolBenzimidazolyl.getMassSpectrum();
		IScanMSD reference = sinapylAclohol.getMassSpectrum();
		assertEquals(0.5543051f, calculator.calculate(unknown, reference, unknown.getExtractedIonSignal().getIonRange()), 0);
	}

	@Test
	public void test3() {

		IScanMSD unknown = phenolBenzimidazolyl.getMassSpectrum();
		IScanMSD reference = phenolBenzimidazolyl.getMassSpectrum();
		assertEquals(1.0f, calculator.calculate(unknown, reference, unknown.getExtractedIonSignal().getIonRange()), 0);
	}
}
