/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.vsd.converter.supplier.jcampdx.converter;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.vsd.converter.core.AbstractScanImportConverter;
import org.eclipse.chemclipse.vsd.converter.core.IScanImportConverter;
import org.eclipse.chemclipse.vsd.converter.supplier.jcampdx.io.ScanReader;
import org.eclipse.chemclipse.vsd.converter.supplier.jcampdx.model.IVendorSpectrumVSD;
import org.eclipse.chemclipse.vsd.model.core.ISpectrumVSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ScanImportConverter extends AbstractScanImportConverter implements IScanImportConverter {

	private static final Logger logger = Logger.getLogger(ScanImportConverter.class);

	@Override
	public IProcessingInfo<ISpectrumVSD> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<ISpectrumVSD> processingInfo = new ProcessingInfo<>();
		try {
			ScanReader scanReader = new ScanReader();
			IVendorSpectrumVSD vendorScan = scanReader.read(file, monitor);
			processingInfo.setProcessingResult(vendorScan);
		} catch(IOException e) {
			processingInfo.addErrorMessage("JCAMP-DX", "There was a problem to import the FT-IR file.");
			logger.warn(e);
		}
		return processingInfo;
	}
}
