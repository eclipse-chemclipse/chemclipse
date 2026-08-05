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
 * Alexander Kerner - implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import org.eclipse.chemclipse.model.settings.IProcessSettings;

public interface IPenaltyCalculationSettings extends IProcessSettings {

	float DEF_LIMIT_MATCH_FACTOR = 100.0f;
	float MIN_LIMIT_MATCH_FACTOR = 0.0f;
	float MAX_LIMIT_MATCH_FACTOR = 100.0f;

	float MIN_PENALTY_WINDOW = 0.0f;
	float MAX_PENALTY_WINDOW = Float.MAX_VALUE;

	float DEF_PENALTY_LEVEL_FACTOR = 5.0f;
	float MIN_PENALTY_LEVEL_FACTOR = 1.0f;
	float MAX_PENALTY_LEVEL_FACTOR = 1000.0f;

	float DEF_PENALTY_MATCH_FACTOR = 20.0f;
	float MIN_PENALTY_MATCH_FACTOR = 0.0f;
	float MAX_PENALTY_MATCH_FACTOR = 100.0f;

	/**
	 * Retention Time / Index Penalty Calculation
	 */
	PenaltyCalculation getPenaltyCalculation();

	void setPenaltyCalculation(PenaltyCalculation penaltyCalculation);

	float getPenaltyWindow();

	void setPenaltyWindow(float penaltyWindow);

	float getPenaltyLevelFactor();

	void setPenaltyLevelFactor(float penaltyLevelFactor);

	float getMaxPenalty();

	void setMaxPenalty(float maxValue);

	float getPenaltyMissingReference();

	void setPenaltyMissingReference(float penalty);
}