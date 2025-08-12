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
package org.eclipse.chemclipse.msd.model.xic;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.junit.Test;

/**
 * Tests the class ExtractedIonSignal.
 */
public class ExtractedIonSignal_4_Test {

	/**
	 * Test setup with start and stop ion = 1 and abundance 1000.
	 */
	@Test
	public void testSetup_1() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(1, 1);
		IIon ion = new Ion(1.0f, 1000.0f);
		extractedIonSignal.setAbundance(ion);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(0), 0);
		assertEquals("Abundance", 1000.0f, extractedIonSignal.getAbundance(1), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(2), 0);
	}

	/**
	 * Test setup with start and stop ion = 1 and abundance 1000.
	 */
	@Test
	public void testSetup_2() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(1, 1);
		IIon ion = new Ion(1.0f, 1000.0f);
		extractedIonSignal.setAbundance(ion);
		extractedIonSignal.setAbundance(0, 2586.4f, true);
		extractedIonSignal.setAbundance(1, 2586.4f, true);
		extractedIonSignal.setAbundance(2, 2586.4f, true);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(0), 0);
		assertEquals("Abundance", 2586.4f, extractedIonSignal.getAbundance(1), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(2), 0);
	}
}
