/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CondenseMassSpectrumCalculator_1_Test {

	private CondenseMassSpectrumCalculator calculator;

	@Before
	public void setUp() {

		calculator = new CondenseMassSpectrumCalculator(true);
		calculator.add(18.1d, 1000.0d);
		calculator.add(18.2d, 1000.0d);
		calculator.add(18.4d, 1000.0d);
		calculator.add(18.5d, 1000.0d);
		calculator.add(18.9d, 1000.0d);
	}

	@Test
	public void test1() {

		Map<Double, Double> mappedTraces = calculator.getMappedTraces();
		assertEquals(2, mappedTraces.size());
		assertTrue(mappedTraces.containsKey(18.0d));
		assertEquals(3000.0d, mappedTraces.get(18.0d), 0);
		assertTrue(mappedTraces.containsKey(19.0d));
		assertEquals(2000.0d, mappedTraces.get(19.0d), 0);
	}
}
