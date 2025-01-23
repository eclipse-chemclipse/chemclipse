/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.msl;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.peak.AbstractPeakImportConverter;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class MSLPeakImportConverter extends AbstractPeakImportConverter {

	@Override
	public IProcessingInfo<IPeaksMSD> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<IPeaksMSD> processingInfo = new ProcessingInfo<>();
		processingInfo.addErrorMessage("AMDIS MSL Peak Import", "The converter supports no *.msl file import.");
		return processingInfo;
	}
}
