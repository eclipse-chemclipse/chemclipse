/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.jcampdx.converter;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.core.AbstractScanImportConverter;
import org.eclipse.chemclipse.wsd.converter.core.IScanImportConverter;
import org.eclipse.chemclipse.wsd.converter.supplier.jcampdx.io.ScanReader;
import org.eclipse.chemclipse.wsd.converter.supplier.jcampdx.model.IVendorSpectrumWSD;
import org.eclipse.chemclipse.wsd.model.core.ISpectrumWSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ScanImportConverter extends AbstractScanImportConverter implements IScanImportConverter {

	private static final Logger logger = Logger.getLogger(ScanImportConverter.class);

	@Override
	public IProcessingInfo<ISpectrumWSD> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<ISpectrumWSD> processingInfo = new ProcessingInfo<>();
		try {
			ScanReader scanReader = new ScanReader();
			IVendorSpectrumWSD vendorScan = scanReader.read(file, monitor);
			processingInfo.setProcessingResult(vendorScan);
		} catch(IOException e) {
			processingInfo.addErrorMessage("JCAMP-DX", "There was a problem with the file.");
			logger.warn(e);
		}
		return processingInfo;
	}
}
