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

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignals;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak exceptions.
 */
public class PeakBuilder_19_Test extends PeakBuilderTestCase {

	private IMarkedIons excludedIons;
	private ITotalScanSignals totalIonSignals;
	private ITotalScanSignalExtractor totalIonSignalExtractor;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		totalIonSignalExtractor = new TotalScanSignalExtractor(chromatogram);
		totalIonSignals = totalIonSignalExtractor.getTotalScanSignals(2, 16);
		excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
	}

	@Test
	public void testValidateTotalIonSignals_1() {

		PeakBuilderMSD.validateTotalIonSignals(totalIonSignals);
	}

	@Test
	public void testValidateTotalIonSignals_2() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.validateTotalIonSignals(null);
		});
	}

	@Test
	public void testValidateExcludedIons_1() {

		PeakBuilderMSD.validateExcludedIons(excludedIons);
	}

	@Test
	public void testValidateExcludedIons_2() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.validateExcludedIons(null);
		});
	}

	@Test
	public void testValidateChromatogram_1() {

		PeakBuilderMSD.validateChromatogram(chromatogram);
	}

	@Test
	public void testValidateChromatogram_2() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.validateChromatogram(null);
		});
	}

	@Test
	public void testValidateExtractedIonSignals_1() {

		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(20);
		PeakBuilderMSD.validateExtractedIonSignals(extractedIonSignals);
	}

	@Test
	public void testValidateExtractedIonSignals_2() {

		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.validateExtractedIonSignals(null);
		});
	}
}
