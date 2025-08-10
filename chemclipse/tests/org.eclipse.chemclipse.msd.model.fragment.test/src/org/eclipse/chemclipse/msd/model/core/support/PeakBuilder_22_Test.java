/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.core.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.eclipse.chemclipse.model.support.BackgroundAbundanceRange;
import org.eclipse.chemclipse.model.support.IBackgroundAbundanceRange;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak exceptions.
 */
public class PeakBuilder_22_Test {

	private ITotalScanSignals totalIonSignals;
	private ITotalScanSignal totalIonSignal;
	private IScanRange scanRange;
	private IBackgroundAbundanceRange backgroundAbundanceRange;
	private IBackgroundAbundanceRange correctedBackground;

	@Before
	public void setUp() throws Exception {

		totalIonSignals = new TotalScanSignals(20, 30);
		scanRange = new ScanRange(20, 30);
		for(int scan = 20; scan <= 30; scan++) {
			totalIonSignal = new TotalScanSignal(scan * 10, 0.0f, 1000.0f);
			totalIonSignals.add(totalIonSignal);
		}
	}

	@Test
	public void testCheckBackgroundAbundanceRange_1() {

		backgroundAbundanceRange = new BackgroundAbundanceRange(1500.0f, 1500.0f);
		correctedBackground = PeakBuilderMSD.checkBackgroundAbundanceRange(totalIonSignals, scanRange, backgroundAbundanceRange);
		assertEquals("Start", 1000.0f, correctedBackground.getStartBackgroundAbundance(), 0);
		assertEquals("Stop", 1000.0f, correctedBackground.getStopBackgroundAbundance(), 0);
	}

	@Test
	public void testCheckBackgroundAbundanceRange_2() {

		backgroundAbundanceRange = new BackgroundAbundanceRange(800.0f, 1500.0f);
		correctedBackground = PeakBuilderMSD.checkBackgroundAbundanceRange(totalIonSignals, scanRange, backgroundAbundanceRange);
		assertEquals("Start", 800.0f, correctedBackground.getStartBackgroundAbundance(), 0);
		assertEquals("Stop", 1000.0f, correctedBackground.getStopBackgroundAbundance(), 0);
	}

	@Test
	public void testCheckBackgroundAbundanceRange_3() {

		backgroundAbundanceRange = new BackgroundAbundanceRange(1500.0f, 750.0f);
		correctedBackground = PeakBuilderMSD.checkBackgroundAbundanceRange(totalIonSignals, scanRange, backgroundAbundanceRange);
		assertEquals("Start", 1000.0f, correctedBackground.getStartBackgroundAbundance(), 0);
		assertEquals("Stop", 750.0f, correctedBackground.getStopBackgroundAbundance(), 0);
	}

	@Test
	public void testCheckBackgroundAbundanceRange_4() {

		backgroundAbundanceRange = new BackgroundAbundanceRange(1500.0f, 750.0f);
		assertThrows(PeakException.class, () -> {
			correctedBackground = PeakBuilderMSD.checkBackgroundAbundanceRange(null, scanRange, backgroundAbundanceRange);
		});
	}

	@Test
	public void testCheckBackgroundAbundanceRange_5() {

		backgroundAbundanceRange = new BackgroundAbundanceRange(1500.0f, 750.0f);
		assertThrows(PeakException.class, () -> {
			correctedBackground = PeakBuilderMSD.checkBackgroundAbundanceRange(totalIonSignals, null, backgroundAbundanceRange);
		});
	}

	@Test
	public void testCheckBackgroundAbundanceRange_6() {

		assertThrows(PeakException.class, () -> {
			correctedBackground = PeakBuilderMSD.checkBackgroundAbundanceRange(totalIonSignals, scanRange, null);
		});
	}
}
