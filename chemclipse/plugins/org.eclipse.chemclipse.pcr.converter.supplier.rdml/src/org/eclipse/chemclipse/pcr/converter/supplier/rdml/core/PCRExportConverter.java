/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.pcr.converter.supplier.rdml.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.pcr.converter.core.AbstractPlateExportConverter;
import org.eclipse.chemclipse.pcr.converter.core.IPlateExportConverter;
import org.eclipse.chemclipse.pcr.converter.supplier.rdml.io.PCRWriter;
import org.eclipse.chemclipse.pcr.model.core.IPlate;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class PCRExportConverter extends AbstractPlateExportConverter {

	private static final Logger logger = Logger.getLogger(PCRExportConverter.class);
	private static IPlateExportConverter instance = null;

	@Override
	public IProcessingInfo<File> convert(File file, IPlate plate, IProgressMonitor monitor) {

		IProcessingInfo<File> processingInfo = super.validate(file);
		PCRWriter pcrWriter = new PCRWriter();
		try {
			pcrWriter.write(plate, file, monitor);
			processingInfo.setProcessingResult(file);
		} catch(IOException e) {
			logger.error(e);
		}
		return processingInfo;
	}

	public static IPlateExportConverter getInstance() {

		if(instance == null) {
			instance = new PCRExportConverter();
		}
		return instance;
	}
}
