/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.pcr.report.supplier.txt.core;

import java.io.File;

import org.eclipse.chemclipse.pcr.converter.core.AbstractPlateImportConverter;
import org.eclipse.chemclipse.pcr.model.core.IPlate;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class PCRImportConverter extends AbstractPlateImportConverter {

	private static final String DESCRIPTION = "PCR Report Converter";

	@Override
	public IProcessingInfo<IPlate> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<IPlate> processingInfo = super.validate(file);
		processingInfo.addErrorMessage(DESCRIPTION, "The export converter doesn't support reading files.");
		return processingInfo;
	}
}
