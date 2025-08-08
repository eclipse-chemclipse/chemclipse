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
import org.eclipse.chemclipse.model.quantitation.CalibrationMethod;
import org.eclipse.chemclipse.model.quantitation.IQuantitationCompound;
import org.eclipse.chemclipse.model.quantitation.IQuantitationEntry;
import org.junit.Before;
import org.junit.Test;

public class QuantitationCalculatorMSD_XIC_1_Test extends QuantitationCalculator_XIC_TestCase {

	/*
	 * UseTIC: false
	 * CalibrationMethod: LINEAR
	 * isZeroCrossing: true
	 */
	private List<IQuantitationEntry> quantitationEntries;
	private IQuantitationEntry quantitationEntry;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();

		IQuantitationCompound quantitationCompound = getQuantitationCompound();
		quantitationCompound.getQuantitationPeaks().addAll(getQuantitationPeaks());

		quantitationCompound.setUseTIC(false);
		quantitationCompound.setCalibrationMethod(CalibrationMethod.LINEAR);
		quantitationCompound.calculateSignalTablesFromPeaks();

		IQuantitationCalculatorMSD calculator = new QuantitationCalculatorMSD();
		quantitationCompound.setUseCrossZero(true);
		quantitationEntries = calculator.calculateQuantitationResults(getReferencePeakMSD_XIC_X(), quantitationCompound);
		quantitationEntry = quantitationEntries.get(0);
	}

	@Test
	public void testCalculateConcentration_1() {

		assertEquals(8, quantitationEntries.size());
	}

	@Test
	public void testCalculateConcentration0_1() {

		quantitationEntry = quantitationEntries.get(0);
		assertEquals(0.020000000000000004d, quantitationEntry.getConcentration(), 0);
	}

	@Test
	public void testCalculateConcentration0_2() {

		quantitationEntry = quantitationEntries.get(0);
		assertEquals(50.0d, quantitationEntry.getSignal(), 0);
	}

	@Test
	public void testCalculateConcentration1_1() {

		quantitationEntry = quantitationEntries.get(6);
		assertEquals(0.01999999999999998d, quantitationEntry.getConcentration(), 0);
	}

	@Test
	public void testCalculateConcentration1_2() {

		quantitationEntry = quantitationEntries.get(6);
		assertEquals(104.0d, quantitationEntry.getSignal(), 0);
	}

	@Test
	public void testCalculateConcentration_2() {

		assertNotNull(quantitationEntry);
	}

	@Test
	public void testCalculateConcentration_3() {

		assertEquals("Styrene", quantitationEntry.getName());
	}

	@Test
	public void testCalculateConcentration_4() {

		assertEquals("Styrene-Butadiene", quantitationEntry.getChemicalClass());
	}

	@Test
	public void testCalculateConcentration_6() {

		assertEquals("mg/ml", quantitationEntry.getConcentrationUnit());
	}

	@Test
	public void testCalculateConcentration_7() {

		assertEquals("The integrated area '1.4E5' is < min response '4.1E5'.", quantitationEntry.getDescription());
	}
}
