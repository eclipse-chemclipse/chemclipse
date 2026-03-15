/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.model.core.implementation;

import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.wsd.model.core.AbstractChromatogramPeakWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.IPeakModelWSD;

public class ChromatogramPeakWSD extends AbstractChromatogramPeakWSD {

	public ChromatogramPeakWSD(IPeakModelWSD peakModel, IChromatogramWSD chromatogram, String modelDescription) throws IllegalArgumentException, PeakException {

		super(peakModel, chromatogram, modelDescription);
	}

	public ChromatogramPeakWSD(IPeakModelWSD peakModel, IChromatogramWSD chromatogram) throws IllegalArgumentException, PeakException {

		super(peakModel, chromatogram);
	}
}