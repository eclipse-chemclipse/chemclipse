/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.csd.converter.supplier.xy.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramExportConverter;
import org.eclipse.chemclipse.converter.chromatogram.IChromatogramExportConverter;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.csd.converter.io.IChromatogramCSDWriter;
import org.eclipse.chemclipse.csd.converter.supplier.xy.io.ChromatogramWriter;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

public class ChromatogramExportConverter extends AbstractChromatogramExportConverter implements IChromatogramExportConverter {

	private static final Logger logger = Logger.getLogger(ChromatogramExportConverter.class);
	//
	private static final String DESCRIPTION = "XY Export Converter";
	private static final String FILE_EXTENSION = ".xy";

	@Override
	public IProcessingInfo<File> convert(File file, IChromatogram chromatogram, IProgressMonitor monitor) {

		IProcessingInfo<File> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages() && chromatogram instanceof IChromatogramCSD chromatogramCSD) {
			try {
				/*
				 * Base name
				 */
				String directory = file.getParent();
				String name = file.getName();
				String fileName = name.toLowerCase().endsWith(FILE_EXTENSION) ? name.substring(0, name.length() - 3) : name;
				/*
				 * Master
				 */
				IChromatogramCSDWriter writer = new ChromatogramWriter();
				File fileMaster = new File(directory + File.separator + fileName + FILE_EXTENSION);
				writer.writeChromatogram(fileMaster, chromatogramCSD, monitor);
				/*
				 * References
				 */
				int id = 1;
				List<IChromatogram> chromatogramReferences = chromatogram.getReferencedChromatograms();
				for(IChromatogram chromatogramReference : chromatogramReferences) {
					if(chromatogramReference instanceof IChromatogramCSD referenceChromatogramCSD) {
						File fileReference = new File(directory + File.separator + fileName + "-" + id++ + FILE_EXTENSION);
						writer.writeChromatogram(fileReference, referenceChromatogramCSD, monitor);
					}
				}
			} catch(IOException e) {
				logger.error(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.failedToWriteFile, file.getAbsolutePath()));
			} catch(FileIsNotWriteableException e) {
				logger.error(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.fileNotWritable, file.getAbsolutePath()));
			}
			processingInfo.setProcessingResult(file);
		}
		return processingInfo;
	}
}
