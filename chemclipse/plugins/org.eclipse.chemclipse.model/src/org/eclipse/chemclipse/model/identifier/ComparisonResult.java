/*******************************************************************************
 * Copyright (c) 2010, 2026 Lablicate GmbH.
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

public class ComparisonResult extends AbstractComparisonResult {

	private static final long serialVersionUID = 6897854010927446632L;

	public static final IComparisonResult COMPARISON_RESULT_NO_MATCH = new ComparisonResultFinal(FACTOR_NO_MATCH, false);
	public static final IComparisonResult COMPARISON_RESULT_BEST_MATCH = new ComparisonResultFinal(FACTOR_BEST_MATCH, true);

	/**
	 * Creates a result of the given identification algorithm without any
	 * metric, e.g. for an algorithm that does not report match factors. The
	 * values are added via {@link #setMetric(String, double)}.
	 */
	public ComparisonResult(String algorithmId) {

		super(algorithmId);
	}

	public ComparisonResult(float matchFactor) {

		super(matchFactor);
	}

	public ComparisonResult(float matchFactor, float reverseMatchFactor, float matchFactorDirect, float reverseMatchFactorDirect) {

		super(matchFactor, reverseMatchFactor, matchFactorDirect, reverseMatchFactorDirect);
	}

	public ComparisonResult(float matchFactor, float reverseMatchFactor, float matchFactorDirect, float reverseMatchFactorDirect, float probability) {

		super(matchFactor, reverseMatchFactor, matchFactorDirect, reverseMatchFactorDirect, probability);
	}

	public ComparisonResult(IComparisonResult comparisonResult) {

		super(comparisonResult);
	}

	@Override
	public ComparisonResult setMatch(boolean match) {

		super.setMatch(match);
		return this;
	}

	@Override
	public String toString() {

		return "ComparisonResult [getAlgorithmId()=" + getAlgorithmId() + ", getPenalty()=" + getPenalty() + ", isMatch()=" + isMatch() + ", getMetricValues()=" + getMetricValues() + "]";
	}
}