/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.quantitation.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.chromatogram.xxd.quantitation.exceptions.NoPeakQuantifierAvailableException;
import org.junit.jupiter.api.Test;

public class CompensationCalculator_1_Test {

	@Test
	public void test_1() throws NoPeakQuantifierAvailableException {

		CompensationCalculator compensationCalculator = new CompensationCalculator(25, 50);
		double compensationFactorActualValue = compensationCalculator.getCompensationFactorCalculated(2, 1);
		assertEquals(0.5d, compensationFactorActualValue, 0);
	}

	@Test
	public void test_2() throws NoPeakQuantifierAvailableException {

		CompensationCalculator compensationCalculator = new CompensationCalculator(25, 50);
		double compensationFactorActualValue = compensationCalculator.getCompensationFactorCalculated(2, 0.7);
		assertEquals(0.35d, compensationFactorActualValue, 0);
	}
}