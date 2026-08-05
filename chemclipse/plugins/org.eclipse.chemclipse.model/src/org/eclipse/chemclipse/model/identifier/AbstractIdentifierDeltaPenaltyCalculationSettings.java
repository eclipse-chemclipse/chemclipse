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

public class AbstractIdentifierDeltaPenaltyCalculationSettings extends AbstractIdentifierDeltaCalculationSettings implements IPenaltyCalculationSettings {

	/**
	 * Penalty Calculation
	 */
	@JsonProperty(value = "Penalty Calculation", defaultValue = "NONE")
	@JsonPropertyDescription(value = "Select the strategy, how penalties are calculated.")
	private PenaltyCalculation penaltyCalculation = PenaltyCalculation.NONE;
	@JsonProperty(value = "Penalty Window", defaultValue = "0")
	@JsonPropertyDescription(value = "The penalty window. The unit of the selected penalty calculation is used.")
	@FloatSettingsProperty(minValue = MIN_PENALTY_WINDOW, maxValue = MAX_PENALTY_WINDOW)
	private float penaltyWindow = 0.0f;
	@JsonProperty(value = "Penalty Level Factor", defaultValue = "5.0")
	@JsonPropertyDescription(value = "The penalty level factor.")
	@FloatSettingsProperty(minValue = MIN_PENALTY_LEVEL_FACTOR, maxValue = MAX_PENALTY_LEVEL_FACTOR)
	private float penaltyLevelFactor = DEF_PENALTY_LEVEL_FACTOR;
	@JsonProperty(value = "Max Penalty", defaultValue = "20")
	@JsonPropertyDescription(value = "The max penalty. Values between 0 (no penalty) and 100 (max penalty) are allowed.")
	@FloatSettingsProperty(minValue = MIN_PENALTY_MATCH_FACTOR, maxValue = MAX_PENALTY_MATCH_FACTOR)
	private float maxPenalty = DEF_PENALTY_MATCH_FACTOR;
	@JsonProperty(value = "Penalty Missing Reference", defaultValue = "0")
	@JsonPropertyDescription(value = "If for example the reference retention index is not available, add the given penalty. Values between 0 (no penalty) and 100 (max penalty) are allowed.")
	@FloatSettingsProperty(minValue = MIN_PENALTY_MATCH_FACTOR, maxValue = MAX_PENALTY_MATCH_FACTOR)
	private float penaltyMissingReference = 0;

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

	@Override
	public float getPenaltyMissingReference() {

		return penaltyMissingReference;
	}

	@Override
	public void setPenaltyMissingReference(float penaltyMissingReference) {

		this.penaltyMissingReference = penaltyMissingReference;
	}
}