/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.internal.runnables;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl.AlkanePatternDetectorCSD;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl.AlkanePatternDetectorMSD;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.wizards.RetentionIndexWizardElements;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class ChromatogramImportRunnable implements IRunnableWithProgress {

	private RetentionIndexWizardElements wizardElements;
	private IChromatogram chromatogram;

	public ChromatogramImportRunnable(RetentionIndexWizardElements wizardElements) {

		this.wizardElements = wizardElements;
	}

	public IChromatogram getChromatogram() {

		return chromatogram;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		/*
		 * Calibration file
		 */
		String pathRetentionIndexFile = "";
		if(wizardElements.isUseExistingRetentionIndexFile()) {
			pathRetentionIndexFile = wizardElements.getPathRetentionIndexFile();
		}
		/*
		 * MSD/CSD
		 */
		boolean useAlreadyDetectedPeaks = wizardElements.isUseAlreadyDetectedPeaks();
		if(wizardElements.isUseMassSpectrometryData()) {
			String chromatogramPath = wizardElements.getSelectedChromatograms().get(0);
			AlkanePatternDetectorMSD alkanePatternDetector = new AlkanePatternDetectorMSD();
			chromatogram = alkanePatternDetector.parseChromatogram(chromatogramPath, pathRetentionIndexFile, useAlreadyDetectedPeaks, monitor);
			File file = new File(chromatogramPath);
			PreferenceSupplier.setFilterPathModelsMSD(file.getParentFile().getAbsolutePath());
		} else {
			String chromatogramPath = wizardElements.getSelectedChromatograms().get(0);
			AlkanePatternDetectorCSD alkanePatternDetector = new AlkanePatternDetectorCSD();
			chromatogram = alkanePatternDetector.parseChromatogram(chromatogramPath, pathRetentionIndexFile, useAlreadyDetectedPeaks, monitor);
			File file = new File(chromatogramPath);
			PreferenceSupplier.setFilterPathModelsCSD(file.getParentFile().getAbsolutePath());
		}
	}
}
