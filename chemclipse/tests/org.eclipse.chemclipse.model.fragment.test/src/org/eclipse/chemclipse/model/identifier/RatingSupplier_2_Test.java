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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RatingSupplier_2_Test {

	private IRatingSupplier ratingSupplier = new RatingSupplier(ComparisonResult.COMPARISON_RESULT_BEST_MATCH);

	@Test
	public void test1() {

		assertEquals("", ratingSupplier.getAdvise());
	}

	@Test
	public void test2() {

		assertEquals(100.0f, ratingSupplier.getScore(), 0);
	}

}