/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl.RetentionIndexExtractor;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.settings.IndexExportSettings;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.model.columns.ISeparationColumnIndices;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramWriter {

	public void writeChromatogram(File file, IChromatogram chromatogram, IndexExportSettings indexExportSettings, IProgressMonitor monitor) throws FileNotFoundException, FileIsNotWriteableException, IOException {

		if(chromatogram == null || file == null) {
			throw new IOException("The chromatogram and the file must be not null.");
		}
		/*
		 * Write the cal specifiation.
		 */
		RetentionIndexExtractor retentionIndexExtractor = new RetentionIndexExtractor();
		boolean useCuratedNames = indexExportSettings.isUseCuratedNames();
		boolean deriveMissingIndices = indexExportSettings.isDeriveMissingIndices();
		ISeparationColumnIndices separationColumnIndices = retentionIndexExtractor.extract(chromatogram, deriveMissingIndices, useCuratedNames);
		CalibrationFileWriter calibrationFileWriter = new CalibrationFileWriter();
		calibrationFileWriter.write(file, separationColumnIndices);
		if(PreferenceSupplier.isOpenReportAfterProcessing()) {
			UpdateNotifier.update(CalibrationFileWriter.TOPIC_PROCESSING_FILE_CREATED, file);
		}
	}
}
