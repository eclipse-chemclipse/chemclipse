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
 * Philip Wenig - refactoring vibrational spectroscopy
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.spectroml.converter;

import java.io.File;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.core.AbstractScanImportConverter;
import org.eclipse.chemclipse.wsd.converter.supplier.spectroml.io.ScanReader;
import org.eclipse.chemclipse.wsd.converter.supplier.spectroml.model.IVendorSpectrumWSD;
import org.eclipse.chemclipse.wsd.model.core.ISpectrumWSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ScanImportConverter extends AbstractScanImportConverter {

	@Override
	public IProcessingInfo<ISpectrumWSD> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<ISpectrumWSD> processingInfo = new ProcessingInfo<>();
		IVendorSpectrumWSD vendorScan = null;
		ScanReader scanReader = new ScanReader();
		vendorScan = scanReader.read(file, monitor);
		processingInfo.setProcessingResult(vendorScan);
		return processingInfo;
	}
}