/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.report.supplier.txt.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.report.settings.IChromatogramReportSettings;
import org.eclipse.chemclipse.chromatogram.xxd.report.supplier.txt.internal.support.SpecificationValidator;
import org.eclipse.chemclipse.chromatogram.xxd.report.supplier.txt.io.ReportWriter4;
import org.eclipse.chemclipse.chromatogram.xxd.report.supplier.txt.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.report.supplier.txt.settings.ReportSettings4;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class Report4 extends AbstractReport {

	private static final Logger logger = Logger.getLogger(Report4.class);

	@Override
	public IProcessingInfo<File> report(File file, boolean append, List<IChromatogram> chromatograms, IChromatogramReportSettings settings, IProgressMonitor monitor) {

		file = SpecificationValidator.validateSpecification(file);
		IProcessingInfo<File> processingInfo = super.validate(file);
		//
		if(!processingInfo.hasErrorMessages()) {
			if(settings instanceof ReportSettings4) {
				ReportWriter4 chromatogramReport = new ReportWriter4();
				try {
					chromatogramReport.generate(file, append, chromatograms, monitor);
					processingInfo.setProcessingResult(file);
				} catch(IOException e) {
					logger.warn(e);
					processingInfo.addErrorMessage("ChemClipse Chromatogram Report", "The report couldn't be created. An error occured.");
				}
			} else {
				logger.warn("The settings are not of type: " + ReportSettings4.class);
			}
		}
		//
		return processingInfo;
	}

	@Override
	public IProcessingInfo<File> generate(File file, boolean append, IChromatogram chromatogram, IProgressMonitor monitor) {

		List<IChromatogram> chromatograms = getChromatogramList(chromatogram);
		ReportSettings4 settings = PreferenceSupplier.getReportSettings4();
		return report(file, append, chromatograms, settings, monitor);
	}

	@Override
	public IProcessingInfo<File> generate(File file, boolean append, List<IChromatogram> chromatograms, IProgressMonitor monitor) {

		ReportSettings4 settings = PreferenceSupplier.getReportSettings4();
		return report(file, append, chromatograms, settings, monitor);
	}
}
