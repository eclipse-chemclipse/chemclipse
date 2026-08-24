/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.dsd.converter.chromatogram;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramConverter;
import org.eclipse.chemclipse.converter.chromatogram.IChromatogramConverter;
import org.eclipse.chemclipse.dsd.model.core.IChromatogramDSD;
import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramPeakWSD;

public class ChromatogramConverterDSD extends AbstractChromatogramConverter<IChromatogramPeakWSD, IChromatogramDSD> {

	private static IChromatogramConverter<IChromatogramPeakWSD, IChromatogramDSD> instance = null;

	public ChromatogramConverterDSD() {

		super("org.eclipse.chemclipse.dsd.converter.chromatogramSupplier", IChromatogramDSD.class, DataCategory.WSD);
	}

	public static IChromatogramConverter<IChromatogramPeakWSD, IChromatogramDSD> getInstance() {

		if(instance == null) {
			instance = new ChromatogramConverterDSD();
		}

		return instance;
	}
}
