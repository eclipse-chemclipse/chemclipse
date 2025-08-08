/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.rtshifter.core.internal.support;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.junit.Before;
import org.junit.Ignore;

/*
 * Chromatogram
 * Retention times:
 * Scan 1 1500
 * Scan 2 2500
 * Scan 3 3500
 * Scan 4 4500
 * Scan 5 5500
 * Scan 6 6500
 * Scan 7 7500
 * Scan 8 8500
 * Scan 9 9500
 * Scan 10 10500
 */
@Ignore
public class ChromatogramTestCase {

	private IChromatogramMSD chromatogram;

	@Before
	public void setUp() throws Exception {

		chromatogram = createChromatogram();
	}

	public IChromatogramMSD getChromatogram() {

		return chromatogram;
	}

	private IChromatogramMSD createChromatogram() {

		IChromatogramMSD chromatogram = new ChromatogramMSD();
		IRegularMassSpectrum scan;
		for(int i = 1; i <= 10; i++) {
			scan = new VendorMassSpectrum();
			chromatogram.addScan(scan);
		}
		chromatogram.setScanDelay(1500);
		chromatogram.setScanInterval(1000);
		chromatogram.recalculateRetentionTimes();
		return chromatogram;
	}
}
