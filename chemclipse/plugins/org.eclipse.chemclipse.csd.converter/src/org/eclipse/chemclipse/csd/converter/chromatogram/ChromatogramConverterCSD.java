/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 * Christoph Läubrich - Adjust to new API
 *******************************************************************************/
package org.eclipse.chemclipse.csd.converter.chromatogram;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramConverter;
import org.eclipse.chemclipse.converter.chromatogram.IChromatogramConverter;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramPeakCSD;
import org.eclipse.chemclipse.model.support.ChromatogramColumnSupport;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramConverterCSD extends AbstractChromatogramConverter<IChromatogramPeakCSD, IChromatogramCSD> {

	private static IChromatogramConverter<IChromatogramPeakCSD, IChromatogramCSD> instance = null;

	public ChromatogramConverterCSD() {

		super("org.eclipse.chemclipse.csd.converter.chromatogramSupplier", IChromatogramCSD.class, DataCategory.CSD);
	}

	public static IChromatogramConverter<IChromatogramPeakCSD, IChromatogramCSD> getInstance() {

		if(instance == null) {
			instance = new ChromatogramConverterCSD();
		}

		return instance;
	}

	@Override
	public void postProcessChromatogram(IProcessingInfo<IChromatogramCSD> processingInfo, IProgressMonitor monitor) {

		if(processingInfo != null && processingInfo.getProcessingResult() instanceof IChromatogramCSD chromatogramCSD) {
			ChromatogramColumnSupport.parseSeparationColumn(chromatogramCSD);
		}
	}
}