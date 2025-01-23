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

public class MatlabParafacPeakImportConverter_1_ITest extends TestCase {

	private IPeaksMSD peaks;
	private IPeakMSD peak;
	private IExtractedIonSignal extractedIonSignal;
	private IPeakModelMSD peakModel;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		MatlabParafacPeakImportConverter converter = new MatlabParafacPeakImportConverter();
		File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_MATLAB_PEAKS));
		IProcessingInfo<IPeaksMSD> processingInfo = converter.convert(file, new NullProgressMonitor());
		peaks = processingInfo.getProcessingResult();
		peak = peaks.getPeaks().get(0);
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

		assertEquals("PARAFAC model, 257820 to 276016 milliseconds, Peak 1", peak.getModelDescription());
	}

	public void testPeaks_3() {

		assertEquals(50, extractedIonSignal.getStartIon());
	}

	public void testPeaks_4() {

		assertEquals(446, extractedIonSignal.getStopIon());
	}

	public void testPeaks_5() {

		assertEquals(0.00222065f, extractedIonSignal.getAbundance(50));
	}

	public void testPeaks_6() {

		assertEquals(6.46808e-005f, extractedIonSignal.getAbundance(446));
	}

	public void testPeaks_7() {

		assertEquals(257820, peakModel.getStartRetentionTime());
	}

	public void testPeaks_8() {

		assertEquals(276016, peakModel.getStopRetentionTime());
	}
}
