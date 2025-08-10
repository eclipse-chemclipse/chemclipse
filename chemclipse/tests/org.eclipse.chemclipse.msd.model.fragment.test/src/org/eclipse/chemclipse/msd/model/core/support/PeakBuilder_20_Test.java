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
package org.eclipse.chemclipse.msd.model.core.support;

import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignals;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.junit.Test;

/**
 * Test the peak exceptions.
 */
public class PeakBuilder_20_Test {

	private IExtractedIonSignals extractedIonSignals;
	private IScanRange scanRange;

	@Test
	public void testCheckScanRange_1() {

		extractedIonSignals = new ExtractedIonSignals(10, 20);
		scanRange = new ScanRange(10, 20);
		PeakBuilderMSD.checkScanRange(extractedIonSignals, scanRange);
	}

	@Test
	public void testCheckScanRange_2() {

		extractedIonSignals = new ExtractedIonSignals(12, 20);
		scanRange = new ScanRange(10, 20);
		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.checkScanRange(extractedIonSignals, scanRange);
		});
	}

	@Test
	public void testCheckScanRange_3() {

		extractedIonSignals = new ExtractedIonSignals(10, 20);
		scanRange = new ScanRange(10, 23);
		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.checkScanRange(extractedIonSignals, scanRange);
		});
	}
}
