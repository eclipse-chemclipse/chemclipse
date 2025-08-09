/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.TreeMap;

import org.junit.Test;

public class Peak_2_Test extends AbstractPeakTestCase {

	private IPeak peak = createPeak();

	@Test
	public void test1() {

		assertNotNull(peak);
	}

	@Test
	public void test2() {

		/*
		 * 1.0177305 - strictModel: true
		 * 1.0165975 - strictModel: false
		 */
		assertEquals(1.0177305f, peak.getPeakModel().getTailing(), 0);
	}

	private IPeak createPeak() {

		float totalSignal = 239;
		float startBackgroundAbundance = 0;
		float stopBackgroundAbundance = 0;

		TreeMap<Integer, Float> retentionTimeIntensityMap = new TreeMap<>();
		retentionTimeIntensityMap.put(3032172, 0.0f);
		retentionTimeIntensityMap.put(3032883, 61.0f);
		retentionTimeIntensityMap.put(3033595, 118.0f);
		retentionTimeIntensityMap.put(3034306, 60.0f);
		retentionTimeIntensityMap.put(3035017, 0.0f);

		return createPeak(totalSignal, retentionTimeIntensityMap, startBackgroundAbundance, stopBackgroundAbundance, true);
	}
}