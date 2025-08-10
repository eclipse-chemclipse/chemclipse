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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.eclipse.chemclipse.model.support.BackgroundAbundanceRange;
import org.eclipse.chemclipse.model.support.IBackgroundAbundanceRange;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.numeric.equations.LinearEquation;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak exceptions.
 * 
 * @author Philip Wenig
 */
public class PeakBuilder_25_Test {

	private ITotalScanSignals totalIonSignals;
	private ITotalScanSignal totalIonSignal;
	private IScanRange scanRange;
	private IBackgroundAbundanceRange backgroundAbundanceRange;
	private LinearEquation linearEquation;

	@Before
	public void setUp() throws Exception {

		List<Float> intensities = new ArrayList<Float>();
		intensities.add(1000.00f);
		intensities.add(5578.14f);
		intensities.add(7596.27f);
		intensities.add(9386.37f);
		intensities.add(5000.0f);
		intensities.add(2709.21f);
		intensities.add(1440.9f);
		intensities.add(810.72f);
		intensities.add(538.22f);
		intensities.add(400.00f);
		scanRange = new ScanRange(1, 10);
		totalIonSignals = new TotalScanSignals(1, 10);
		float abundance = 0.0f;
		for(int scan = 1; scan <= 10; scan++) {
			abundance = intensities.get(scan - 1);
			totalIonSignal = new TotalScanSignal(scan * 10, 0.0f, abundance);
			totalIonSignals.add(totalIonSignal);
		}
		backgroundAbundanceRange = new BackgroundAbundanceRange(1000.0f, 400.0f);
	}

	@Test
	public void testGetBackgroundEquation_1() {

		linearEquation = PeakBuilderMSD.getBackgroundEquation(totalIonSignals, scanRange, backgroundAbundanceRange);
		assertEquals("A", -6.666666666666667, linearEquation.getA(), 0);
		assertEquals("B", 1066.6666666666667, linearEquation.getB(), 0);
	}

	@Test
	public void testGetBackgroundEquation_2() {

		assertThrows(PeakException.class, () -> {
			linearEquation = PeakBuilderMSD.getBackgroundEquation(null, scanRange, backgroundAbundanceRange);
		});
	}

	public void testGetBackgroundEquation_3() {

		assertThrows(PeakException.class, () -> {
			linearEquation = PeakBuilderMSD.getBackgroundEquation(totalIonSignals, null, backgroundAbundanceRange);
		});
	}

	public void testGetBackgroundEquation_4() {

		assertThrows(PeakException.class, () -> {
			linearEquation = PeakBuilderMSD.getBackgroundEquation(totalIonSignals, scanRange, null);
		});
	}
}
