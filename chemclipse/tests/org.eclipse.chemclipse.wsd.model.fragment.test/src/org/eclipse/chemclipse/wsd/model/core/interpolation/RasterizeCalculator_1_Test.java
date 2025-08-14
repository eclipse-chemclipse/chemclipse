/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.model.core.interpolation;

import static org.junit.Assert.assertNull;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.chemclipse.model.interpolation.RasterizeCalculator;
import org.junit.Before;
import org.junit.Test;

public class RasterizeCalculator_1_Test {

	private TreeMap<Float, Float> dataOriginal = new TreeMap<>();

	@Before
	public void setUp() {

		dataOriginal.put(200.0f, 500.0f);
		dataOriginal.put(202.0f, 1000.0f);
		dataOriginal.put(204.0f, 1500.0f);
	}

	@Test
	public void test1() {

		Map<Integer, Float> dataRasterized = RasterizeCalculator.apply(dataOriginal, 0);
		assertNull(dataRasterized);
	}
}