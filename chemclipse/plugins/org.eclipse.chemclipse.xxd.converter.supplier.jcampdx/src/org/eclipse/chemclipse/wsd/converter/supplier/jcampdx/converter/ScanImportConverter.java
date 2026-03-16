/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.converter.supplier.jcampdx.converter;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.core.AbstractScanImportConverter;
import org.eclipse.chemclipse.wsd.converter.supplier.jcampdx.io.ScanReader;
import org.eclipse.chemclipse.wsd.converter.supplier.jcampdx.model.IVendorSpectrumWSD;
import org.eclipse.chemclipse.wsd.model.core.ISpectrumWSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ScanImportConverter extends AbstractScanImportConverter {

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
