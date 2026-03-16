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
package org.eclipse.chemclipse.vsd.converter.supplier.cml.converter;

import java.io.File;

import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.vsd.converter.core.AbstractScanImportConverter;
import org.eclipse.chemclipse.vsd.converter.supplier.cml.io.ScanReader;
import org.eclipse.chemclipse.vsd.converter.supplier.cml.model.IVendorSpectrumVSD;
import org.eclipse.chemclipse.vsd.model.core.ISpectrumVSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ScanImportConverter extends AbstractScanImportConverter {

	@Override
	public IProcessingInfo<ISpectrumVSD> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<ISpectrumVSD> processingInfo = new ProcessingInfo<>();
		IVendorSpectrumVSD vendorScan = null;
		ScanReader scanReader = new ScanReader();
		vendorScan = scanReader.read(file, monitor);
		processingInfo.setProcessingResult(vendorScan);
		return processingInfo;
	}
}
