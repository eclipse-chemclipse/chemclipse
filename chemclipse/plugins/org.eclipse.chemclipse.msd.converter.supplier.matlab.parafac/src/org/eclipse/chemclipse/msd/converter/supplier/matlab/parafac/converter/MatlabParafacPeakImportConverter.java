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
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.chemclipse.converter.exceptions.FileIsEmptyException;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotReadableException;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.io.IPeakReader;
import org.eclipse.chemclipse.msd.converter.peak.AbstractPeakImportConverter;
import org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.io.MatlabParafacPeakReader;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.IProcessingMessage;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

public class MatlabParafacPeakImportConverter extends AbstractPeakImportConverter {

	private static final Logger logger = Logger.getLogger(MatlabParafacPeakImportConverter.class);

	@Override
	public IProcessingInfo<IPeaksMSD> convert(File file, IProgressMonitor monitor) {

		IProcessingMessage processingMessage;
		IProcessingInfo<IPeaksMSD> processingInfo = new ProcessingInfo<>();
		try {
			super.validate(file);
			IPeakReader peakReader = new MatlabParafacPeakReader();
			processingInfo = peakReader.read(file, monitor);
		} catch(FileNotFoundException e) {
			logger.warn(e);
			processingMessage = new ProcessingMessage(MessageType.ERROR, ConverterMessages.importPeaks, NLS.bind(ConverterMessages.fileNotFound, file.getAbsolutePath()));
			processingInfo.addMessage(processingMessage);
		} catch(FileIsNotReadableException e) {
			logger.warn(e);
			processingMessage = new ProcessingMessage(MessageType.ERROR, ConverterMessages.importPeaks, NLS.bind(ConverterMessages.failedToReadFile, file.getAbsolutePath()));
			processingInfo.addMessage(processingMessage);
		} catch(FileIsEmptyException e) {
			logger.warn(e);
			processingMessage = new ProcessingMessage(MessageType.ERROR, ConverterMessages.importPeaks, NLS.bind(ConverterMessages.emptyFile, file.getAbsolutePath()));
			processingInfo.addMessage(processingMessage);
		} catch(IOException e) {
			logger.warn(e);
			processingMessage = new ProcessingMessage(MessageType.ERROR, ConverterMessages.importPeaks, NLS.bind(ConverterMessages.failedToReadFile, file.getAbsolutePath()));
			processingInfo.addMessage(processingMessage);
		}
		return processingInfo;
	}
}
