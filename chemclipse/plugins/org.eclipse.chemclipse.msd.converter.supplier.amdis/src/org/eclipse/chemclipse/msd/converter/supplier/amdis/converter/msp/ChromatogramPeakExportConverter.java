/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.msp;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramExportConverter;
import org.eclipse.chemclipse.converter.chromatogram.IChromatogramExportConverter;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDWriter;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.internal.converter.SpecificationValidatorMSP;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.io.chromatogram.ChromatogramWriterPeakMSP;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

public class ChromatogramPeakExportConverter extends AbstractChromatogramExportConverter implements IChromatogramExportConverter {

	private static final Logger logger = Logger.getLogger(ChromatogramPeakExportConverter.class);
	//
	public static final String DESCRIPTION = "NIST Chromatogram Peaks";
	public static final String FILE_EXTENSION = ".msp";
	public static final String FILE_NAME = DESCRIPTION.replaceAll("\\s", "") + FILE_EXTENSION;
	public static final String FILTER_EXTENSION = "*" + FILE_EXTENSION;
	public static final String FILTER_NAME = DESCRIPTION + " (*" + FILE_EXTENSION + ")";

	@Override
	public IProcessingInfo<File> convert(File file, IChromatogram chromatogram, IProgressMonitor monitor) {

		file = SpecificationValidatorMSP.validateSpecification(file);
		IProcessingInfo<File> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages() && chromatogram instanceof IChromatogramMSD chromatogramMSD) {
			try {
				IChromatogramMSDWriter writer = new ChromatogramWriterPeakMSP();
				writer.writeChromatogram(file, chromatogramMSD, monitor);
				processingInfo.setProcessingResult(file);
			} catch(IOException e) {
				logger.error(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.failedToWriteFile, file.getAbsolutePath()));
			} catch(FileIsNotWriteableException e) {
				logger.error(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.fileNotWritable, file.getAbsolutePath()));
			}
		}
		return processingInfo;
	}
}
