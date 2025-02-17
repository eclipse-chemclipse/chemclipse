/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.vsd.converter.chromatogram;

import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramExportConverter;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class AbstractExportConverter extends AbstractChromatogramExportConverter implements IExportConverterVSD {

	private static final String DESCRIPTION = "Export Converter (VSD)";

	@Override
	public IProcessingInfo<File> convert(File file, IChromatogram chromatogram, IProgressMonitor monitor) {

		IProcessingInfo<File> processingInfo = new ProcessingInfo<>();
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			processingInfo.addMessages(convert(outputStream, chromatogram, monitor));
		} catch(Exception e) {
			processingInfo.addErrorMessage(DESCRIPTION, "Failed to export the chromatogram to file: " + file);
		}
		//
		return processingInfo;
	}
}
