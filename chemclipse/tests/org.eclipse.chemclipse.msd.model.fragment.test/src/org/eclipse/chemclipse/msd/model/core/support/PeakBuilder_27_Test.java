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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.model.core.IPeakIntensityValues;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak exceptions.
 */
public class PeakBuilder_27_Test {

	private ITotalScanSignals totalIonSignals;
	private ITotalScanSignal totalIonSignal;
	private IPeakIntensityValues peakIntensityValues;

	@Before
	public void setUp() throws Exception {

		List<Float> intensities = new ArrayList<Float>();
		intensities.add(0.0f);
		intensities.add(54.1f);
		intensities.add(78.38f);
		intensities.add(100.0f);
		intensities.add(49.69f);
		intensities.add(23.79f);
		intensities.add(9.8f);
		intensities.add(3.23f);
		intensities.add(0.84f);
		intensities.add(0.0f);
		totalIonSignals = new TotalScanSignals(1, 10);
		float abundance = 0.0f;
		for(int scan = 1; scan <= 10; scan++) {
			abundance = intensities.get(scan - 1);
			totalIonSignal = new TotalScanSignal(scan * 10, 0.0f, abundance);
			totalIonSignals.add(totalIonSignal);
		}
	}

	@Test
	public void testGetPeakIntensityValues_1() {

		Map.Entry<Integer, Float> value;
		peakIntensityValues = PeakBuilderMSD.getPeakIntensityValues(totalIonSignals);
		assertNotNull(peakIntensityValues);
		value = peakIntensityValues.getIntensityValue(10);
		assertEquals("Intensity", 0.0f, value.getValue(), 0);
		value = peakIntensityValues.getIntensityValue(40);
		assertEquals("Intensity", IPeakIntensityValues.MAX_INTENSITY, value.getValue(), 0);
		value = peakIntensityValues.getIntensityValue(100);
		assertEquals("Intensity", 0.0f, value.getValue(), 0);
	}

	@Test
	public void testGetPeakIntensityValues_2() {

		assertThrows(PeakException.class, () -> {
			peakIntensityValues = PeakBuilderMSD.getPeakIntensityValues(null);
		});
	}
}
