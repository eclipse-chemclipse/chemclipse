/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl.CalibrationFile;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.io.ChromatogramWriter;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.settings.IndexExportSettings;
import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramExportConverter;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramExportConverter extends AbstractChromatogramExportConverter {

	private static final Logger logger = Logger.getLogger(ChromatogramExportConverter.class);

	@Override
	public IProcessingInfo<File> convert(File file, IChromatogram chromatogram, IProgressMonitor monitor) {

		IProcessingInfo<File> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			try {
				ChromatogramWriter writer = new ChromatogramWriter();
				IndexExportSettings indexExportSettings = new IndexExportSettings();
				indexExportSettings.setUseCuratedNames(PreferenceSupplier.isCalibrationExportUseCuratedNames());
				indexExportSettings.setDeriveMissingIndices(PreferenceSupplier.isCalibrationExportDeriveMissingIndices());
				indexExportSettings.setOpenCalibrationFileAfterProcessing(PreferenceSupplier.isOpenCalibrationFileAfterProcessing());
				writer.writeChromatogram(file, chromatogram, indexExportSettings, monitor);
				processingInfo.setProcessingResult(file);
			} catch(IOException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(CalibrationFile.DESCRIPTION, "Failed to write file: " + file);
			}
		}
		return processingInfo;
	}
}
