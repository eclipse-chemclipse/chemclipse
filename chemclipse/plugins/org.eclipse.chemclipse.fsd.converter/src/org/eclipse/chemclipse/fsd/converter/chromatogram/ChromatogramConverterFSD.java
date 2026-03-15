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
package org.eclipse.chemclipse.fsd.converter.chromatogram;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramConverter;
import org.eclipse.chemclipse.converter.chromatogram.IChromatogramConverter;
import org.eclipse.chemclipse.fsd.model.core.IChromatogramFSD;
import org.eclipse.chemclipse.fsd.model.core.IChromatogramPeakFSD;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramConverterFSD extends AbstractChromatogramConverter<IChromatogramPeakFSD, IChromatogramFSD> {

	private static IChromatogramConverter<IChromatogramPeakFSD, IChromatogramFSD> instance = null;

	public ChromatogramConverterFSD() {

		super("org.eclipse.chemclipse.fsd.converter.chromatogramSupplier", IChromatogramFSD.class, DataCategory.FSD);
	}

	public static IChromatogramConverter<IChromatogramPeakFSD, IChromatogramFSD> getInstance() {

		if(instance == null) {
			instance = new ChromatogramConverterFSD();
		}

		return instance;
	}

	@Override
	public void postProcessChromatogram(IProcessingInfo<IChromatogramFSD> processingInfo, IProgressMonitor monitor) {

	}
}
