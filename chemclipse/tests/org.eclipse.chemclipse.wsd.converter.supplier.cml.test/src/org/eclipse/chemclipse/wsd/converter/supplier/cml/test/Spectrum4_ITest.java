/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.converter.supplier.cml.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.supplier.cml.PathResolver;
import org.eclipse.chemclipse.wsd.converter.supplier.cml.converter.ScanImportConverter;
import org.eclipse.chemclipse.wsd.converter.supplier.cml.model.IVendorSpectrumWSD;
import org.eclipse.chemclipse.wsd.model.core.ISpectrumWSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class Spectrum4_ITest {

	private ISpectrumWSD spectrumWSD;

	@Before
	public void setUp() throws Exception {

		File file = new File(PathResolver.getAbsolutePath(TestPathHelper.SPECTRUM4));
		ScanImportConverter importConverter = new ScanImportConverter();
		IProcessingInfo<ISpectrumWSD> processingInfo = importConverter.convert(file, new NullProgressMonitor());
		spectrumWSD = processingInfo.getProcessingResult();
	}

	@Test
	public void testLoading() {

		assertNotNull(spectrumWSD);
		assertEquals("sp04", spectrumWSD.getDataName());
		if(spectrumWSD instanceof IVendorSpectrumWSD vendorSpectrumWSD) {
			assertEquals("109-99-9", vendorSpectrumWSD.getCasNumber());
		}
	}

	@Test
	public void testSignals() {

		assertEquals(1001, spectrumWSD.getSignals().size());
	}
}