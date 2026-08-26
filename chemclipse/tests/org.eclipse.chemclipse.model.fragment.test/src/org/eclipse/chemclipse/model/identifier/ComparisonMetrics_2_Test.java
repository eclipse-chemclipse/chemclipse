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

import org.junit.jupiter.api.Test;

public class ComparisonMetrics_2_Test {

	@Test
	public void testDefaultAlgorithm() {

		IComparisonResult comparisonResult = new ComparisonResult(42);
		assertEquals(ComparisonMetricsSimple.ALGORITHM_SIMPLE, comparisonResult.getAlgorithmId());
	}

	@Test
	public void testLegacyGettersReadTheMetrics() {

		IComparisonResult comparisonResult = new ComparisonResult(42);
		assertEquals(42, comparisonResult.getMetricNotAdjusted(ComparisonMetricsClassic.MATCH_FACTOR).getAsDouble(), 0);
	}
}
