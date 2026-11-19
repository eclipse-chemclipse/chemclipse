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
package org.eclipse.chemclipse.chromatogram.xxd.quantitation.impl;

import org.eclipse.chemclipse.chromatogram.xxd.quantitation.settings.AbstractPeakQuantifierSettings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class AdjustCompensationSettings extends AbstractPeakQuantifierSettings {

	@JsonProperty(value = "Setpoint", defaultValue = "0.0")
	@JsonPropertyDescription(value = "That is the expected value e.g. for sample weight.")
	private double setpoint = 0.0d;
	@JsonProperty(value = "Actual Value", defaultValue = "0.0")
	@JsonPropertyDescription(value = "That is the actual value e.g. for sample weight.")
	private double actualValue = 0.0d;

	public double getSetpoint() {

		return setpoint;
	}

	public void setSetpoint(double setpoint) {

		this.setpoint = setpoint;
	}

	public double getActualValue() {

		return actualValue;
	}

	public void setActualValue(double actualValue) {

		this.actualValue = actualValue;
	}
}