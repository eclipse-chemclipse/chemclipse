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
package org.eclipse.chemclipse.msd.converter.supplier.mzpeak.converter;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramExportConverter;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDWriter;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.io.ChromatogramMSDWriterVersion09;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

public class ChromatogramExportConverter extends AbstractChromatogramExportConverter {

	private static final Logger logger = Logger.getLogger(ChromatogramExportConverter.class);

	@Override
	public IProcessingInfo<File> convert(File file, IChromatogram chromatogram, IProgressMonitor monitor) {

		IProcessingInfo<File> processingInfo = super.validate(file);
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}
		if(!(chromatogram instanceof IChromatogramMSD chromatogramMSD)) {
			processingInfo.addErrorMessage(ConverterMessages.exportChromatogram, NLS.bind(ConverterMessages.failedToWriteFile, file.getAbsolutePath()));
			return processingInfo;
		}
		IChromatogramMSDWriter writer = new ChromatogramMSDWriterVersion09();
		try {
			writer.writeChromatogram(file, chromatogramMSD, monitor);
			processingInfo.setProcessingResult(file);
		} catch(FileIsNotWriteableException | IOException e) {
			logger.warn(e);
			processingInfo.addErrorMessage(ConverterMessages.exportChromatogram, NLS.bind(ConverterMessages.failedToWriteFile, file.getAbsolutePath()));
		}
		return processingInfo;
	}
}
