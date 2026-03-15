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
 * Christoph Läubrich - add delegate constructor
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.implementation;

import org.eclipse.chemclipse.model.core.IPeakIntensityValues;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.msd.model.core.AbstractPeakModelMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;

public class PeakModelMSD extends AbstractPeakModelMSD {

	/**
	 * Renew the UUID on change.
	 */
	private static final long serialVersionUID = -1550042043393366604L;

	public PeakModelMSD(IPeakMassSpectrum peakMaximum, IPeakIntensityValues peakIntensityValues) throws IllegalArgumentException, PeakException {

		this(peakMaximum, peakIntensityValues, 0, 0);
	}

	public PeakModelMSD(IPeakMassSpectrum peakMaximum, IPeakIntensityValues peakIntensityValues, float startBackgroundAbundance, float stopBackgroundAbundance) throws PeakException {

		super(peakMaximum, peakIntensityValues, startBackgroundAbundance, stopBackgroundAbundance);
	}
}
