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

import org.eclipse.chemclipse.model.l10n.ModelMessages;

/**
 * They are the default metric set of an {@link IComparisonResult}.
 */
public class ComparisonMetricsClassic {

	/**
	 * Algorithm id of the classical spectral match.
	 */
	public static final String ALGORITHM_CLASSIC = "classic"; //$NON-NLS-1$

	public static final String MATCH_FACTOR = "matchFactor"; //$NON-NLS-1$
	public static final String REVERSE_MATCH_FACTOR = "reverseMatchFactor"; //$NON-NLS-1$
	public static final String MATCH_FACTOR_DIRECT = "matchFactorDirect"; //$NON-NLS-1$
	public static final String REVERSE_MATCH_FACTOR_DIRECT = "reverseMatchFactorDirect"; //$NON-NLS-1$
	public static final String PROBABILITY = "probability"; //$NON-NLS-1$
	public static final String IN_LIB_FACTOR = "inLibFactor"; //$NON-NLS-1$

	private static final List<IComparisonMetric> METRICS = createMetrics();

	private ComparisonMetricsClassic() {

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
		metrics.add(new ComparisonMetric(MATCH_FACTOR, ModelMessages.matchFactor, ModelMessages.similarityOfUnknownToLibrarySpectrum, "0.0", true, ComparisonMetric.HIGHER_IS_BETTER)); //$NON-NLS-1$
		metrics.add(new ComparisonMetric(REVERSE_MATCH_FACTOR, ModelMessages.reverseMatchFactor, ModelMessages.similarityIgnoringAbsentIons, "0.0", true, ComparisonMetric.HIGHER_IS_BETTER)); //$NON-NLS-1$
		metrics.add(new ComparisonMetric(MATCH_FACTOR_DIRECT, ModelMessages.matchFactorDirect, ModelMessages.matchFactorWithoutPreprocessing, "0.0", true, ComparisonMetric.HIGHER_IS_BETTER)); //$NON-NLS-1$
		metrics.add(new ComparisonMetric(REVERSE_MATCH_FACTOR_DIRECT, ModelMessages.reverseMatchFactorDirect, ModelMessages.reverseMatchFactorWithoutPreprocessing, "0.0", true, ComparisonMetric.HIGHER_IS_BETTER)); //$NON-NLS-1$
		metrics.add(new ComparisonMetric(PROBABILITY, ModelMessages.probability, ModelMessages.probabilityHitIsCorrect, "0.0", false, ComparisonMetric.HIGHER_IS_BETTER)); //$NON-NLS-1$
		metrics.add(new ComparisonMetric(IN_LIB_FACTOR, ModelMessages.inLibFactor, ModelMessages.rankOfInLibHit, "0.0", false, ComparisonMetric.HIGHER_IS_BETTER)); //$NON-NLS-1$

		return Collections.unmodifiableList(metrics);
	}
}
