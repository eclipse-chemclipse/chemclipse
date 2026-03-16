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
package org.eclipse.chemclipse.pcr.converter.supplier.rdes.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.pcr.converter.core.AbstractPlateImportConverter;
import org.eclipse.chemclipse.pcr.converter.core.IPlateImportConverter;
import org.eclipse.chemclipse.pcr.converter.supplier.rdes.io.PCRReader;
import org.eclipse.chemclipse.pcr.model.core.IPlate;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class PCRImportConverter extends AbstractPlateImportConverter {

	private static final Logger logger = Logger.getLogger(PCRImportConverter.class);
	private static final String DESCRIPTION = "RDES Import";
	private static IPlateImportConverter instance = null;

	@Override
	public IProcessingInfo<IPlate> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<IPlate> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			PCRReader qPCR = new PCRReader();
			try {
				IPlate plate = qPCR.read(file);
				processingInfo.setProcessingResult(plate);
			} catch(IOException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, "Failed to read file: " + file.getAbsolutePath());
			}
		}
		return processingInfo;
	}

	public static IPlateImportConverter getInstance() {

		if(instance == null) {
			instance = new PCRImportConverter();
		}
		return instance;
	}
}
