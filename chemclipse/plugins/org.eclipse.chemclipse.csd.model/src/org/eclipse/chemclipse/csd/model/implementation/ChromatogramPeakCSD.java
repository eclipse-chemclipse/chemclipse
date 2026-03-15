/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.csd.model.implementation;

import org.eclipse.chemclipse.csd.model.core.AbstractChromatogramPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakModelCSD;
import org.eclipse.chemclipse.model.exceptions.PeakException;

public class ChromatogramPeakCSD extends AbstractChromatogramPeakCSD {

	public ChromatogramPeakCSD(IPeakModelCSD peakModel, IChromatogramCSD chromatogram) throws IllegalArgumentException, PeakException {

		super(peakModel, chromatogram);
	}

	public ChromatogramPeakCSD(IPeakModelCSD peakModel, IChromatogramCSD chromatogram, String modelDescription) throws IllegalArgumentException, PeakException {

		super(peakModel, chromatogram, modelDescription);
	}
}