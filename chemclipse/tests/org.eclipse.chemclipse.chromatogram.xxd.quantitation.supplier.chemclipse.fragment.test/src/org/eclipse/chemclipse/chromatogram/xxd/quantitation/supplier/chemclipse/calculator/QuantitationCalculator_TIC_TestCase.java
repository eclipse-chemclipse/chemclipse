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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.quantitation.IQuantitationCompound;
import org.eclipse.chemclipse.model.quantitation.IQuantitationPeak;
import org.eclipse.chemclipse.msd.model.implementation.QuantitationPeakMSD;
import org.eclipse.chemclipse.xxd.model.quantitation.QuantitationCompound;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class QuantitationCalculator_TIC_TestCase extends ReferencePeakMSDTestCase {

	private IQuantitationCompound quantitationCompound;
	private List<IQuantitationPeak> quantitationPeaks;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		quantitationCompound = new QuantitationCompound("Styrene", "mg/ml", 5500);
		quantitationCompound.setChemicalClass("Styrene-Butadiene");

		quantitationPeaks = new ArrayList<IQuantitationPeak>();
		IQuantitationPeak quantitationPeak1 = new QuantitationPeakMSD(getReferencePeakMSD_TIC_1(), 0.01d, "mg/ml");
		quantitationPeaks.add(quantitationPeak1);
		IQuantitationPeak quantitationPeak2 = new QuantitationPeakMSD(getReferencePeakMSD_TIC_2(), 0.05d, "mg/ml");
		quantitationPeaks.add(quantitationPeak2);
		IQuantitationPeak quantitationPeak3 = new QuantitationPeakMSD(getReferencePeakMSD_TIC_3(), 0.1d, "mg/ml");
		quantitationPeaks.add(quantitationPeak3);
	}

	public IQuantitationCompound getQuantitationCompound() {

		return quantitationCompound;
	}

	public List<IQuantitationPeak> getQuantitationPeaks() {

		return quantitationPeaks;
	}
}
