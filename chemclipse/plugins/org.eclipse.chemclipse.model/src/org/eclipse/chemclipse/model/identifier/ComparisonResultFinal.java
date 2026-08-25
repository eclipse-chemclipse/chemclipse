/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
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

/**
 * The final comparison result is used to set a best or no match.
 * The match factor ... shall be not editable.
 */
public final class ComparisonResultFinal extends ComparisonResult {

	private static final long serialVersionUID = 1071555450387053384L;

	public ComparisonResultFinal(float matchFactor, boolean match) {

		super(matchFactor);
		super.setMatch(match);
	}

	@Override
	public ComparisonResult setMatch(boolean match) {

		return this;
	}

	@Override
	public void clearPenalty() {

		// No action
	}

	@Override
	public void setPenalty(float penalty) {

		// No action
	}

	@Override
	public void addPenalty(float penalty) {

		// No action
	}

	@Override
	public void setMetric(String metricId, double value) {

		// No action
	}

	@Override
	public void setInLibFactor(float inLibFactor) {

		// No action
	}

	@Override
	protected void setMatchFactor(float matchFactor) {

		// No action
	}

	@Override
	protected void setMatchFactorDirect(float matchFactorDirect) {

		// No action
	}

	@Override
	protected void setReverseMatchFactor(float reverseMatchFactor) {

		// No action
	}

	@Override
	protected void setReverseMatchFactorDirect(float reverseMatchFactorDirect) {

		// No action
	}
}