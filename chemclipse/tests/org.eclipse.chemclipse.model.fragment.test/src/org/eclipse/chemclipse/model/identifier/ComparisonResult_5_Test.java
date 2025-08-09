/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComparisonResult_5_Test {

	private IComparisonResult comparisonResult = new ComparisonResult(80.0f, 70.0f, 95.0f, 95.0f);
	private IRatingSupplier ratingSupplier = comparisonResult.getRatingSupplier();

	@Test
	public void test1() {

		assertEquals("", ratingSupplier.getAdvise());
	}

	@Test
	public void test2() {

		assertEquals(90.0f, ratingSupplier.getScore(), 0);
	}

	@Test
	public void test3() {

		assertEquals(RatingStatus.VERY_GOOD, ratingSupplier.getStatus());
	}
}