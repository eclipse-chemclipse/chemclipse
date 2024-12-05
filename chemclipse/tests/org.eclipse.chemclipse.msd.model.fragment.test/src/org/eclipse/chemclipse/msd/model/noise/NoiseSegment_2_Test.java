/*******************************************************************************
 * Copyright (c) 2010, 2024 Lablicate GmbH.
 * 
 * All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.noise;

import junit.framework.TestCase;

public class NoiseSegment_2_Test extends TestCase {

	private INoiseSegmentMSD noiseSegment;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		noiseSegment = new NoiseSegmentMSD(null, null);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void testGetAnalysisSegment_1() {

		assertNull(noiseSegment.getAnalysisSegment());
	}

	public void testGetNoiseMassSpectrum_1() {

		assertNull(noiseSegment.getNoiseMassSpectrum());
	}
}