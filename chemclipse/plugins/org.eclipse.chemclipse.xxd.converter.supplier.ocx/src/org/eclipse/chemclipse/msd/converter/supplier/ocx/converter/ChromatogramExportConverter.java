/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.ocx.converter;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramExportConverter;
import org.eclipse.chemclipse.converter.chromatogram.IChromatogramExportConverter;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDWriter;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.io.ChromatogramWriterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.support.SpecificationValidator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

public class ChromatogramExportConverter extends AbstractChromatogramExportConverter implements IChromatogramExportConverter {

	private static final Logger logger = Logger.getLogger(ChromatogramExportConverter.class);

	@Override
	public IProcessingInfo<File> convert(File file, IChromatogram chromatogram, IProgressMonitor monitor) {

		file = SpecificationValidator.validateSpecification(file);
		IProcessingInfo<File> processingInfo = super.validate(file);
		/*
		 * Don't process if errors have occurred.
		 */
		if(!processingInfo.hasErrorMessages() && chromatogram instanceof IChromatogramMSD chromatogramMSD) {
			try {
				IChromatogramMSDWriter writer = new ChromatogramWriterMSD();
				writer.writeChromatogram(file, chromatogramMSD, monitor);
			} catch(IOException e) {
				logger.error(e);
				processingInfo.addErrorMessage(ConverterMessages.exportChromatogram, NLS.bind(ConverterMessages.failedToWriteFile, file.getAbsolutePath()));
			} catch(FileIsNotWriteableException e) {
				logger.error(e);
				processingInfo.addErrorMessage(ConverterMessages.exportChromatogram, NLS.bind(ConverterMessages.fileNotWritable, file.getAbsolutePath()));
			}
			processingInfo.setProcessingResult(file);
		}
		return processingInfo;
	}
}
