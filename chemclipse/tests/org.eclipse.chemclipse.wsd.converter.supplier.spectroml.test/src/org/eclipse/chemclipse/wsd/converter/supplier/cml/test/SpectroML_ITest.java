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
import java.time.ZoneId;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.supplier.spectroml.PathResolver;
import org.eclipse.chemclipse.wsd.converter.supplier.spectroml.converter.ScanImportConverter;
import org.eclipse.chemclipse.wsd.converter.supplier.spectroml.model.IVendorSpectrumWSD;
import org.eclipse.chemclipse.wsd.model.core.ISpectrumWSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

import junit.framework.TestCase;

public class SpectroML_ITest extends TestCase {

	private ISpectrumWSD spectrumWSD;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		File file = new File(PathResolver.getAbsolutePath(TestPathHelper.SAMPLE));
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
	}

	@Test
	public void testMetadata() {

		assertEquals("sample experiment", spectrumWSD.getDataName());
		assertEquals("simple measurement of drinking water", spectrumWSD.getDetailedInfo());
		assertEquals("HP 8453", spectrumWSD.getInstrument());
		assertEquals(2000, spectrumWSD.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().getYear());
		assertEquals("Paul DeRose", spectrumWSD.getOperator());
		assertEquals("1063546374", spectrumWSD.getBarcode());
		assertEquals("water", spectrumWSD.getSampleName());
		if(spectrumWSD instanceof IVendorSpectrumWSD vendorSpectrumWSD) {
			assertEquals("7732-18-5", vendorSpectrumWSD.getCasNumber());
			assertEquals("H2O", vendorSpectrumWSD.getFormula());
		}
	}

	@Test
	public void testSignals() {

		assertEquals(3, spectrumWSD.getSignals().size());
	}
}