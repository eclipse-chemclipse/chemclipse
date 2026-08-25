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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The metrics of the classical spectral match, shared by the NIST search, the
 * file based identifier and the distance, entropy and alfassi comparators.
 * They are the default metric set of an {@link IComparisonResult}.
 */
public class ComparisonMetrics {

	/**
	 * Algorithm id of the classical spectral match.
	 */
	public static final String ALGORITHM_CLASSIC = "classic";

	public static final String MATCH_FACTOR = "matchFactor";
	public static final String REVERSE_MATCH_FACTOR = "reverseMatchFactor";
	public static final String MATCH_FACTOR_DIRECT = "matchFactorDirect";
	public static final String REVERSE_MATCH_FACTOR_DIRECT = "reverseMatchFactorDirect";
	public static final String PROBABILITY = "probability";
	public static final String IN_LIB_FACTOR = "inLibFactor";

	private static final List<IComparisonMetric> METRICS = createMetrics();

	private ComparisonMetrics() {

	}

	/**
	 * Returns the metrics of the classical spectral match in display order.
	 *
	 * @return {@link List}
	 */
	public static List<IComparisonMetric> getMetrics() {

		return METRICS;
	}

	private static List<IComparisonMetric> createMetrics() {

		List<IComparisonMetric> metrics = new ArrayList<>();
		metrics.add(new ComparisonMetric(MATCH_FACTOR, "Match Factor", "Similarity of the unknown spectrum to the library spectrum.", "0.0", true, ComparisonMetric.HIGHER_IS_BETTER));
		metrics.add(new ComparisonMetric(REVERSE_MATCH_FACTOR, "Reverse Match Factor", "Similarity, ignoring ions that are absent in the library spectrum.", "0.0", true, ComparisonMetric.HIGHER_IS_BETTER));
		metrics.add(new ComparisonMetric(MATCH_FACTOR_DIRECT, "Match Factor Direct", "Match factor without pre-processing of the spectra.", "0.0", true, ComparisonMetric.HIGHER_IS_BETTER));
		metrics.add(new ComparisonMetric(REVERSE_MATCH_FACTOR_DIRECT, "Reverse Match Factor Direct", "Reverse match factor without pre-processing of the spectra.", "0.0", true, ComparisonMetric.HIGHER_IS_BETTER));
		metrics.add(new ComparisonMetric(PROBABILITY, "Probability", "Probability that the hit is correct, relative to the other hits.", "0.0", false, ComparisonMetric.HIGHER_IS_BETTER));
		metrics.add(new ComparisonMetric(IN_LIB_FACTOR, "InLib Factor", "Rank of the hit reported by an InLib search.", "0.0", false, ComparisonMetric.HIGHER_IS_BETTER));

		return Collections.unmodifiableList(metrics);
	}
}
