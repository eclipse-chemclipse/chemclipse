/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.nmr.converter.supplier.gaml.converter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.eclipse.chemclipse.nmr.converter.core.AbstractScanImportConverter;
import org.eclipse.chemclipse.nmr.converter.core.IScanImportConverter;
import org.eclipse.chemclipse.nmr.converter.supplier.gaml.io.ScanReaderVersion100;
import org.eclipse.chemclipse.nmr.converter.supplier.gaml.io.ScanReaderVersion110;
import org.eclipse.chemclipse.nmr.converter.supplier.gaml.io.ScanReaderVersion120;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.io.IConstants;
import org.eclipse.chemclipse.xxd.converter.supplier.io.exception.UnknownVersionException;
import org.eclipse.core.runtime.IProgressMonitor;

public class ScanImportConverter extends AbstractScanImportConverter<Collection<IComplexSignalMeasurement<?>>> implements IScanImportConverter<Collection<IComplexSignalMeasurement<?>>> {

	public ScanImportConverter() {

		super();
	}

	@Override
	public IProcessingInfo<Collection<IComplexSignalMeasurement<?>>> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<Collection<IComplexSignalMeasurement<?>>> processingInfo = new ProcessingInfo<>();
		try {
			final FileReader fileReader = new FileReader(file);
			final char[] charBuffer = new char[100];
			fileReader.read(charBuffer);
			fileReader.close();
			//
			final String header = new String(charBuffer);
			if(header.contains(IConstants.GAML_V_100)) {
				ScanReaderVersion100 scanReader = new ScanReaderVersion100();
				Collection<IComplexSignalMeasurement<?>> result = scanReader.read(file, monitor);
				processingInfo.setProcessingResult(result);
			} else if(header.contains(IConstants.GAML_V_110)) {
				ScanReaderVersion110 scanReader = new ScanReaderVersion110();
				Collection<IComplexSignalMeasurement<?>> result = scanReader.read(file, monitor);
				processingInfo.setProcessingResult(result);
			} else if(header.contains(IConstants.GAML_V_120)) {
				ScanReaderVersion120 scanReader = new ScanReaderVersion120();
				Collection<IComplexSignalMeasurement<?>> result = scanReader.read(file, monitor);
				processingInfo.setProcessingResult(result);
			} else {
				throw new UnknownVersionException();
			}
		} catch(IOException e) {
			e.printStackTrace();
			processingInfo.addErrorMessage("GAML NMR", "There was a problem during file import.", e);
		}
		return processingInfo;
	}
}
