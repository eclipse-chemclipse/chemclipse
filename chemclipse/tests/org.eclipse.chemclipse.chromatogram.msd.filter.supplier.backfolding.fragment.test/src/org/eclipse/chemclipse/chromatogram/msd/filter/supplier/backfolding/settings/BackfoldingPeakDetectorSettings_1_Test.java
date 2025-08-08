/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.backfolding.settings;

import org.eclipse.chemclipse.chromatogram.peak.detector.model.Threshold;
import org.junit.Test;

import junit.framework.TestCase;

public class BackfoldingPeakDetectorSettings_1_Test extends TestCase {

	private PeakDetectorSettings settings = new PeakDetectorSettings();

	@Test
	public void testGetBackfoldingSettings_1() {

		IBackfoldingSettings backfoldingSettings = settings.getBackfoldingSettings();
		assertNotNull(backfoldingSettings);
	}

	@Test
	public void testGetThreshold_1() {

		Threshold threshold = settings.getThreshold();
		assertEquals("Threshold", Threshold.MEDIUM, threshold);
	}

	@Test
	public void testSetThreshold_1() {

		settings.setThreshold(Threshold.HIGH);
		Threshold threshold = settings.getThreshold();
		assertEquals("Threshold", Threshold.HIGH, threshold);
	}
}
