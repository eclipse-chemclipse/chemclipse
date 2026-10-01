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
package org.eclipse.chemclipse.msd.identifier.comparison;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;

/**
 * The mass spectrum a settings dialogue is about to be opened for.
 */
public class MassSpectrumComparatorContext {

	private static IScanMSD massSpectrum = null;

	private MassSpectrumComparatorContext() {

	}

	public static IScanMSD getMassSpectrum() {

		return massSpectrum;
	}

	public static void setMassSpectrum(IScanMSD massSpectrum) {

		MassSpectrumComparatorContext.massSpectrum = massSpectrum;
	}
}
