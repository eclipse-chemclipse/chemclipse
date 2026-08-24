/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.converter.supplier.scf.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramImportConverter;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.dsd.converter.io.IChromatogramDSDReader;
import org.eclipse.chemclipse.dsd.model.core.IChromatogramDSD;
import org.eclipse.chemclipse.wsd.converter.supplier.scf.io.ChromatogramReader;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

public class ChromatogramImportConverter extends AbstractChromatogramImportConverter<IChromatogramDSD> {

	private static final Logger logger = Logger.getLogger(ChromatogramImportConverter.class);
	private static final String DESCRIPTION = "SCF Import Converter";

	@Override
	public IProcessingInfo<IChromatogramDSD> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramDSD> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			IChromatogramDSDReader reader = new ChromatogramReader();
			monitor.subTask(ConverterMessages.importChromatogram);
			try {
				IChromatogramDSD chromatogram = reader.read(file, monitor);
				processingInfo.setProcessingResult(chromatogram);
			} catch(IOException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.failedToReadFile, file.getAbsolutePath()));
			}
		}
		return processingInfo;
	}

	@Override
	public IProcessingInfo<IChromatogramOverview> convertOverview(File file, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramOverview> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			IChromatogramDSDReader reader = new ChromatogramReader();
			monitor.subTask(ConverterMessages.importChromatogramOverview);
			try {
				IChromatogramOverview chromatogramOverview = reader.readOverview(file, monitor);
				processingInfo.setProcessingResult(chromatogramOverview);
			} catch(IOException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.failedToReadFile, file.getAbsolutePath()));
			}
		}
		return processingInfo;
	}
}
