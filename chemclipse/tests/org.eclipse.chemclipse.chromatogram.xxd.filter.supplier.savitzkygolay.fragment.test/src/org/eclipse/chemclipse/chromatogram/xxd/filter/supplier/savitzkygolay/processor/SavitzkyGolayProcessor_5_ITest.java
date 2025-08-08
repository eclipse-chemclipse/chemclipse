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

public class SavitzkyGolayProcessor_5_ITest {

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
		supplierFilterSettings.setWidth(31);
		supplierFilterSettings.setOrder(4);
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
		assertEquals(67758.20896584442d, sgTic[0], 0);
	}

	@Test
	public void test3() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(61266.42230895289d, sgTic[1], 0);
	}

	@Test
	public void test4() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(55875.55772159202d, sgTic[2], 0);
	}

	@Test
	public void test5() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(51437.635988888396d, sgTic[3], 0);
	}

	@Test
	public void test6() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(44964.866012741455d, sgTic[289], 0);
	}

	@Test
	public void test7() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(49439.79381130549d, sgTic[892], 0);
	}

	@Test
	public void test8() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(72844.98978663163d, sgTic[1293], 0);
	}

	@Test
	public void test9() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(38271.11345939934d, sgTic[1474], 0);
	}

	@Test
	public void test10() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(76556.411265042d, sgTic[2970], 0);
	}

	@Test
	public void test11() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(236207.10789766416d, sgTic[3766], 0);
	}

	@Test
	public void test12() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(230968.30559207205d, sgTic[4180], 0);
	}

	@Test
	public void test13() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(333919.43573667714d, sgTic[4993], 0);
	}

	@Test
	public void test14() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(156212.88376568144d, sgTic[5722], 0);
	}

	@Test
	public void test15() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(155765.43254685838d, sgTic[5723], 0);
	}

	@Test
	public void test16() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(155078.02171381755d, sgTic[5724], 0);
	}

	@Test
	public void test17() {

		double[] sgTic = SavitzkyGolayProcessor.smooth(totalScanSignals, supplierFilterSettings, new NullProgressMonitor());
		assertEquals(154096.83778247377d, sgTic[5725], 0);
	}
}
