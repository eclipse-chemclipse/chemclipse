/*******************************************************************************
 * Copyright (c) 2018, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class AbstractIdentifierDeltaPenaltyCalculationSettings extends AbstractIdentifierSettings implements IPenaltyCalculationSettings, IDeltaCalculationSettings {

	/**
	 * Delta Calculation
	 */
	@JsonProperty(value = "Delta Calculation", defaultValue = "NONE")
	@JsonPropertyDescription(value = "Select the strategy, how delta windows are applied to exclude peaks from the identification.")
	private DeltaCalculation deltaCalculation = DeltaCalculation.NONE;
	@JsonProperty(value = "Delta Window", defaultValue = "0")
	@JsonPropertyDescription(value = "Identify the peak if the unknown is inside of the delta window (delta -/+).")
	@FloatSettingsProperty(minValue = IDeltaCalculationSettings.MIN_DELTA_WINDOW, maxValue = IDeltaCalculationSettings.MAX_DELTA_WINDOW)
	private float deltaWindow = 0.0f;
	/**
	 * Penalty Calculation
	 */
	@JsonProperty(value = "Penalty Calculation", defaultValue = "NONE")
	@JsonPropertyDescription(value = "Select the strategy, how penalties are calculated.")
	private PenaltyCalculation penaltyCalculation = PenaltyCalculation.NONE;
	@JsonProperty(value = "Penalty Window", defaultValue = "0")
	@JsonPropertyDescription(value = "The penalty window. The unit of the selected penalty calculation is used.")
	@FloatSettingsProperty(minValue = IPenaltyCalculationSettings.MIN_PENALTY_WINDOW, maxValue = IPenaltyCalculationSettings.MAX_PENALTY_WINDOW)
	private float penaltyWindow = 0.0f;
	@JsonProperty(value = "Penalty Level Factor", defaultValue = "5.0")
	@JsonPropertyDescription(value = "The penalty level factor.")
	@FloatSettingsProperty(minValue = IPenaltyCalculationSettings.MIN_PENALTY_LEVEL_FACTOR, maxValue = IPenaltyCalculationSettings.MAX_PENALTY_LEVEL_FACTOR)
	private float penaltyLevelFactor = IPenaltyCalculationSettings.DEF_PENALTY_LEVEL_FACTOR;
	@JsonProperty(value = "Max Penalty", defaultValue = "20")
	@JsonPropertyDescription(value = "The max penalty. Values between 0 (no penalty) and 100 (max penalty) are allowed.")
	@FloatSettingsProperty(minValue = MIN_PENALTY_MATCH_FACTOR, maxValue = MAX_PENALTY_MATCH_FACTOR)
	private float maxPenalty = DEF_PENALTY_MATCH_FACTOR;

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

	@Override
	public PenaltyCalculation getPenaltyCalculation() {

		return penaltyCalculation;
	}

	@Override
	public void setPenaltyCalculation(PenaltyCalculation penaltyCalculation) {

		this.penaltyCalculation = penaltyCalculation;
	}

	@Override
	public float getPenaltyWindow() {

		return penaltyWindow;
	}

	@Override
	public void setPenaltyWindow(float penaltyWindow) {

		this.penaltyWindow = penaltyWindow;
	}

	@Override
	public float getPenaltyLevelFactor() {

		return penaltyLevelFactor;
	}

	@Override
	public void setPenaltyLevelFactor(float penaltyLevelFactor) {

		this.penaltyLevelFactor = penaltyLevelFactor;
	}

	@Override
	public float getMaxPenalty() {

		return maxPenalty;
	}

	@Override
	public void setMaxPenalty(float maxPenalty) {

		this.maxPenalty = maxPenalty;
	}
}
