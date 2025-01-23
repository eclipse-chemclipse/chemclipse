/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.converter;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.TestPathHelper;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;

import junit.framework.TestCase;

public class MatlabParafacPeakExportConverter_1_ITest extends TestCase {

	private IPeaksMSD peaks;
	private IPeakMSD peak;
	private IExtractedIonSignal extractedIonSignal;
	private IPeakModelMSD peakModel;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		/*
		 * Import
		 */
		MatlabParafacPeakImportConverter importConverter = new MatlabParafacPeakImportConverter();
		File importFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_MATLAB_PEAKS));
		IProcessingInfo<IPeaksMSD> importProcessingInfo = importConverter.convert(importFile, new NullProgressMonitor());
		peaks = importProcessingInfo.getProcessingResult();
		/*
		 * Export
		 */
		MatlabParafacPeakExportConverter exportConverter = new MatlabParafacPeakExportConverter();
		File exportFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_EXPORT_FOLDER) + File.separator + TestPathHelper.TESTFILE_EXPORT_MATLAB_PEAKS);
		IProcessingInfo<File> exportProcessingInfo = exportConverter.convert(exportFile, peaks, false, new NullProgressMonitor());
		exportFile = exportProcessingInfo.getProcessingResult();
		/*
		 * Re-Import
		 */
		importConverter = new MatlabParafacPeakImportConverter();
		File reimportFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_EXPORT_FOLDER) + File.separator + TestPathHelper.TESTFILE_EXPORT_MATLAB_PEAKS);
		importProcessingInfo = importConverter.convert(reimportFile, new NullProgressMonitor());
		peaks = importProcessingInfo.getProcessingResult();
		peak = peaks.getPeaks().get(1);
		IPeakMassSpectrum peakMassSpectrum = peak.getExtractedMassSpectrum();
		extractedIonSignal = peakMassSpectrum.getExtractedIonSignal();
		peakModel = peak.getPeakModel();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void testPeaks_1() {

		assertEquals(3, peaks.getPeaks().size());
	}

	public void testPeaks_2() {

		assertEquals("PARAFAC model, 257820 to 276016 milliseconds, Peak 2", peak.getModelDescription());
	}

	public void testPeaks_3() {

		assertEquals(50, extractedIonSignal.getStartIon());
	}

	public void testPeaks_4() {

		assertEquals(302, extractedIonSignal.getStopIon());
	}

	public void testPeaks_5() {

		assertEquals(0.00225118f, extractedIonSignal.getAbundance(50));
	}

	public void testPeaks_6() {

		assertEquals(0.000246839f, extractedIonSignal.getAbundance(302));
	}

	public void testPeaks_7() {

		assertEquals(257820, peakModel.getStartRetentionTime());
	}

	public void testPeaks_8() {

		assertEquals(276016, peakModel.getStopRetentionTime());
	}
}
