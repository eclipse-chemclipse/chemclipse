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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.quantitation.IQuantitationCompound;
import org.eclipse.chemclipse.model.quantitation.IQuantitationPeak;
import org.eclipse.chemclipse.model.quantitation.IQuantitationSignal;
import org.eclipse.chemclipse.model.quantitation.IQuantitationSignals;
import org.eclipse.chemclipse.model.quantitation.IResponseSignal;
import org.eclipse.chemclipse.model.quantitation.IResponseSignals;
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.msd.model.implementation.QuantitationPeakMSD;
import org.junit.Before;
import org.junit.Test;

public class QuantitationCompound_3_Test extends ReferencePeakMSDTestCase {

	private IQuantitationCompound quantitationCompound;
	private IQuantitationSignals quantitationSignals;
	private IResponseSignals concentrationResponseEntries;

	@Override
	@Before
	public void setUp() {

		super.setUp();
		quantitationCompound = new QuantitationCompound("Styrene", "mg/ml", 5500);

		List<IQuantitationPeak> quantitationPeaks = new ArrayList<IQuantitationPeak>();
		IQuantitationPeak quantitationPeak = new QuantitationPeakMSD(getReferencePeakMSD_TIC_1(), 0.1d, "mg/ml");
		quantitationPeaks.add(quantitationPeak);
		quantitationCompound.getQuantitationPeaks().addAll(quantitationPeaks);

		quantitationCompound.setUseTIC(true);
		quantitationCompound.calculateSignalTablesFromPeaks();

		quantitationSignals = quantitationCompound.getQuantitationSignals();
		concentrationResponseEntries = quantitationCompound.getResponseSignals();
	}

	@Test
	public void testGetQuantitationSignals_1() {

		assertNotNull(quantitationSignals);
	}

	@Test
	public void testGetQuantitationSignals_2() {

		assertEquals(1, quantitationSignals.size());
	}

	@Test
	public void testGetQuantitationSignals_3() {

		IQuantitationSignal quantitationSignal = quantitationSignals.first();
		assertEquals(AbstractIon.TIC_ION, quantitationSignal.getSignal(), 0);
		assertEquals(IQuantitationSignal.ABSOLUTE_RELATIVE_RESPONSE, quantitationSignal.getRelativeResponse(), 0);
		assertEquals(0.0d, quantitationSignal.getUncertainty(), 0);
		assertTrue(quantitationSignal.isUse());
	}

	@Test
	public void testGetConcentrationResponseEntries_1() {

		assertNotNull(concentrationResponseEntries);
	}

	@Test
	public void testGetConcentrationResponseEntries_2() {

		assertEquals(1, concentrationResponseEntries.size());
	}

	@Test
	public void testGetConcentrationResponseEntries_3() {

		IResponseSignal concentrationResponseEntry = concentrationResponseEntries.get(0);
		assertEquals(AbstractIon.TIC_ION, concentrationResponseEntry.getSignal(), 0);
		assertEquals(0.1d, concentrationResponseEntry.getConcentration(), 0);
		assertEquals(750220.0d, concentrationResponseEntry.getResponse(), 0);
	}
}
