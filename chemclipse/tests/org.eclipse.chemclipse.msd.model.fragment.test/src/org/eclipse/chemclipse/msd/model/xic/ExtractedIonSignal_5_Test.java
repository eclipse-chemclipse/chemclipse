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
package org.eclipse.chemclipse.msd.model.xic;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ExtractedIonSignal_5_Test {

	private IExtractedIonSignal extractedIonSignal;

	@Before
	public void setUp() {

		extractedIonSignal = new ExtractedIonSignal(18, 28);
		extractedIonSignal.setAbundance(18, 5689.8f);
		extractedIonSignal.setAbundance(19, 829.83f);
		extractedIonSignal.setAbundance(20, 893.2f);
		extractedIonSignal.setAbundance(21, 113.9f);
		extractedIonSignal.setAbundance(22, 389.2f);
		extractedIonSignal.setAbundance(23, 298.6f);
		extractedIonSignal.setAbundance(24, 8938.2f);
		extractedIonSignal.setAbundance(25, 192.1f);
		extractedIonSignal.setAbundance(26, 2788.89f);
		extractedIonSignal.setAbundance(27, 829.1f);
		extractedIonSignal.setAbundance(28, 568.89f);
	}

	@Test
	public void testSize_1() {

		assertEquals(11, extractedIonSignal.getNumberOfIonValues());
	}

	@Test
	public void testSize_2() {

		assertEquals(21531.71f, extractedIonSignal.getTotalSignal(), 0);
	}

	@Test
	public void testSize_3() {

		assertEquals(113.9f, extractedIonSignal.getMinIntensity(), 0);
	}

	@Test
	public void testSize_4() {

		assertEquals(8938.2f, extractedIonSignal.getMaxIntensity(), 0);
	}

	@Test
	public void testSize_5() {

		assertEquals(829.1f, extractedIonSignal.getMedianIntensity(), 0);
	}

	@Test
	public void testSize_6() {

		assertEquals(24, extractedIonSignal.getIonMaxIntensity(), 0);
	}
}
