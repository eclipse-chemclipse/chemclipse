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

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NoiseSegment_2_Test {

	private INoiseSegmentMSD noiseSegment = new NoiseSegmentMSD(null, null);

	@Test
	public void testGetAnalysisSegment_1() {

		assertNull(noiseSegment.getAnalysisSegment());
	}

	@Test
	public void testGetNoiseMassSpectrum_1() {

		assertNull(noiseSegment.getNoiseMassSpectrum());
	}
}