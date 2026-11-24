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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.impl.settings;

import org.eclipse.chemclipse.chromatogram.msd.filter.settings.AbstractMassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.support.settings.DoubleSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class PrecursorAssignerFilterSettings extends AbstractMassSpectrumFilterSettings {

	@JsonProperty(value = "Precursor Ion", defaultValue = "0")
	@JsonPropertyDescription(value = "Define the precursor that is assigned.")
	@DoubleSettingsProperty(minValue = 0.0d, maxValue = 100000.0d)
	private double precursorIon = 0;

	@Override
	public boolean isValidFor(IScanMSD scanMSD) {

		if(scanMSD instanceof IRegularMassSpectrum regularMassSpectrum) {
			return regularMassSpectrum.getMassSpectrometer() > 1;
		}
		return false;
	}

	public double getPrecursorIon() {

		return precursorIon;
	}

	public void setPrecursorIon(double precursorIon) {

		this.precursorIon = precursorIon;
	}
}