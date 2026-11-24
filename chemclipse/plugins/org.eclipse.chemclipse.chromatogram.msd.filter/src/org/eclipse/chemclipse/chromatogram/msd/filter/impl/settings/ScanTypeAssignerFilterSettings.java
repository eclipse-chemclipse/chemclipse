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
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.support.settings.ShortSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class ScanTypeAssignerFilterSettings extends AbstractMassSpectrumFilterSettings {

	@JsonProperty(value = "Mass Spectrometer", defaultValue = "1")
	@JsonPropertyDescription(value = "MS1, MS2, ...")
	@ShortSettingsProperty(minValue = 1, maxValue = 100)
	private short massSpectrometer = 1;

	@JsonProperty(value = "Mass Spectrum Type", defaultValue = "CENTROID")
	@JsonPropertyDescription(value = "Centroid or Profile")
	private MassSpectrumType massSpectrumType = MassSpectrumType.CENTROID;

	public short getMassSpectrometer() {

		return massSpectrometer;
	}

	public void setMassSpectrometer(short massSpectrometer) {

		this.massSpectrometer = massSpectrometer;
	}

	public MassSpectrumType getMassSpectrumType() {

		return massSpectrumType;
	}

	public void setMassSpectrumType(MassSpectrumType massSpectrumType) {

		this.massSpectrumType = massSpectrumType;
	}
}