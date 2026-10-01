/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.report.txt.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.report.massspectra.AbstractMassSpectraReportGenerator;
import org.eclipse.chemclipse.msd.report.settings.IMassSpectraReportSettings;
import org.eclipse.chemclipse.msd.report.txt.internal.support.SpecificationValidator;
import org.eclipse.chemclipse.msd.report.txt.io.StandaloneMassSpectrumReportWriter;
import org.eclipse.chemclipse.msd.report.txt.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.report.txt.settings.StandaloneMassSpectrumReportSettings;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class StandaloneMassSpectrumReport extends AbstractMassSpectraReportGenerator {

	private static final Logger logger = Logger.getLogger(StandaloneMassSpectrumReport.class);
	private static final String DESCRIPTION = "Mass Spectra Metadata Report";

	@Override
	public IProcessingInfo<File> generate(File file, boolean append, IMassSpectra massSpectra, IMassSpectraReportSettings settings, IProgressMonitor monitor) {

		File reportFile = SpecificationValidator.validateSpecification(file);
		IProcessingInfo<File> processingInfo = super.validate(reportFile);

		if(!processingInfo.hasErrorMessages()) {
			if(settings instanceof StandaloneMassSpectrumReportSettings) {
				StandaloneMassSpectrumReportWriter reportWriter = new StandaloneMassSpectrumReportWriter();
				try {
					reportWriter.generate(reportFile, append, massSpectra);
					processingInfo.setProcessingResult(reportFile);
				} catch(IOException e) {
					logger.warn(e);
					processingInfo.addErrorMessage(DESCRIPTION, "The report couldn't be created.");
				}
			} else {
				logger.warn("The settings are not of type: " + StandaloneMassSpectrumReportSettings.class);
			}
		}

		return processingInfo;
	}

	@Override
	public IProcessingInfo<File> generate(File file, boolean append, IMassSpectra massSpectra, IProgressMonitor monitor) {

		StandaloneMassSpectrumReportSettings settings = PreferenceSupplier.getMetadataReportSettings();
		return generate(file, append, massSpectra, settings, monitor);
	}
}
