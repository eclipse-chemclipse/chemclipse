/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.subtract.internal.calculator;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.subtract.calculator.SubtractCalculator;
import org.junit.Test;

public class SubtractCalculator_1_Test {

	private SubtractCalculator subtractCalculator = new SubtractCalculator();

	@Test
	public void testMassSpectrumMap_1() {

		Map<Double, Float> subtractMassSpectrumMap = subtractCalculator.getMassSpectrumMap(null, true, true);
		assertEquals(0, subtractMassSpectrumMap.size());
	}

	@Test
	public void testMassSpectrumMap_2() {

		Map<Double, Float> subtractMassSpectrumMap = subtractCalculator.getMassSpectrumMap(null, true, false);
		assertEquals(0, subtractMassSpectrumMap.size());
	}
}
