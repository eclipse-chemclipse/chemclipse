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
package org.eclipse.chemclipse.model.identifier;

import java.util.List;

import org.eclipse.chemclipse.support.literature.LiteratureReference;

public class PenaltyCalculationSettings implements IPenaltyCalculationSettings {

	private PenaltyCalculation penaltyCalculation = PenaltyCalculation.NONE;
	private float penaltyWindow = 0.0f;
	private float penaltyLevelFactor = IPenaltyCalculationSettings.DEF_PENALTY_LEVEL_FACTOR;
	private float maxPenalty = IPenaltyCalculationSettings.DEF_PENALTY_MATCH_FACTOR;
	private float penaltyMissingReference = 0.0f;

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

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		return null;
	}
}