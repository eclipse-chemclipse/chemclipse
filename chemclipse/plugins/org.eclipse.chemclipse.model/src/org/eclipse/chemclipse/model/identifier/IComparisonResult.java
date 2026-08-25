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
 * Alexander Kerner - implementation
 * Christoph Läubrich - add comparator static field
 * Matthias Mailänder - contribute the metrics per identification algorithm
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

/**
 * The result of a comparison of an unknown against a library entry.
 *
 * The values that an identification algorithm reports are stored as metrics,
 * see {@link #getMetricValues()}. Which metrics exist is defined by the
 * algorithm, see {@link IComparisonMetric} and the extension point
 * "org.eclipse.chemclipse.model.comparisonMetrics". The metrics of the
 * classical spectral match are additionally available via dedicated getters,
 * e.g. {@link #getMatchFactor()}.
 *
 * 0 = no match
 * 100 = perfect match
 */
public interface IComparisonResult extends Serializable, Comparable<IComparisonResult> {

	/**
	 * Ranks by the normalized score of the rating supplier, worst first.
	 */
	static Comparator<IComparisonResult> SCORE_COMPARATOR = (o1, o2) -> Float.compare(getScore(o1), getScore(o2));

	float FACTOR_BEST_MATCH = 100.0f;
	float FACTOR_NO_MATCH = 0.0f;

	float MAX_MATCH_FACTOR = FACTOR_BEST_MATCH;
	float MAX_REVERSE_MATCH_FACTOR = FACTOR_BEST_MATCH;

	float DEF_MAX_PENALTY = IPenaltyCalculationSettings.DEF_PENALTY_MATCH_FACTOR;
	float MIN_ALLOWED_PENALTY = IPenaltyCalculationSettings.MIN_PENALTY_MATCH_FACTOR;
	float MAX_ALLOWED_PENALTY = IPenaltyCalculationSettings.MAX_PENALTY_MATCH_FACTOR;

	float MIN_ALLOWED_PROBABILITY = 0.0f;
	float MAX_ALLOWED_PROBABILITY = 100.0f;

	/**
	 * Returns the score of the comparison result, mapping a rating supplier
	 * that reports no rating to {@link #FACTOR_NO_MATCH}.
	 */
	static float getScore(IComparisonResult comparisonResult) {

		float score = comparisonResult.getRatingSupplier().getScore();
		return Float.isNaN(score) ? FACTOR_NO_MATCH : score;
	}

	/**
	 * Identifies the algorithm that created this result, e.g. "classic" for the
	 * classical spectral match. The metrics and the rating supplier are
	 * resolved by this id, see {@link ComparisonMetricRegistry}.
	 */
	String getAlgorithmId();

	/**
	 * Returns the descriptors of the metrics this result carries, in the order
	 * in which they were reported by the algorithm.
	 */
	List<IComparisonMetric> getMetrics();

	/**
	 * Returns the metric values as they were reported by the algorithm, without
	 * applying the penalty. The map is unmodifiable, use
	 * {@link #setMetric(String, double)} to add a value.
	 */
	Map<String, Double> getMetricValues();

	/**
	 * Returns the value of the given metric, with the penalty applied if the
	 * metric declares it as applicable. Empty if the metric is not set.
	 */
	OptionalDouble getMetric(String metricId);

	/**
	 * Returns the value of the given metric without applying the penalty.
	 * Empty if the metric is not set.
	 */
	OptionalDouble getMetricNotAdjusted(String metricId);

	/**
	 * Sets the value of the given metric.
	 */
	void setMetric(String metricId, double value);

	boolean isMatch();

	IComparisonResult setMatch(boolean match);

	float getMatchFactor();

	float getMatchFactorNotAdjusted();

	float getMatchFactorDirect();

	float getMatchFactorDirectNotAdjusted();

	/**
	 * Sets a penalty. It's effectively the same as calling
	 * {@link #clearPenalty()} and {@link #addPenalty(float)}. {@code penalty}
	 * must be between MIN_ALLOWED_PENALTY and MAX_ALLOWED_PENALTY otherwise an
	 * exception is thrown.
	 *
	 * @param penalty
	 *            the penalty to set
	 * @throws IllegalArgumentException
	 *             if {@code penalty} is smaller than
	 *             {@link #MIN_ALLOWED_PENALTY} or larger than
	 *             {@link #MAX_ALLOWED_PENALTY}
	 */
	void setPenalty(float penalty);

	/**
	 * Adds given penalty to this {@code IComparisonResult}'s penalty.
	 */
	void addPenalty(float penalty);

	void clearPenalty();

	float getPenalty();

	float getReverseMatchFactor();

	float getReverseMatchFactorNotAdjusted();

	float getReverseMatchFactorDirect();

	float getReverseMatchFactorDirectNotAdjusted();

	float getProbability();

	float getInLibFactor();

	void setInLibFactor(float inLibFactor);

	IRatingSupplier getRatingSupplier();
}
