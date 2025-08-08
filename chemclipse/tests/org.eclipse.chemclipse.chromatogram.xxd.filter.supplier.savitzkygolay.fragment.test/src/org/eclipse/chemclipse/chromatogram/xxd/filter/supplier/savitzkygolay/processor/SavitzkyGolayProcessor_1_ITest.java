/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.processor;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.TestPathHelper;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.settings.ChromatogramFilterSettings;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class SavitzkyGolayProcessor_1_ITest {

	private ITotalScanSignals totalScanSignals;
	private ChromatogramFilterSettings supplierFilterSettings;

	@Before
	public void setUp() throws Exception {

		/*
		 * Signals
		 */
		totalScanSignals = new TotalScanSignals(5726);
		BufferedReader reader = new BufferedReader(new FileReader(new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1))));
		String line;
		while((line = reader.readLine()) != null) {
			String[] values = line.split(", ");
			ITotalScanSignal totalScanSignal = new TotalScanSignal(Integer.valueOf(values[0]), Float.valueOf(values[1]), Float.valueOf(values[2]));
			totalScanSignals.add(totalScanSignal);
		}
		reader.close();
		/*
		 * Processor and settings
		 */
		supplierFilterSettings = new ChromatogramFilterSettings();
		supplierFilterSettings.setWidth(5);
		supplierFilterSettings.setOrder(2);
		supplierFilterSettings.setDerivative(0);
	}

	@Test
	public void test1() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(5726, sgTic.length);
	}

	@Test
	public void test2() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(67506.74285714285d, sgTic[0], 0);
	}

	@Test
	public void test3() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(61229.22857142857d, sgTic[1], 0);
	}

	@Test
	public void test4() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(55891.85714285714d, sgTic[2], 0);
	}

	@Test
	public void test5() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(51801.742857142846d, sgTic[3], 0);
	}

	@Test
	public void test6() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(45188.17142857142d, sgTic[289], 0);
	}

	@Test
	public void test7() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(43531.057142857135d, sgTic[892], 0);
	}

	@Test
	public void test8() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(72842.85714285713d, sgTic[1293], 0);
	}

	@Test
	public void test9() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(35408.77142857142d, sgTic[1474], 0);
	}

	@Test
	public void test10() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(70151.79999999999d, sgTic[2970], 0);
	}

	@Test
	public void test11() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(230127.19999999995d, sgTic[3766], 0);
	}

	@Test
	public void test12() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(225207.37142857138d, sgTic[4180], 0);
	}

	@Test
	public void test13() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(334841.2857142857d, sgTic[4993], 0);
	}

	@Test
	public void test14() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(157031.5714285714d, sgTic[5722], 0);
	}

	@Test
	public void test15() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(157544.11428571428d, sgTic[5723], 0);
	}

	@Test
	public void test16() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(156061.45714285714d, sgTic[5724], 0);
	}

	@Test
	public void test17() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(152630.08571428567d, sgTic[5725], 0);
	}
}
