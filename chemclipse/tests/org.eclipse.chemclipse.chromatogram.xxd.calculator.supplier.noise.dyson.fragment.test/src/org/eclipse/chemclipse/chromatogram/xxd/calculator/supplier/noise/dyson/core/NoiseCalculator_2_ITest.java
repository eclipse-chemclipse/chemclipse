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
 * Christoph Läubrich - update to reflect changes in INoiseCalculator API
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.noise.dyson.core;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.noise.dyson.TestPathHelper;
import org.eclipse.chemclipse.model.results.ChromatogramSegmentation;
import org.junit.Before;
import org.junit.Test;

public class NoiseCalculator_2_ITest extends ChromatogramReaderTestCase {

	private NoiseCalculator noiseCalculator = new NoiseCalculator();;

	@Override
	@Before
	public void setUp() throws Exception {

		pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_2);
		super.setUp();
	}

	@Test
	public void testReader_1() {

		/*
		 * The loading time of the chromatogram takes a while.
		 * That's why several tests are made here.
		 */
		chromatogram.addMeasurementResult(new ChromatogramSegmentation(chromatogram, 13));
		assertEquals(0.0f, noiseCalculator.getSignalToNoiseRatio(chromatogram, 0), 0);
		assertEquals(25f, noiseCalculator.getSignalToNoiseRatio(chromatogram, 50), 0);
		assertEquals(65.75f, noiseCalculator.getSignalToNoiseRatio(chromatogram, 131.5f), 0);
		assertEquals(40000f, noiseCalculator.getSignalToNoiseRatio(chromatogram, 80000), 0);
	}
}
