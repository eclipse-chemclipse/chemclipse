/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.nmr.converter.supplier.jcampdx.converter;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.eclipse.chemclipse.nmr.converter.core.AbstractScanImportConverter;
import org.eclipse.chemclipse.nmr.converter.supplier.jcampdx.io.ScanReaderNMR;
import org.eclipse.chemclipse.nmr.model.core.ISpectrumNMR;
import org.eclipse.chemclipse.nmr.model.core.SpectrumNMR;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class ScanImportConverterNMR extends AbstractScanImportConverter {

	private static final Logger logger = Logger.getLogger(ScanImportConverterNMR.class);

	public ScanImportConverterNMR() {

		super();
	}

	@Override
	public IProcessingInfo<ISpectrumNMR> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<ISpectrumNMR> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			ScanReaderNMR scanReader = new ScanReaderNMR();
			try {
				IComplexSignalMeasurement<?> complexSignalMeasurement = scanReader.read(file, monitor);
				ISpectrumNMR spectrumNMR = new SpectrumNMR();
				spectrumNMR.setComplexSignalMeasurements(Collections.singleton(complexSignalMeasurement));
				processingInfo.setProcessingResult(spectrumNMR);
			} catch(IOException e) {
				logger.warn(e);
			}
		}
		return processingInfo;
	}
}