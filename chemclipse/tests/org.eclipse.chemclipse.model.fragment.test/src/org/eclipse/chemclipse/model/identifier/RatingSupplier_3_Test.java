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

public class RatingSupplier_3_Test {

	private IRatingSupplier ratingSupplier = new RatingSupplier(new ComparisonResult(20.0f, 80.0f, 0, 0));

	@Test
	public void test1() {

		assertEquals("Convoluted Target (Impurities)", ratingSupplier.getAdvise());
	}

	@Test
	public void test2() {

		assertEquals(50.0f, ratingSupplier.getScore(), 0);
	}

	@Test
	public void test3() {

		assertEquals(RatingStatus.VERY_BAD, ratingSupplier.getStatus());
	}
}