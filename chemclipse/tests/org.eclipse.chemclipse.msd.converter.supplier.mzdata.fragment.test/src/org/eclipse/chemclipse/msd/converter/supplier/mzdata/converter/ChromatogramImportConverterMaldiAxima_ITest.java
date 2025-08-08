/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.converter;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.mzdata.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.StandaloneMassSpectrum;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class ChromatogramImportConverterMaldiAxima_ITest {

	private IStandaloneMassSpectrum standaloneMassSpectrum;

	@Before
	public void setUp() throws Exception {

		File importFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_MALDI_AXIMA_CFR));
		MassSpectrumImportConverter converter = new MassSpectrumImportConverter();
		IProcessingInfo<IMassSpectra> processingInfo = converter.convert(importFile, new NullProgressMonitor());
		VendorMassSpectra massSpectra = (VendorMassSpectra)processingInfo.getProcessingResult();
		standaloneMassSpectrum = (StandaloneMassSpectrum)massSpectra.getMassSpectrum(1);
	}

	@Test
	public void testOperator() {

		assertEquals("Mike Ashton, Kratos Analytical Limited", standaloneMassSpectrum.getOperator());
	}

	@Test
	public void testInstrument() {

		assertEquals("AXIMA-CFR", standaloneMassSpectrum.getInstrument());
	}

	@Test
	public void testNumberOfIons() {

		assertEquals(134, standaloneMassSpectrum.getNumberOfIons());
	}
}
