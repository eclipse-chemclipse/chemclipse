/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.calculator;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.model.quantitation.CalibrationMethod;
import org.eclipse.chemclipse.model.quantitation.IQuantitationCompound;
import org.eclipse.chemclipse.model.quantitation.IQuantitationPeak;
import org.eclipse.chemclipse.model.quantitation.IQuantitationSignals;
import org.eclipse.chemclipse.model.quantitation.IResponseSignals;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.junit.Before;
import org.junit.Test;

public class QuantitationCalculatorMSD_XIC_6_Test extends QuantitationCalculator_XIC_TestCase {

	/*
	 * UseTIC: false (-> m/z 108) -> does not exist
	 * CalibrationMethod: LINEAR
	 * isZeroCrossing: false
	 */
	private IQuantitationCompound quantitationCompound;
	private IQuantitationSignals quantitationSignals;
	private IResponseSignals concentrationResponseEntries;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();

		quantitationCompound = getQuantitationCompound();
		List<IQuantitationPeak> quantitationPeaks = getQuantitationPeaks();
		for(IQuantitationPeak peak : quantitationPeaks) {
			IPeakModel peakModel = peak.getReferencePeak().getPeakModel();
			if(peakModel instanceof IPeakModelMSD peakModelMSD) {
				peakModelMSD.getPeakMassSpectrum().addIon(new Ion(108.0, 54903.5f));
			}
		}

		quantitationCompound.setUseTIC(false);
		quantitationCompound.setCalibrationMethod(CalibrationMethod.LINEAR);
		quantitationCompound.calculateSignalTablesFromPeaks();

		quantitationSignals = quantitationCompound.getQuantitationSignals();
		concentrationResponseEntries = quantitationCompound.getResponseSignals();
	}

	@Test
	public void testSize_1() {

		assertEquals(0, quantitationSignals.size()); // Default TIC, 100
	}

	@Test
	public void testSize_2() {

		assertEquals(0, concentrationResponseEntries.size());
	}
}
