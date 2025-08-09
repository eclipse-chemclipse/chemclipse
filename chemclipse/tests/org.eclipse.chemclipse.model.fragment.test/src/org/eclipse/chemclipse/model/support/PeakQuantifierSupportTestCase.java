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
package org.eclipse.chemclipse.model.support;

import static org.junit.Assert.assertNotNull;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IPeakIntensityValues;
import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.implementation.Peak;
import org.eclipse.chemclipse.model.implementation.PeakIntensityValues;
import org.eclipse.chemclipse.model.implementation.PeakModel;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.junit.Before;
import org.junit.Test;

public class PeakQuantifierSupportTestCase {

	private IPeak peak = null;

	@Before
	public void setUp() throws Exception {

		peak = createPeak();
	}

	public IPeak getPeak() {

		return peak;
	}

	@Test
	public void test0() {

		assertNotNull(peak);
	}

	private IPeak createPeak() {

		IScan peakMaximum = new Scan(1000);
		IPeakIntensityValues peakIntensityValues = new PeakIntensityValues();
		peakIntensityValues.addIntensityValue(100, 10.f);
		peakIntensityValues.addIntensityValue(200, 30.f);
		peakIntensityValues.addIntensityValue(300, 80.f);
		peakIntensityValues.addIntensityValue(400, 100.f);
		peakIntensityValues.addIntensityValue(500, 70.f);
		peakIntensityValues.addIntensityValue(600, 40.f);
		IPeakModel peakModel = new PeakModel(peakMaximum, peakIntensityValues, 0.0f, 0.0f);
		peakModel.setStrictModel(true);

		return new Peak(peakModel);
	}
}