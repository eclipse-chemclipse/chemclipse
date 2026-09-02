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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mace.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.chemclipse.converter.exceptions.FileIsEmptyException;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotReadableException;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.database.AbstractDatabaseImportConverter;
import org.eclipse.chemclipse.msd.converter.io.IMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.supplier.mace.io.MaceReader;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

public class DatabaseImportConverter extends AbstractDatabaseImportConverter {

	private static final Logger logger = Logger.getLogger(DatabaseImportConverter.class);
	private static final String DESCRIPTION = "MACE Database Import";

	@Override
	public IProcessingInfo<IMassSpectra> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<IMassSpectra> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			try {
				IMassSpectraReader massSpectraReader = new MaceReader();
				IMassSpectra massSpectra = massSpectraReader.read(file, monitor);
				if(massSpectra != null && !massSpectra.isEmpty()) {
					processingInfo.setProcessingResult(massSpectra);
				} else {
					processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.noMassSpectraStored, file.getAbsolutePath()));
				}
			} catch(FileNotFoundException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.fileNotFound, file.getAbsolutePath()));
			} catch(FileIsNotReadableException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.fileNotReadable, file.getAbsolutePath()));
			} catch(FileIsEmptyException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.emptyFile, file.getAbsolutePath()));
			} catch(IOException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, NLS.bind(ConverterMessages.failedToReadFile, file.getAbsolutePath()));
			}
		}
		return processingInfo;
	}
}