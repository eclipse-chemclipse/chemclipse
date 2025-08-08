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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.noise.stein.core;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.noise.stein.TestPathHelper;
import org.eclipse.chemclipse.model.results.ChromatogramSegmentation;
import org.junit.Before;
import org.junit.Test;

public class NoiseCalculator_1_ITest extends ChromatogramReaderTestCase {

	private NoiseCalculator noiseCalculator = new NoiseCalculator();

	@Override
	@Before
	public void setUp() throws Exception {

		pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1);
		super.setUp();
	}

	@Test
	public void testReader_1() {

		chromatogram.addMeasurementResult(new ChromatogramSegmentation(chromatogram, 9));
		assertEquals(53.83383f, noiseCalculator.getSignalToNoiseRatio(chromatogram, 500), 0);
	}
}
