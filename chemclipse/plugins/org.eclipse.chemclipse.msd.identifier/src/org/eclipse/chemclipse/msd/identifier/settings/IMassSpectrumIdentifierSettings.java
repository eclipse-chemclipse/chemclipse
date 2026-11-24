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
 * Matthias Mailänder - API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier.settings;

import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;

public interface IMassSpectrumIdentifierSettings extends IIdentifierSettings {

	/**
	 * Checks whether the identifier is able to handle the given mass spectrum.
	 * By default, only {@link MassSpectrumType#CENTROID} spectra are accepted.
	 * Identifiers that evaluate profile data need to override this method.
	 *
	 * @param scanMSD
	 *            the {@link IScanMSD} to check
	 * @return boolean
	 */
	default boolean isValidFor(IScanMSD scanMSD) {

		if(scanMSD instanceof IRegularMassSpectrum regularMassSpectrum) {
			return regularMassSpectrum.getMassSpectrumType() == MassSpectrumType.CENTROID;
		}
		return false;
	}
}
