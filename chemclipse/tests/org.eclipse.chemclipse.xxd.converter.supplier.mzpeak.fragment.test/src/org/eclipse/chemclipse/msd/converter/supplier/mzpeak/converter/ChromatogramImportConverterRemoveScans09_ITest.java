/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Aleksandar Kurtakov - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzpeak.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.Test;

/**
 * Scans that are not loaded yet must read their own spectrum after the first
 * 10 scans have been removed. The first remaining scan was scan 11 with 941 ions.
 */
public class ChromatogramImportConverterRemoveScans09_ITest {

	@Test
	public void testLoadScan() {

		IChromatogramMSD chromatogram = importAndRemoveScans();
		assertEquals(941, chromatogram.getScan(1).getIons().size());
	}

	@Test
	public void testLoadScans() {

		IChromatogramMSD chromatogram = importAndRemoveScans();
		chromatogram.enforceLoadScanProxies(new NullProgressMonitor());
		assertEquals(941, chromatogram.getScan(1).getIons().size());
	}

	private static IChromatogramMSD importAndRemoveScans() {

		ChromatogramImportConverter converter = new ChromatogramImportConverter();
		IChromatogramMSD chromatogram = converter.convert(new File("testData/small.mzpeak"), new NullProgressMonitor()).getProcessingResult();
		List<IScan> scans = chromatogram.getScans();
		chromatogram.replaceAllScans(new ArrayList<>(scans.subList(10, scans.size())));
		return chromatogram;
	}
}
