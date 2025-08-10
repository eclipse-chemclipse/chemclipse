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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.junit.Test;

public class ExtractedIonSignals_3_Test {

	private IChromatogramMSD chromatogram = new ChromatogramMSD();

	@Test
	public void testConstructor_1() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(10, chromatogram);
		assertNotNull("getChromatogram", extractedIonSignals.getChromatogram());
	}

	@Test
	public void testConstructor_2() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(10, null);
		assertNull("getChromatogram", extractedIonSignals.getChromatogram());
	}

	@Test
	public void testConstructor_3() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(20, 40, chromatogram);
		assertNotNull("getChromatogram", extractedIonSignals.getChromatogram());
	}

	@Test
	public void testConstructor_4() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(20, 40, null);
		assertNull("getChromatogram", extractedIonSignals.getChromatogram());
	}
}
