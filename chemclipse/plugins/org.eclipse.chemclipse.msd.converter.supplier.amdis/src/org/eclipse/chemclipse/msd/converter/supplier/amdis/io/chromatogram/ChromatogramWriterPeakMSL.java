/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.io.chromatogram;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.msd.converter.io.AbstractChromatogramMSDWriter;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.msl.MSLPeakExportConverter;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.msd.model.core.PeaksMSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramWriterPeakMSL extends AbstractChromatogramMSDWriter {

	@Override
	public void writeChromatogram(File file, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		if(chromatogram == null || file == null) {
			throw new IOException("The chromatogram and the file must be not null.");
		}
		/*
		 * Extract the peaks
		 */
		IPeaksMSD peaks = new PeaksMSD();
		List<IChromatogramPeakMSD> chromatogramPeaks = chromatogram.getPeaks();
		for(IChromatogramPeakMSD chromatogramPeak : chromatogramPeaks) {
			peaks.addPeak(chromatogramPeak);
		}
		/*
		 * Export the peaks
		 */
		MSLPeakExportConverter peakExportConverter = new MSLPeakExportConverter();
		peakExportConverter.convert(file, peaks, false, monitor);
	}
}
