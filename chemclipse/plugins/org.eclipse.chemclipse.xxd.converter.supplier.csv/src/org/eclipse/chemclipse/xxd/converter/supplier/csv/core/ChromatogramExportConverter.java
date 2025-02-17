/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.csv.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramExportConverter;
import org.eclipse.chemclipse.converter.chromatogram.IChromatogramExportConverter;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.csv.internal.converter.SpecificationValidator;
import org.eclipse.chemclipse.xxd.converter.supplier.csv.io.core.ChromatogramWriter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

public class ChromatogramExportConverter extends AbstractChromatogramExportConverter implements IChromatogramExportConverter {

	private static final Logger logger = Logger.getLogger(ChromatogramExportConverter.class);
	private static final String DESCRIPTION = "CSV Export Converter";

	@Override
	public IProcessingInfo<File> convert(File file, IChromatogram chromatogram, IProgressMonitor monitor) {

		file = SpecificationValidator.validateSpecification(file, "csv");
		IProcessingInfo<File> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			/*
			 * CSD, MSD, WSD
			 */
			ChromatogramWriter writer = null;
			if(chromatogram instanceof IChromatogramCSD) {
				writer = new ChromatogramWriter();
			} else if(chromatogram instanceof IChromatogramMSD) {
				writer = new ChromatogramWriter();
			} else if(chromatogram instanceof IChromatogramWSD) {
				writer = new ChromatogramWriter();
			}
			//
			if(writer != null) {
				try {
					writer.writeChromatogram(file, chromatogram, monitor);
					processingInfo.setProcessingResult(file);
				} catch(IOException e) {
					logger.error(e);
					processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.failedToWriteFile, file.getAbsolutePath()));
				}
			}
		}
		return processingInfo;
	}
}
