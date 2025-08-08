/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.io;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.TestPathHelper;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.model.IPeakIdentificationBatchJob;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.model.IPeakIdentificationEntry;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.model.IPeakInputEntry;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.model.IPeakIntegrationEntry;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class PeakIdentificationBatchJobReader_1_ITest {

	private IPeakIdentificationBatchJobReader reader = new PeakIdentificationBatchJobReader();
	private IPeakIdentificationBatchJob batchProcessJob;
	private IPeakInputEntry inputEntry;
	private IPeakIntegrationEntry integrationEntry;
	private IPeakIdentificationEntry identificationEntry;

	@Before
	public void setUp() throws Exception {

		File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_BATCH_PROCESS_JOB));
		batchProcessJob = reader.read(file, new NullProgressMonitor());
	}

	/*
	 * HEADER
	 */
	@Test
	public void testGetReportFolder_1() {

		assertEquals("/org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.fragment.test/testData/files/import", batchProcessJob.getReportFolder());
	}

	@Test
	public void testIsOverrideReport_1() {

		assertEquals(false, batchProcessJob.isOverrideReport());
	}

	/*
	 * INPUT
	 */
	@Test
	public void testGetInputEntries_1() {

		assertEquals(131, batchProcessJob.getPeakInputEntries().size());
	}

	@Test
	public void testGetInputEntries_2() {

		String inputFile = batchProcessJob.getPeakInputEntries().get(0).getInputFile();
		assertEquals("/org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.fragment.test/testData/files/import/snip40_P1.mpl", inputFile);
	}

	@Test
	public void testGetInputEntries_3() {

		inputEntry = batchProcessJob.getPeakInputEntries().get(0);
		assertEquals("/org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.fragment.test/testData/files/import/snip40_P1.mpl", inputEntry.getInputFile());
	}

	/*
	 * PROCESS
	 */
	@Test
	public void testGetIntegrationEntry_1() {

		integrationEntry = batchProcessJob.getPeakIntegrationEntry();
		assertEquals("org.eclipse.chemclipse.chromatogram.msd.integrator.supplier.chemstation.peakIntegrator", integrationEntry.getProcessorId());
	}

	@Test
	public void testGetIdentificationEntry_1() {

		identificationEntry = batchProcessJob.getPeakIdentificationEntry();
		assertEquals("org.eclipse.chemclipse.chromatogram.msd.identifier.supplier.nist.peak", identificationEntry.getProcessorId());
	}

	/*
	 * OUTPUT
	 */
	@Test
	public void testGetOutputEntries_1() {

		assertEquals(3, batchProcessJob.getPeakOutputEntries().size());
	}

	@Test
	public void testGetOutputEntries_2() {

		String inputFile = batchProcessJob.getPeakOutputEntries().get(0).getOutputFolder();
		assertEquals("/org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.fragment.test/testData/files/import", inputFile);
	}

	@Test
	public void testGetOutputEntries_3() {

		String converterId = batchProcessJob.getPeakOutputEntries().get(0).getConverterId();
		assertEquals("org.eclipse.chemclipse.chromatogram.msd.converter.supplier.matlab.parafac", converterId);
	}
}
