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

import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.internal.calculator.IQuantitationCalculatorMSD;
import org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.internal.calculator.QuantitationCalculatorMSD;
import org.eclipse.chemclipse.model.quantitation.CalibrationMethod;
import org.eclipse.chemclipse.model.quantitation.IQuantitationCompound;
import org.eclipse.chemclipse.model.quantitation.IQuantitationSignals;
import org.eclipse.chemclipse.model.quantitation.IResponseSignals;
import org.eclipse.chemclipse.model.quantitation.QuantitationSignal;
import org.eclipse.chemclipse.model.quantitation.ResponseSignal;
import org.eclipse.chemclipse.msd.model.exceptions.EvaluationException;
import org.junit.Before;
import org.junit.Test;

public class QuantitationCalculatorMSD_XIC_5_Test extends QuantitationCalculator_XIC_TestCase {

	/*
	 * UseTIC: false (-> m/z 180) -> does not exist
	 * CalibrationMethod: LINEAR
	 * isZeroCrossing: true
	 */
	private IQuantitationCalculatorMSD calculator;
	private IQuantitationCompound quantitationCompound;
	private IQuantitationSignals quantitationSignals;
	private IResponseSignals concentrationResponseEntries;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();

		quantitationCompound = getQuantitationCompound();
		quantitationCompound.setUseTIC(false);
		quantitationCompound.setCalibrationMethod(CalibrationMethod.LINEAR);
		quantitationSignals = quantitationCompound.getQuantitationSignals();
		concentrationResponseEntries = quantitationCompound.getResponseSignals();

		calculator = new QuantitationCalculatorMSD();
	}

	@Test
	public void testCalculateConcentration_1() {

		quantitationSignals.add(new QuantitationSignal(108.0d, 0.25f));
		concentrationResponseEntries.add(new ResponseSignal(180.0d, 0.3d, 59600.0d));
		quantitationCompound.setUseCrossZero(false);
		assertThrows(EvaluationException.class, () -> {
			calculator.calculateQuantitationResults(getReferencePeakMSD_XIC_X(), quantitationCompound);
		});
	}
}
