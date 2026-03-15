/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.implementation;

import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.msd.model.core.AbstractChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;

public class ChromatogramPeakMSD extends AbstractChromatogramPeakMSD {

	public ChromatogramPeakMSD(IPeakModelMSD peakModel, IChromatogramMSD chromatogram) throws PeakException {

		super(peakModel, chromatogram);
	}

	public ChromatogramPeakMSD(IPeakModelMSD peakModel, IChromatogramMSD chromatogram, String modelDescription) throws PeakException {

		super(peakModel, chromatogram, modelDescription);
	}
}