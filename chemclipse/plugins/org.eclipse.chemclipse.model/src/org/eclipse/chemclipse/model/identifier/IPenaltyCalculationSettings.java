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
 * Alexander Kerner - implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import org.eclipse.chemclipse.model.settings.IProcessSettings;

public interface IPenaltyCalculationSettings extends IProcessSettings {

	float DEF_LIMIT_MATCH_FACTOR = 100.0f;
	float MIN_LIMIT_MATCH_FACTOR = 0.0f;
	float MAX_LIMIT_MATCH_FACTOR = 100.0f;
	//
	float MIN_PENALTY_WINDOW = 0.0f;
	float MAX_PENALTY_WINDOW = Float.MAX_VALUE;
	//
	float DEF_PENALTY_LEVEL_FACTOR = 5.0f;
	float MIN_PENALTY_LEVEL_FACTOR = 1.0f;
	float MAX_PENALTY_LEVEL_FACTOR = 1000.0f;
	//
	float DEF_PENALTY_MATCH_FACTOR = 20.0f;
	float MIN_PENALTY_MATCH_FACTOR = 0.0f;
	float MAX_PENALTY_MATCH_FACTOR = 100.0f;

	/**
	 * Limit Match Factor
	 * 
	 * @return float
	 */
	float getLimitMatchFactor();

	/**
	 * Only identify the peak if no target is available with a match factor >= the limit.
	 * 
	 * @param limitMatchFactor
	 */
	void setLimitMatchFactor(float limitMatchFactor);

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
}
