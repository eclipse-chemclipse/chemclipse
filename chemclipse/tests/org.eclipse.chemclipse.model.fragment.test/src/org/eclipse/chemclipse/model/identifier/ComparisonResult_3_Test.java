/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import junit.framework.TestCase;

public class ComparisonResult_3_Test extends TestCase {

	private IComparisonResult comparisonResult = new ComparisonResult(80.0f, 70.0f, 0.0f, 0.0f);
	private IRatingSupplier ratingSupplier = comparisonResult.getRatingSupplier();

	@Override
	protected void setUp() throws Exception {

		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertEquals("", ratingSupplier.getAdvise());
	}

	public void test2() {

		assertEquals(75.0f, ratingSupplier.getScore());
	}

	public void test3() {

		assertEquals(RatingStatus.AVERAGE, ratingSupplier.getStatus());
	}
}