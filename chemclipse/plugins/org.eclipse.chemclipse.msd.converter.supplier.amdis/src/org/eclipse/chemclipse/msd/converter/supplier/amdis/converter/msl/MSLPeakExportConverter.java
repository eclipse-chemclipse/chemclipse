/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - adjust to new API
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.msl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.peak.AbstractPeakExportConverter;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.internal.converter.SpecificationValidatorMSL;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.io.PeakWriterMSL;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * NAME FIELD:
 * If the mass spectrum is a type of IRegularLibraryMassSpectrum, than getLibraryInformation().getName() will be used,
 * otherwise massSpectrum.getIdentifier().
 * 
 */
public class MSLPeakExportConverter extends AbstractPeakExportConverter {

	private static final Logger logger = Logger.getLogger(MSLPeakExportConverter.class);
	private static final String DESCRIPTION = "AMDIS MSL Peak Export";

	@Override
	public IProcessingInfo<File> convert(File file, IPeaksMSD peaks, boolean append, IProgressMonitor monitor) {

		IProcessingInfo<File> processingInfo = new ProcessingInfo<>();
		/*
		 * Checks that file and mass spectra are not null.
		 */
		file = SpecificationValidatorMSL.validateSpecification(file);
		IProcessingInfo<File> processingInfoValidate = validate(file, peaks);
		if(processingInfoValidate.hasErrorMessages()) {
			processingInfo.addMessages(processingInfoValidate);
		} else {
			try {
				/*
				 * Convert the mass spectra.
				 */
				PeakWriterMSL peakWriter = new PeakWriterMSL();
				peakWriter.write(file, peaks, append);
				processingInfo.setProcessingResult(file);
			} catch(FileNotFoundException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, "The file couldn't be found: " + file.getAbsolutePath());
			} catch(IOException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, "The file is not writeable: " + file.getAbsolutePath());
			}
		}
		return processingInfo;
	}

	private IProcessingInfo<File> validate(File file, IPeaksMSD peaks) {

		IProcessingInfo<File> processingInfo = new ProcessingInfo<>();
		processingInfo.addMessages(super.validate(file));
		processingInfo.addMessages(super.validate(peaks));
		return processingInfo;
	}
}
