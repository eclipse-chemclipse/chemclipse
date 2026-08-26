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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ComparisonMetrics_3_Test {

	@Test
	public void testEValueRanksLowerFirst() {

		IComparisonMetric evalue = new ComparisonMetric("e", "E-Value", "", "0.###E0", false, ComparisonMetric.LOWER_IS_BETTER);
		assertTrue(evalue.getComparator().compare(1.0e-50d, 1.0d) < 0);
	}
}
