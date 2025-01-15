/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.cml.test;

import java.io.File;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.supplier.cml.PathResolver;
import org.eclipse.chemclipse.wsd.converter.supplier.cml.converter.ScanImportConverter;
import org.eclipse.chemclipse.wsd.converter.supplier.cml.model.IVendorSpectrumWSD;
import org.eclipse.chemclipse.wsd.model.core.ISpectrumWSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

import junit.framework.TestCase;

public class Spectrum4_ITest extends TestCase {

	private ISpectrumWSD spectrumWSD;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		File file = new File(PathResolver.getAbsolutePath(TestPathHelper.SPECTRUM4));
		ScanImportConverter importConverter = new ScanImportConverter();
		IProcessingInfo<ISpectrumWSD> processingInfo = importConverter.convert(file, new NullProgressMonitor());
		spectrumWSD = processingInfo.getProcessingResult();
	}

	@Override
	protected void tearDown() throws Exception {

		spectrumWSD = null;
		super.tearDown();
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