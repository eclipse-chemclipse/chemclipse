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
public class ExtractedIonSignal_1_Test {

	/**
	 * Test setup with start and stop ion = 0.
	 */
	@Test
	public void testSetup_1() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(0, 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(-1), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(0), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(1), 0);
	}

	/**
	 * Test setup with start and stop ion = 1.
	 */
	@Test
	public void testSetup_2() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(0, 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(0), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(1), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(2), 0);
	}

	/**
	 * Test setup with start and stop ion = 1 and abundance 1000.
	 */
	@Test
	public void testSetup_3() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(1, 1);
		IIon ion = new Ion(1.0f, 1000.0f);
		extractedIonSignal.setAbundance(ion);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(0), 0);
		assertEquals("Abundance", 1000.0f, extractedIonSignal.getAbundance(1), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(2), 0);
	}

	/**
	 * Try to set negative values.
	 */
	@Test
	public void testSetup_4() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(-1, 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(0), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(1), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(2), 0);
	}

	/**
	 * Try to set negative values.
	 */
	@Test
	public void testSetup_5() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(0, -1);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(0), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(1), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(2), 0);
	}

	/**
	 * Try to set negative values.
	 */
	@Test
	public void testSetup_6() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(-2, -1);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(0), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(1), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(2), 0);
	}

	/**
	 * Test setup with start and stop ion 10-11<br/>
	 * ion = 10 and abundance 1000<br/>
	 * ion = 11 and abundance 2000
	 */
	@Test
	public void testSetup_7() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(10, 11);
		IIon ion = new Ion(10.0f, 1000.0f);
		extractedIonSignal.setAbundance(ion);
		ion = new Ion(11.0f, 2000.0f);
		extractedIonSignal.setAbundance(ion);
		assertEquals("Abundance", 1000.0f, extractedIonSignal.getAbundance(10), 0);
		assertEquals("Abundance", 2000.0f, extractedIonSignal.getAbundance(11), 0);
	}

	/**
	 * Test setup with start and stop ion 10<br/>
	 * ion = 10 and abundance 1000<br/>
	 */
	@Test
	public void testSetup_8() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(10, 11);
		IIon ion = new Ion(10.0f, 1000.0f);
		extractedIonSignal.setAbundance(ion);
		assertEquals("Abundance", 1000.0f, extractedIonSignal.getAbundance(10), 0);
		assertEquals("Abundance", 0.0f, extractedIonSignal.getAbundance(11), 0);
	}

	/**
	 * Test setup with start and stop ion 10-11<br/>
	 * ion = 10 and abundance 1000<br/>
	 * ion = 11 and abundance 2000 ion = 10 and abundance 1000 ion = 10 and
	 * abundance 1000 ion = 11 and abundance 2000
	 */
	@Test
	public void testSetup_9() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(10, 11);
		IIon ion = new Ion(10.0f, 1000.0f);
		extractedIonSignal.setAbundance(ion);
		extractedIonSignal.setAbundance(ion);
		extractedIonSignal.setAbundance(ion);
		ion = new Ion(11.0f, 2000.0f);
		extractedIonSignal.setAbundance(ion);
		extractedIonSignal.setAbundance(ion);
		assertEquals("Abundance", 3000.0f, extractedIonSignal.getAbundance(10), 0);
		assertEquals("Abundance", 4000.0f, extractedIonSignal.getAbundance(11), 0);
	}

	/**
	 * Test setup with start and stop ion 10.4<br/>
	 * ion = 10 and abundance 1000
	 */
	@Test
	public void testSetup_10() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(10.4f, 10.4f);
		IIon ion = new Ion(10.4f, 1000.0f);
		extractedIonSignal.setAbundance(ion);
		assertEquals("Abundance", 1000.0f, extractedIonSignal.getAbundance(10), 0);
	}

	/**
	 * Test setup with start and stop ion 10.5<br/>
	 * ion = 11 and abundance 1000
	 */
	@Test
	public void testSetup_11() {

		IExtractedIonSignal extractedIonSignal = new ExtractedIonSignal(10.5f, 10.5f);
		IIon ion = new Ion(10.5f, 1000.0f);
		extractedIonSignal.setAbundance(ion);
		assertEquals("Abundance", 1000.0f, extractedIonSignal.getAbundance(11), 0);
	}
}
