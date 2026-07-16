/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.mzml.converter;

import java.io.File;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramExportConverter;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.io.ChromatogramWriter;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.io.IChromatogramWriterMzML;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramExportConverter extends AbstractChromatogramExportConverter {

	@Override
	public IProcessingInfo<File> convert(File file, IChromatogram chromatogram, IProgressMonitor monitor) {

		IProcessingInfo<File> processingInfo = super.validate(file);
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}
		IChromatogramWriterMzML writer = new ChromatogramWriter();
		writer.writeChromatogram(file, chromatogram, monitor);
		processingInfo.setProcessingResult(file);
		return processingInfo;
	}
}
