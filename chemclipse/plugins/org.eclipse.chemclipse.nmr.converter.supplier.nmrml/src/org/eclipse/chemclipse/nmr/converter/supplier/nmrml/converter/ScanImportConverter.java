/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.nmr.converter.supplier.nmrml.converter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.chemclipse.converter.exceptions.UnknownVersionException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.eclipse.chemclipse.nmr.converter.core.AbstractScanImportConverter;
import org.eclipse.chemclipse.nmr.converter.supplier.nmrml.io.ScanReaderVersion100;
import org.eclipse.chemclipse.nmr.model.core.ISpectrumNMR;
import org.eclipse.chemclipse.nmr.model.core.SpectrumNMR;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class ScanImportConverter extends AbstractScanImportConverter {

	private static final Logger logger = Logger.getLogger(ScanImportConverter.class);

	public ScanImportConverter() {

		super();
	}

	@Override
	public IProcessingInfo<ISpectrumNMR> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<ISpectrumNMR> processingInfo = new ProcessingInfo<>();
		try (final FileReader fileReader = new FileReader(file)) {
			final char[] charBuffer = new char[500];
			fileReader.read(charBuffer);

			final String header = new String(charBuffer);
			if(header.contains("nmrML")) {
				ScanReaderVersion100 scanReader = new ScanReaderVersion100();
				Collection<IComplexSignalMeasurement<?>> complexSignalMeasurement = scanReader.read(file, monitor);
				ISpectrumNMR spectrumNMR = new SpectrumNMR();
				spectrumNMR.setComplexSignalMeasurements(complexSignalMeasurement);
				processingInfo.setProcessingResult(spectrumNMR);
			} else {
				throw new UnknownVersionException();
			}
		} catch(IOException e) {
			processingInfo.addErrorMessage("nmrML", "There was a problem during file import.");
			logger.error(e);
		}
		return processingInfo;
	}
}
