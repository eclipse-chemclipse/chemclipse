/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzml.io;

import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.VendorIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.IonTransition;

public class XmlMassSpectrumReader {

	private XmlMassSpectrumReader() {

	}

	public static void addIons(double[] intensities, double[] mzs, IScanMSD massSpectrum) {

		int ions = Math.min(mzs.length, intensities.length);
		for(int i = 0; i < ions; i++) {
			massSpectrum.addIon(new VendorIon(mzs[i], (float)intensities[i]), false);
		}
	}

	public static void addIons(double[] intensities, double[] mzs, IScanMSD massSpectrum, double selectedIon, double collisionEnergy) {

		int ions = Math.min(mzs.length, intensities.length);
		for(int i = 0; i < ions; i++) {
			IIonTransition ionTransition = new IonTransition(selectedIon, mzs[i], collisionEnergy, 1, 1, 0);
			massSpectrum.addIon(new VendorIonMSn(mzs[i], (float)intensities[i], ionTransition), false);
		}
	}
}
