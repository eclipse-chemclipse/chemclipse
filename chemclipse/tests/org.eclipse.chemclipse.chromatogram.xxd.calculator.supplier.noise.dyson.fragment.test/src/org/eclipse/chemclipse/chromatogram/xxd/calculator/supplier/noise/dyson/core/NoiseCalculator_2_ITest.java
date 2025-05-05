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

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.noise.dyson.TestPathHelper;
import org.eclipse.chemclipse.model.results.ChromatogramSegmentation;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;

public class NoiseCalculator_2_ITest extends ChromatogramReaderTestCase {

	private NoiseCalculator noiseCalculator;
	private ITotalScanSignals signals;

	@Override
	protected void setUp() throws Exception {

		pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_2);
		super.setUp();
		noiseCalculator = new NoiseCalculator();
		signals = new TotalScanSignals(chromatogram);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void testReader_1() {

		/*
		 * The loading time of the chromatogram takes a while.
		 * That's why several tests are made here.
		 */
		chromatogram.addMeasurementResult(new ChromatogramSegmentation(chromatogram, 13));
		assertEquals(0.0f, noiseCalculator.getSignalToNoiseRatio(chromatogram, signals, 0));
		assertEquals(25f, noiseCalculator.getSignalToNoiseRatio(chromatogram, signals, 50));
		assertEquals(65.75f, noiseCalculator.getSignalToNoiseRatio(chromatogram, signals, 131.5f));
		assertEquals(40000f, noiseCalculator.getSignalToNoiseRatio(chromatogram, signals, 80000));
	}
}
