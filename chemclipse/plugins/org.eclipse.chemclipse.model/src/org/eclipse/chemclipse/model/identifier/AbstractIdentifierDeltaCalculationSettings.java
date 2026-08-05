/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.identifier;

import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class AbstractIdentifierDeltaCalculationSettings extends AbstractIdentifierSettings implements IDeltaCalculationSettings {

	/**
	 * Delta Calculation
	 */
	@JsonProperty(value = "Delta Calculation", defaultValue = "NONE")
	@JsonPropertyDescription(value = "Select the strategy, how delta windows are applied to exclude peaks from the identification.")
	private DeltaCalculation deltaCalculation = DeltaCalculation.NONE;
	@JsonProperty(value = "Delta Window", defaultValue = "0")
	@JsonPropertyDescription(value = "Identify the peak if the unknown is inside of the delta window (delta -/+).")
	@FloatSettingsProperty(minValue = MIN_DELTA_WINDOW, maxValue = MAX_DELTA_WINDOW)
	private float deltaWindow = 0.0f;

	@Override
	public DeltaCalculation getDeltaCalculation() {

		return deltaCalculation;
	}

	@Override
	public void setDeltaCalculation(DeltaCalculation deltaCalculation) {

		this.deltaCalculation = deltaCalculation;
	}

	@Override
	public float getDeltaWindow() {

		return deltaWindow;
	}

	@Override
	public void setDeltaWindow(float deltaWindow) {

		this.deltaWindow = deltaWindow;
	}
}