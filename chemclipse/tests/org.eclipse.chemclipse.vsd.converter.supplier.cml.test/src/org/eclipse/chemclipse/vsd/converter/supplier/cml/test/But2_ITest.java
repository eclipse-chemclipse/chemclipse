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
package org.eclipse.chemclipse.vsd.converter.supplier.cml.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.vsd.converter.supplier.cml.PathResolver;
import org.eclipse.chemclipse.vsd.converter.supplier.cml.converter.ScanImportConverter;
import org.eclipse.chemclipse.vsd.converter.supplier.cml.model.IVendorSpectrumVSD;
import org.eclipse.chemclipse.vsd.model.core.ISpectrumVSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class But2_ITest {

	private ISpectrumVSD spectrumVSD;

	@Before
	public void setUp() throws Exception {

		File file = new File(PathResolver.getAbsolutePath(TestPathHelper.BUT2));
		ScanImportConverter importConverter = new ScanImportConverter();
		IProcessingInfo<ISpectrumVSD> processingInfo = importConverter.convert(file, new NullProgressMonitor());
		spectrumVSD = processingInfo.getProcessingResult();
	}

	@Test
	public void testLoading() {

		assertNotNull(spectrumVSD);
		assertEquals("2-Butanol", spectrumVSD.getSampleName());
		assertEquals("but2", spectrumVSD.getDataName());
		if(spectrumVSD instanceof IVendorSpectrumVSD vendorSpectrumVSD) {
			assertEquals("C 4 H 10 O 1", vendorSpectrumVSD.getFormula());
			assertEquals("78-92-2", vendorSpectrumVSD.getCasNumber());
		}
	}

	@Test
	public void testSignals() {

		assertEquals(224, spectrumVSD.getScanVSD().getProcessedSignals().size());
	}
}
