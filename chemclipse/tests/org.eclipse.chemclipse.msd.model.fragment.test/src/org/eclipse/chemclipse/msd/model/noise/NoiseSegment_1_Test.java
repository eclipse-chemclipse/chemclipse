/*******************************************************************************
 * Copyright (c) 2010, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.noise;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.chemclipse.model.support.AnalysisSegment;
import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.CombinedMassSpectrum;
import org.junit.Before;
import org.junit.Test;

public class NoiseSegment_1_Test {

	private INoiseSegmentMSD noiseSegment;
	private IAnalysisSegment analysisSegment;
	private ICombinedMassSpectrum noiseMassSpectrum;

	@Before
	public void setUp() {

		analysisSegment = new AnalysisSegment(20, 200) {

			@Override
			public int getStartRetentionTime() {

				return 0;
			}

			@Override
			public int getStopRetentionTime() {

				return 0;
			}
		};
		noiseMassSpectrum = new CombinedMassSpectrum();
		noiseSegment = new NoiseSegmentMSD(analysisSegment, noiseMassSpectrum);
	}

	@Test
	public void testGetAnalysisSegment_1() {

		assertNotNull(noiseSegment.getAnalysisSegment());
	}

	@Test
	public void testGetAnalysisSegment_2() {

		assertEquals("Segment Width", 200, noiseSegment.getAnalysisSegment().getWidth());
	}

	@Test
	public void testGetNoiseMassSpectrum_1() {

		assertNotNull(noiseSegment.getNoiseMassSpectrum());
	}
}
