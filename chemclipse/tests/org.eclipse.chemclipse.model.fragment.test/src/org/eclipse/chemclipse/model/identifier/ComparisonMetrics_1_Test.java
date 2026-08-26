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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ComparisonMetrics_1_Test {

	private static final String ALGORITHM_FOO = "foo";
	private static final String METRIC_BAR = "foo.bar";

	@Test
	public void testDefaultAlgorithm() {

		IComparisonResult comparisonResult = new ComparisonResult(80.0f, 70.0f, 60.0f, 50.0f);
		assertEquals(ComparisonMetricsClassic.ALGORITHM_CLASSIC, comparisonResult.getAlgorithmId());
	}

	@Test
	public void testLegacyGettersReadTheMetrics() {

		IComparisonResult comparisonResult = new ComparisonResult(80.0f, 70.0f, 60.0f, 50.0f);
		assertEquals(80.0d, comparisonResult.getMetricNotAdjusted(ComparisonMetricsClassic.MATCH_FACTOR).getAsDouble(), 0);
		assertEquals(70.0d, comparisonResult.getMetricNotAdjusted(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR).getAsDouble(), 0);
		assertEquals(60.0d, comparisonResult.getMetricNotAdjusted(ComparisonMetricsClassic.MATCH_FACTOR_DIRECT).getAsDouble(), 0);
		assertEquals(50.0d, comparisonResult.getMetricNotAdjusted(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR_DIRECT).getAsDouble(), 0);
	}

	@Test
	public void testSetInLibFactorIsStoredAsMetric() {

		IComparisonResult comparisonResult = new ComparisonResult(80.0f);
		comparisonResult.setInLibFactor(3.0f);
		assertEquals(3.0f, comparisonResult.getInLibFactor(), 0);
		assertEquals(3.0d, comparisonResult.getMetricNotAdjusted(ComparisonMetricsClassic.IN_LIB_FACTOR).getAsDouble(), 0);
	}

	@Test
	public void testPenaltyAppliesToTheMatchFactorOnly() {

		IComparisonResult comparisonResult = new ComparisonResult(80.0f, 70.0f, 60.0f, 50.0f, 90.0f);
		comparisonResult.setPenalty(10.0f);
		/*
		 * The match factor declares the penalty as applicable, the probability does not.
		 */
		assertEquals(70.0d, comparisonResult.getMetric(ComparisonMetricsClassic.MATCH_FACTOR).getAsDouble(), 0);
		assertEquals(80.0d, comparisonResult.getMetricNotAdjusted(ComparisonMetricsClassic.MATCH_FACTOR).getAsDouble(), 0);
		assertEquals(90.0d, comparisonResult.getMetric(ComparisonMetricsClassic.PROBABILITY).getAsDouble(), 0);
	}

	@Test
	public void testUnknownMetricIsEmpty() {

		IComparisonResult comparisonResult = new ComparisonResult(80.0f);
		assertTrue(comparisonResult.getMetric(METRIC_BAR).isEmpty());
	}

	@Test
	public void testAlgorithmSpecificMetrics() {

		IComparisonResult comparisonResult = new ComparisonResult(ALGORITHM_FOO);
		comparisonResult.setMetric(METRIC_BAR, 1.0e-42d);
		assertEquals(ALGORITHM_FOO, comparisonResult.getAlgorithmId());
		assertEquals(1, comparisonResult.getMetricValues().size());
		assertEquals(1.0e-42d, comparisonResult.getMetricNotAdjusted(METRIC_BAR).getAsDouble(), 0);
		/*
		 * No match factor has been reported by this algorithm.
		 */
		assertEquals(0.0f, comparisonResult.getMatchFactor(), 0);
	}

	@Test
	public void testUnknownMetricFallsBackToAGenericDescriptor() {

		IComparisonResult comparisonResult = new ComparisonResult(ALGORITHM_FOO);
		comparisonResult.setMetric(METRIC_BAR, 42.0d);
		List<IComparisonMetric> metrics = comparisonResult.getMetrics();
		assertEquals(1, metrics.size());
		IComparisonMetric metric = metrics.getFirst();
		assertEquals(METRIC_BAR, metric.getId());
		assertEquals(METRIC_BAR, metric.getLabel());
		assertFalse(metric.isPenaltyApplicable());
	}

	@Test
	public void testMetricsAreReportedInDeclaredOrder() {

		IComparisonResult comparisonResult = new ComparisonResult(80.0f);
		List<IComparisonMetric> metrics = comparisonResult.getMetrics();
		assertEquals(6, metrics.size());
		assertEquals(ComparisonMetricsClassic.MATCH_FACTOR, metrics.get(0).getId());
		assertEquals(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR, metrics.get(1).getId());
		assertEquals(ComparisonMetricsClassic.MATCH_FACTOR_DIRECT, metrics.get(2).getId());
		assertEquals(ComparisonMetricsClassic.REVERSE_MATCH_FACTOR_DIRECT, metrics.get(3).getId());
		assertEquals(ComparisonMetricsClassic.PROBABILITY, metrics.get(4).getId());
		assertEquals(ComparisonMetricsClassic.IN_LIB_FACTOR, metrics.get(5).getId());
	}

	@Test
	public void testTheFinalResultRejectsMetrics() {

		IComparisonResult comparisonResult = ComparisonResult.COMPARISON_RESULT_BEST_MATCH;
		comparisonResult.setMetric(ComparisonMetricsClassic.MATCH_FACTOR, 0.0d);
		assertEquals(IComparisonResult.FACTOR_BEST_MATCH, comparisonResult.getMatchFactor(), 0);
	}

	@Test
	public void testEValueRanksLowerFirst() {

		IComparisonMetric evalue = new ComparisonMetric("e", "E-Value", "", "0.###E0", false, ComparisonMetric.LOWER_IS_BETTER);
		assertTrue(evalue.getComparator().compare(1.0e-50d, 1.0d) < 0);
	}
}
