/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.vsd.model.implementation;

import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.vsd.model.core.AbstractChromatogramPeakVSD;
import org.eclipse.chemclipse.vsd.model.core.IChromatogramVSD;
import org.eclipse.chemclipse.vsd.model.core.IPeakModelVSD;

public class ChromatogramPeakVSD extends AbstractChromatogramPeakVSD {

	public ChromatogramPeakVSD(IPeakModelVSD peakModel, IChromatogramVSD chromatogram, String modelDescription) throws IllegalArgumentException, PeakException {

		super(peakModel, chromatogram, modelDescription);
	}

	public ChromatogramPeakVSD(IPeakModelVSD peakModel, IChromatogramVSD chromatogram) throws IllegalArgumentException, PeakException {

		super(peakModel, chromatogram);
	}
}