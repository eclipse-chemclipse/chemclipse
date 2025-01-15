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
 * Philip Wenig - refactoring vibrational spectroscopy
 *******************************************************************************/
package org.eclipse.chemclipse.vsd.converter.supplier.cml.test;

import java.io.File;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.vsd.converter.supplier.cml.PathResolver;
import org.eclipse.chemclipse.vsd.converter.supplier.cml.converter.ScanImportConverter;
import org.eclipse.chemclipse.vsd.converter.supplier.cml.model.IVendorSpectrumVSD;
import org.eclipse.chemclipse.vsd.model.core.ISpectrumVSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

import junit.framework.TestCase;

public class PCLANIL_ITest extends TestCase {

	private ISpectrumVSD spectrumVSD;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		File file = new File(PathResolver.getAbsolutePath(TestPathHelper.PCLANIL));
		ScanImportConverter importConverter = new ScanImportConverter();
		IProcessingInfo<ISpectrumVSD> processingInfo = importConverter.convert(file, new NullProgressMonitor());
		spectrumVSD = processingInfo.getProcessingResult();
	}

	@Override
	protected void tearDown() throws Exception {

		spectrumVSD = null;
		super.tearDown();
	}

	@Test
	public void testLoading() {

		assertNotNull(spectrumVSD);
		assertEquals("para-chloroaniline", spectrumVSD.getSampleName());
		assertEquals("IR_para-chlor", spectrumVSD.getDataName());
		assertEquals("PERKIN-ELMER 1000 FT-IR", spectrumVSD.getInstrument());
		if(spectrumVSD instanceof IVendorSpectrumVSD vendorSpectrumVSD) {
			assertEquals("C6H6ClN", vendorSpectrumVSD.getFormula());
			assertEquals("106-47-8", vendorSpectrumVSD.getCasNumber());
		}
	}

	@Test
	public void testSignals() {

		assertEquals(1801, spectrumVSD.getScanVSD().getProcessedSignals().size());
	}
}
