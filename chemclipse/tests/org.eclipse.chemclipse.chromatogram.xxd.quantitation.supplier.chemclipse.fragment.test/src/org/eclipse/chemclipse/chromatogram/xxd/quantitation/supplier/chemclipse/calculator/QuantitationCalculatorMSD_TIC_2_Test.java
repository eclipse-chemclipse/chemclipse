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
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.internal.calculator.IQuantitationCalculatorMSD;
import org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.internal.calculator.QuantitationCalculatorMSD;
import org.eclipse.chemclipse.model.core.ISignal;
import org.eclipse.chemclipse.model.quantitation.CalibrationMethod;
import org.eclipse.chemclipse.model.quantitation.IQuantitationCompound;
import org.eclipse.chemclipse.model.quantitation.IQuantitationEntry;
import org.junit.Before;

public class QuantitationCalculatorMSD_TIC_2_Test extends QuantitationCalculator_TIC_TestCase {

	/*
	 * UseTIC: true
	 * CalibrationMethod: LINEAR
	 * isZeroCrossing: false
	 */
	private List<IQuantitationEntry> quantitationEntries;
	private IQuantitationEntry quantitationEntry;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();

		IQuantitationCompound quantitationCompound = getQuantitationCompound();
		quantitationCompound.getQuantitationPeaks().addAll(getQuantitationPeaks());

		quantitationCompound.setUseTIC(true);
		quantitationCompound.setCalibrationMethod(CalibrationMethod.LINEAR);
		quantitationCompound.calculateSignalTablesFromPeaks();

		IQuantitationCalculatorMSD calculator = new QuantitationCalculatorMSD();
		quantitationCompound.setUseCrossZero(false);
		quantitationEntries = calculator.calculateQuantitationResults(getReferencePeakMSD_TIC_X(), quantitationCompound);
		quantitationEntry = quantitationEntries.get(0);
	}

	public void testCalculateConcentration_1() {

		assertEquals(1, quantitationEntries.size());
	}

	public void testCalculateConcentration_2() {

		assertNotNull(quantitationEntry);
	}

	public void testCalculateConcentration_3() {

		assertEquals("Styrene", quantitationEntry.getName());
	}

	public void testCalculateConcentration_4() {

		assertEquals("Styrene-Butadiene", quantitationEntry.getChemicalClass());
	}

	public void testCalculateConcentration_5() {

		assertEquals(0.029999999999999992d, quantitationEntry.getConcentration(), 0);
	}

	public void testCalculateConcentration_6() {

		assertEquals("mg/ml", quantitationEntry.getConcentrationUnit());
	}

	public void testCalculateConcentration_7() {

		assertEquals("", quantitationEntry.getDescription());
	}

	public void testCalculateConcentration_8() {

		assertEquals(ISignal.TOTAL_INTENSITY, quantitationEntry.getSignal(), 0);
	}
}
