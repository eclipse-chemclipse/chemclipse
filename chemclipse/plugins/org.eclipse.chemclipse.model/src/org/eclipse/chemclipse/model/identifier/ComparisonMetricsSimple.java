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
 * They are the default metric set of an {@link IComparisonResult}
 * when there is just a single match factor.
 */
public class ComparisonMetricsSimple {

	/**
	 * Algorithm id of the simple matching.
	 */
	public static final String ALGORITHM_SIMPLE = "simple";

	/**
	 * Just a single match factor
	 */
	public static final String MATCH_FACTOR = "matchFactor";

	private static final List<IComparisonMetric> METRICS = createMetrics();

	private ComparisonMetricsSimple() {

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
		metrics.add(new ComparisonMetric(MATCH_FACTOR, "Match Factor", "Similarity of the unknown to the library entry.", "0.0", true, ComparisonMetric.HIGHER_IS_BETTER));

		return Collections.unmodifiableList(metrics);
	}
}
