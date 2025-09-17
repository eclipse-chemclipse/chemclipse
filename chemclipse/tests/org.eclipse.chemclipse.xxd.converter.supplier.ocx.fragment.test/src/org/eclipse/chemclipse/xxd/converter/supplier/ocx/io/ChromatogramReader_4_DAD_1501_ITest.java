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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.chromatogram.ChromatogramConverterWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.TestPathHelper;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.versions.VersionConstants;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ChromatogramReader_4_DAD_1501_ITest {

	private static IChromatogramWSD chromatogram;

	@BeforeAll
	public void setUp() {

		PreferenceSupplier.setForceLoadAlternateDetector(true);
		File fileImport = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_4_DAD_1501));
		IProcessingInfo<IChromatogramWSD> processingInfo = ChromatogramConverterWSD.getInstance().convert(fileImport, VersionConstants.CONVERTER_ID_CHROMATOGRAM, new NullProgressMonitor());
		chromatogram = processingInfo.getProcessingResult();
	}

	@AfterAll
	public void tearDown() {

		PreferenceSupplier.setForceLoadAlternateDetector(false);
	}

	@Test
	public void testReader_1() {

		assertEquals(1443, chromatogram.getNumberOfScans());
	}

	@Test
	public void testReader_2() {

		assertEquals("Chromatogram4-1501", chromatogram.getName());
	}

	@Test
	public void testReader_3() {

		assertEquals(510325, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testReader_4() {

		assertEquals(1087125, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testReader_5() {

		assertEquals(3.54757344E8f, chromatogram.getMaxSignal(), 0);
	}

	@Test
	public void testReader_6() {

		assertEquals(4.2400872E7f, chromatogram.getMinSignal(), 0);
	}

	@Test
	public void testReader_7() {

		assertEquals(510325, chromatogram.getScanDelay());
	}

	@Test
	public void testReader_8() {

		assertEquals(400, chromatogram.getScanInterval());
	}

	@Test
	public void testReader_9() {

		IScanWSD scan = chromatogram.getScan(1);

		assertEquals(226, scan.getScanSignals().size());
		assertEquals(5.9459736E7f, scan.getTotalSignal(), 0);
	}

	@Test
	public void testReader_10() {

		assertEquals(4, chromatogram.getPeaks().size());
	}

	@Test
	public void testReader_11() {

		assertEquals(4.057491060101555E10d, chromatogram.getPeakIntegratedArea(), 0);
	}

	@Test
	public void testReader_12() {

		assertEquals(0.0d, chromatogram.getChromatogramIntegratedArea(), 0);
	}

	@Test
	public void testReader_13() {

		assertEquals(0.0d, chromatogram.getBackgroundIntegratedArea(), 0);
	}

	@Test
	public void testReader_14() {

		assertEquals(0.0d, chromatogram.getSampleWeight(), 0);
	}
}
