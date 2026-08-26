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
 * Christoph Läubrich - getPenalty and Matchfactors should be accessed by getters and not direct field access
 * Matthias Mailänder - store the values as metrics of an identification algorithm
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

public abstract class AbstractComparisonResult implements IComparisonResult {

	/**
	 * Renew the UUID on change.
	 */
	private static final long serialVersionUID = 7748523860423928004L;

	private final String algorithmId;
	private final Map<String, Double> metrics = new LinkedHashMap<>();
	private boolean isMatch = false;
	private float penalty = 0.0f;
	/*
	 * The rating supplier is resolved from the algorithm id and is therefore
	 * not part of the serialized state. Only the algorithm id is stored, so
	 * that a result which is loaded again is rated by the algorithm that has
	 * created it and not by the default rating supplier.
	 */
	private transient IRatingSupplier ratingSupplier = null;

	/**
	 * Creates a result of the given identification algorithm without any
	 * metric. The algorithm sets its metrics via
	 * {@link #setMetric(String, double)}.
	 *
	 * @param algorithmId
	 */
	protected AbstractComparisonResult(String algorithmId) {

		this.algorithmId = (algorithmId != null && !algorithmId.isEmpty()) ? algorithmId : ComparisonMetricsClassic.ALGORITHM_CLASSIC;
	}

	protected AbstractComparisonResult(float matchFactor) {

		this(ComparisonMetricsSimple.ALGORITHM_SIMPLE);
		putMetric(ComparisonMetricsClassic.MATCH_FACTOR, matchFactor);
	}

	protected AbstractComparisonResult(float matchFactor, float reverseMatchFactor, float matchFactorDirect, float reverseMatchFactorDirect) {

		this(matchFactor, reverseMatchFactor, matchFactorDirect, reverseMatchFactorDirect, MAX_ALLOWED_PROBABILITY);
	}

	protected AbstractComparisonResult(float matchFactor, float reverseMatchFactor, float matchFactorDirect, float reverseMatchFactorDirect, float probability) {

		this(ComparisonMetricsClassic.ALGORITHM_CLASSIC);
		putMetric(ComparisonMetricsClassic.MATCH_FACTOR, matchFactor);
		putMetric(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR, reverseMatchFactor);
		putMetric(ComparisonMetricsClassic.MATCH_FACTOR_DIRECT, matchFactorDirect);
		putMetric(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR_DIRECT, reverseMatchFactorDirect);
		setProbability(probability);
		putMetric(ComparisonMetricsClassic.IN_LIB_FACTOR, 0.0d);
	}

	protected AbstractComparisonResult(IComparisonResult comparisonResult) {

		this(comparisonResult.getAlgorithmId());
		metrics.putAll(comparisonResult.getMetricValues());
		this.penalty = comparisonResult.getPenalty();
	}

	@Override
	public String getAlgorithmId() {

		return algorithmId;
	}

	@Override
	public List<IComparisonMetric> getMetrics() {

		List<IComparisonMetric> comparisonMetrics = new ArrayList<>();
		for(String metricId : metrics.keySet()) {
			comparisonMetrics.add(ComparisonMetricRegistry.getMetric(algorithmId, metricId));
		}

		return Collections.unmodifiableList(comparisonMetrics);
	}

	@Override
	public Map<String, Double> getMetricValues() {

		return Collections.unmodifiableMap(metrics);
	}

	@Override
	public OptionalDouble getMetric(String metricId) {

		OptionalDouble value = getMetricNotAdjusted(metricId);
		if(value.isEmpty()) {
			return value;
		}

		if(ComparisonMetricRegistry.getMetric(algorithmId, metricId).isPenaltyApplicable()) {
			return OptionalDouble.of(Math.max(0.0d, value.getAsDouble() - getPenalty()));
		}

		return value;
	}

	@Override
	public OptionalDouble getMetricNotAdjusted(String metricId) {

		Double value = metrics.get(metricId);
		return value != null ? OptionalDouble.of(value.doubleValue()) : OptionalDouble.empty();
	}

	@Override
	public void setMetric(String metricId, double value) {

		putMetric(metricId, value);
	}

	@Override
	public float getPenalty() {

		return penalty;
	}

	@Override
	public void clearPenalty() {

		setPenalty(0);
	}

	@Override
	public void setPenalty(float penalty) {

		if(penalty >= MIN_ALLOWED_PENALTY && penalty <= MAX_ALLOWED_PENALTY) {
			this.penalty = penalty;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void addPenalty(float penalty) {

		float newPenalty = getPenalty() + penalty;
		if(newPenalty > MAX_ALLOWED_PENALTY) {
			setPenalty(MAX_ALLOWED_PENALTY);
		} else if(newPenalty < MIN_ALLOWED_PENALTY) {
			setPenalty(MIN_ALLOWED_PENALTY);
		} else {
			setPenalty(newPenalty);
		}
	}

	@Override
	public boolean isMatch() {

		return isMatch;
	}

	@Override
	public AbstractComparisonResult setMatch(boolean match) {

		this.isMatch = match;
		return this;
	}

	@Override
	public final float getMatchFactor() {

		return getAdjustedValue(getMatchFactorNotAdjusted(), getPenalty());
	}

	@Override
	public final float getMatchFactorDirect() {

		return getAdjustedValue(getMatchFactorDirectNotAdjusted(), getPenalty());
	}

	@Override
	public float getMatchFactorNotAdjusted() {

		return getMetricValue(ComparisonMetricsClassic.MATCH_FACTOR);
	}

	@Override
	public float getMatchFactorDirectNotAdjusted() {

		return getMetricValue(ComparisonMetricsClassic.MATCH_FACTOR_DIRECT);
	}

	@Override
	public final float getReverseMatchFactor() {

		return getAdjustedValue(getReverseMatchFactorNotAdjusted(), getPenalty());
	}

	@Override
	public final float getReverseMatchFactorDirect() {

		return getAdjustedValue(getReverseMatchFactorDirectNotAdjusted(), getPenalty());
	}

	@Override
	public float getReverseMatchFactorNotAdjusted() {

		return getMetricValue(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR);
	}

	@Override
	public float getReverseMatchFactorDirectNotAdjusted() {

		return getMetricValue(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR_DIRECT);
	}

	@Override
	public float getProbability() {

		return getMetricValue(ComparisonMetricsClassic.PROBABILITY);
	}

	@Override
	public float getInLibFactor() {

		return getMetricValue(ComparisonMetricsClassic.IN_LIB_FACTOR);
	}

	@Override
	public void setInLibFactor(float inLibFactor) {

		setMetric(ComparisonMetricsClassic.IN_LIB_FACTOR, inLibFactor);
	}

	@Override
	public IRatingSupplier getRatingSupplier() {

		if(ratingSupplier == null) {
			ratingSupplier = ComparisonMetricRegistry.createRatingSupplier(algorithmId);
			ratingSupplier.updateComparisonResult(this);
		}

		return ratingSupplier;
	}

	public static float getAdjustedValue(float value, float penalty) {

		float result = value - penalty;
		if(result < 0) {
			return 0;
		}

		return result;
	}

	@Override
	public int compareTo(IComparisonResult comparisonResult) {

		int result = Boolean.compare(this.isMatch(), comparisonResult.isMatch());
		if(result == 0) {
			result = Float.compare(IComparisonResult.getScore(this), IComparisonResult.getScore(comparisonResult));
		}

		return result;
	}

	protected void setMatchFactor(float matchFactor) {

		putMetric(ComparisonMetricsClassic.MATCH_FACTOR, matchFactor);
	}

	protected void setMatchFactorDirect(float matchFactorDirect) {

		putMetric(ComparisonMetricsClassic.MATCH_FACTOR_DIRECT, matchFactorDirect);
	}

	protected void setReverseMatchFactor(float reverseMatchFactor) {

		putMetric(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR, reverseMatchFactor);
	}

	protected void setReverseMatchFactorDirect(float reverseMatchFactorDirect) {

		putMetric(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR_DIRECT, reverseMatchFactorDirect);
	}

	private float getMetricValue(String metricId) {

		Double value = metrics.get(metricId);
		return value != null ? value.floatValue() : 0.0f;
	}

	private void putMetric(String metricId, double value) {

		metrics.put(metricId, Double.valueOf(value));
	}

	private void setProbability(float probability) {

		if(probability >= MIN_ALLOWED_PROBABILITY && probability <= MAX_ALLOWED_PROBABILITY) {
			putMetric(ComparisonMetricsClassic.PROBABILITY, probability);
		} else {
			putMetric(ComparisonMetricsClassic.PROBABILITY, 0.0d);
		}
	}
}
