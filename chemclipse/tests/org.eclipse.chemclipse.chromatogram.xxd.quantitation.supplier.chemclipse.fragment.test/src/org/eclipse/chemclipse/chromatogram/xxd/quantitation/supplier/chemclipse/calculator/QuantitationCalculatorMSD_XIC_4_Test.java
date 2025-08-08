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
import org.eclipse.chemclipse.model.quantitation.IQuantitationSignals;
import org.junit.Before;
import org.junit.Test;

public class QuantitationCalculatorMSD_XIC_4_Test extends QuantitationCalculator_XIC_TestCase {

	/*
	 * UseTIC: false (-> m/z 103, 104)
	 * CalibrationMethod: LINEAR
	 * isZeroCrossing: false
	 */
	private List<IQuantitationEntry> quantitationEntries;
	private IQuantitationEntry quantitationEntry1;
	private IQuantitationEntry quantitationEntry2;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();

		IQuantitationCompound quantitationCompound = getQuantitationCompound();
		quantitationCompound.getQuantitationPeaks().addAll(getQuantitationPeaks());

		quantitationCompound.setUseTIC(false);
		quantitationCompound.setCalibrationMethod(CalibrationMethod.LINEAR);
		quantitationCompound.calculateSignalTablesFromPeaks();

		IQuantitationSignals quantitationSignals = quantitationCompound.getQuantitationSignals();
		quantitationSignals.deselectAllSignals();
		quantitationSignals.selectSignal(104.0d);
		quantitationSignals.selectSignal(103.0d);

		IQuantitationCalculatorMSD calculator = new QuantitationCalculatorMSD();
		quantitationCompound.setUseCrossZero(false);
		quantitationEntries = calculator.calculateQuantitationResults(getReferencePeakMSD_XIC_X(), quantitationCompound);

		quantitationEntry1 = quantitationEntries.get(0);
		quantitationEntry2 = quantitationEntries.get(1);
	}

	@Test
	public void testCalculateConcentration_1() {

		assertEquals(2, quantitationEntries.size());
	}

	/*
	 * Entry 1
	 */
	@Test
	public void testCalculateConcentration11() {

		assertNotNull(quantitationEntry1);
	}

	@Test
	public void testCalculateConcentration12() {

		assertEquals(0.020000000000000146d, quantitationEntry1.getConcentration(), 0);
	}

	@Test
	public void testCalculateConcentration13() {

		assertEquals(103.0d, quantitationEntry1.getSignal(), 0);
	}

	/*
	 * Entry 2
	 */
	@Test
	public void testCalculateConcentration21() {

		assertNotNull(quantitationEntry2);
	}

	@Test
	public void testCalculateConcentration22() {

		assertEquals(0.019999999999999737d, quantitationEntry2.getConcentration(), 0);
	}

	@Test
	public void testCalculateConcentration23() {

		assertEquals(104.0d, quantitationEntry2.getSignal(), 0);
	}

	/*
	 * Miscellaneous Entry 1
	 */
	@Test
	public void testCalculateConcentration1_1() {

		assertEquals("Styrene", quantitationEntry1.getName());
	}

	@Test
	public void testCalculateConcentration1_2() {

		assertEquals("Styrene-Butadiene", quantitationEntry1.getChemicalClass());
	}

	@Test
	public void testCalculateConcentration1_3() {

		assertEquals("mg/ml", quantitationEntry1.getConcentrationUnit());
	}

	@Test
	public void testCalculateConcentration1_4() {

		assertEquals("The integrated area '1.7E5' is < min response '5.0E5'.", quantitationEntry1.getDescription());
	}
}
