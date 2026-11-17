/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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

import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.IVendorChromatogramMSD;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.VendorScanMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.RetentionTimeSupport;

public class XmlChromatogramReader {

	private XmlChromatogramReader() {

	}

	public static void addTotalSignals(double[] intensities, double[] retentionTimes, IVendorChromatogramMSD chromatogram) {

		int tic = Math.min(retentionTimes.length, intensities.length);
		for(int i = 0; i < tic; i++) {
			VendorScanMSD scan = new VendorScanMSD();
			int retentionTime = RetentionTimeSupport.toMilliseconds(retentionTimes[i]);
			scan.setRetentionTime(retentionTime);
			float intensity = (float)intensities[i];
			VendorIon ion = new VendorIon(IIon.TIC_ION, intensity);
			scan.addIon(ion, false);
			chromatogram.addScan(scan);
		}
	}
}
