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
package org.eclipse.chemclipse.xxd.model.quantitation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.chemclipse.model.quantitation.CalibrationMethod;
import org.eclipse.chemclipse.model.quantitation.IQuantitationCompound;
import org.eclipse.chemclipse.model.quantitation.IQuantitationSignals;
import org.eclipse.chemclipse.model.quantitation.IResponseSignals;
import org.eclipse.chemclipse.model.quantitation.IRetentionIndexWindow;
import org.eclipse.chemclipse.model.quantitation.IRetentionTimeWindow;
import org.junit.Before;
import org.junit.Test;

public class QuantitationCompound_1_Test {

	private IQuantitationCompound quantitationCompound;

	@Before
	public void setUp() {

		quantitationCompound = new QuantitationCompound("Styrene", "mg/ml", 5500);
	}

	@Test
	public void testGetCalibrationMethod_1() {

		assertEquals(CalibrationMethod.LINEAR, quantitationCompound.getCalibrationMethod());
	}

	@Test
	public void testGetChemicalClass_1() {

		assertEquals("", quantitationCompound.getChemicalClass());
	}

	@Test
	public void testGetConcentrationResponseEntries_1() {

		assertNotNull(quantitationCompound.getResponseSignals());
	}

	@Test
	public void testGetConcentrationResponseEntries_2() {

		IResponseSignals entries = quantitationCompound.getResponseSignals();
		assertEquals(0, entries.size());
	}

	@Test
	public void testGetConcentrationUnit_1() {

		assertEquals("mg/ml", quantitationCompound.getConcentrationUnit());
	}

	@Test
	public void testGetName_1() {

		assertEquals("Styrene", quantitationCompound.getName());
	}

	@Test
	public void testGetQuantitationSignals_1() {

		assertNotNull(quantitationCompound.getQuantitationSignals());
	}

	@Test
	public void testGetQuantitationSignals_2() {

		IQuantitationSignals entries = quantitationCompound.getQuantitationSignals();
		assertEquals(0, entries.size()); // Default TIC, 100
	}

	@Test
	public void testGetRetentionIndexWindow_1() {

		assertNotNull(quantitationCompound.getRetentionIndexWindow());
	}

	@Test
	public void testGetRetentionIndexWindow_2() {

		IRetentionIndexWindow retentionIndexWindow = quantitationCompound.getRetentionIndexWindow();
		assertEquals(0.0f, retentionIndexWindow.getRetentionIndex(), 0);
	}

	@Test
	public void testGetRetentionTimeWindow_1() {

		assertNotNull(quantitationCompound.getRetentionTimeWindow());
	}

	@Test
	public void testGetRetentionTimeWindow_2() {

		IRetentionTimeWindow retentionTimeWindow = quantitationCompound.getRetentionTimeWindow();
		assertEquals(5500, retentionTimeWindow.getRetentionTime());
	}
}
