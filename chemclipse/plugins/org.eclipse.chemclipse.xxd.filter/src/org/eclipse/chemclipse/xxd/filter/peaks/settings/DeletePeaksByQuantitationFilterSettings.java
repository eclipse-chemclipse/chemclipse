/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.filter.peaks.settings;

import org.eclipse.chemclipse.support.settings.DoubleSettingsProperty;
import org.eclipse.chemclipse.support.settings.StringSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class DeletePeaksByQuantitationFilterSettings {

	@JsonProperty(value = "Name", defaultValue = "")
	@JsonPropertyDescription(value = "Delete the peaks with a quantitation of the given name or all if empty.")
	@StringSettingsProperty(allowEmpty = true)
	private String name = "";
	@JsonProperty(value = "Min. Concentration", defaultValue = "0.0")
	@JsonPropertyDescription(value = "Delete the peaks with a concentration lower the given limit.")
	@DoubleSettingsProperty(minValue = 0, maxValue = Double.MAX_VALUE)
	private double concentration = 0.0d;
	@JsonProperty(value = "Unit", defaultValue = "")
	@JsonPropertyDescription(value = "Delete the peaks with a quantitation of the given unit.")
	@StringSettingsProperty(allowEmpty = false)
	private String unit = "";

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public double getConcentration() {

		return concentration;
	}

	public void setConcentration(double concentration) {

		this.concentration = concentration;
	}

	public String getUnit() {

		return unit;
	}

	public void setUnit(String unit) {

		this.unit = unit;
	}
}