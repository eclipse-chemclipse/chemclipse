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
 * Christoph Läubrich - adjust to new API
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.io.IPeakWriter;
import org.eclipse.chemclipse.msd.converter.peak.AbstractPeakExportConverter;
import org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.io.MatlabParafacPeakWriter;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.IProcessingMessage;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.core.runtime.IProgressMonitor;

public class MatlabParafacPeakExportConverter extends AbstractPeakExportConverter {

	private static final Logger logger = Logger.getLogger(MatlabParafacPeakExportConverter.class);

	@Override
	public IProcessingInfo<File> convert(File file, IPeaksMSD peaks, boolean append, IProgressMonitor monitor) {

		IProcessingMessage processingMessage;
		IProcessingInfo<File> processingInfo = new ProcessingInfo<>();
		/*
		 * Checks that file and mass spectra are not null.
		 */
		IProcessingInfo<File> processingInfoValidate = validate(file, peaks);
		if(processingInfoValidate.hasErrorMessages()) {
			processingInfo.addMessages(processingInfoValidate);
		} else {
			try {
				/*
				 * Convert the peaks.
				 */
				IPeakWriter peakWriter = new MatlabParafacPeakWriter();
				IProcessingInfo<File> processingInfoWriter = peakWriter.write(file, peaks, append);
				processingInfo.addMessages(processingInfoWriter);
				processingInfo.setProcessingResult(processingInfoWriter.getProcessingResult());
				//
			} catch(FileNotFoundException e) {
				logger.warn(e);
				processingMessage = new ProcessingMessage(MessageType.ERROR, "Export Peaks", "The given file was not found: " + file.getAbsolutePath());
				processingInfo.addMessage(processingMessage);
			} catch(FileIsNotWriteableException e) {
				logger.warn(e);
				processingMessage = new ProcessingMessage(MessageType.ERROR, "Export Peaks", "The given file is not writable: " + file.getAbsolutePath());
				processingInfo.addMessage(processingMessage);
			} catch(IOException e) {
				logger.warn(e);
				processingMessage = new ProcessingMessage(MessageType.ERROR, "Export Peaks", "There has gone something wrong writing the file: " + file.getAbsolutePath());
				processingInfo.addMessage(processingMessage);
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
