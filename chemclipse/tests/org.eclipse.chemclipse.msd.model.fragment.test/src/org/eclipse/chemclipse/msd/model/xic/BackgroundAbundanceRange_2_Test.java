/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.xic;

import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.support.BackgroundAbundanceRange;
import org.eclipse.chemclipse.model.support.IBackgroundAbundanceRange;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the class BackgroundAbundanceRange concerning equals, hashCode and
 * toString.
 */
public class BackgroundAbundanceRange_2_Test {

	private IBackgroundAbundanceRange backgroundAbundanceRange1;
	private IBackgroundAbundanceRange backgroundAbundanceRange2;

	@Before
	public void setUp() throws Exception {

		backgroundAbundanceRange1 = new BackgroundAbundanceRange(0, 5);
		backgroundAbundanceRange2 = new BackgroundAbundanceRange(0, 5);
	}

	@Test
	public void testEquals_1() {

		assertTrue(backgroundAbundanceRange1.equals(backgroundAbundanceRange2));
	}

	@Test
	public void testEquals_2() {

		assertTrue(backgroundAbundanceRange2.equals(backgroundAbundanceRange1));
	}

	@Test
	public void testHashCode_1() {

		assertTrue(backgroundAbundanceRange1.hashCode() == backgroundAbundanceRange2.hashCode());
	}

	@Test
	public void testHashCode_2() {

		assertTrue(backgroundAbundanceRange2.hashCode() == backgroundAbundanceRange1.hashCode());
	}

	@Test
	public void testToString_1() {

		assertTrue(backgroundAbundanceRange1.toString().equals(backgroundAbundanceRange2.toString()));
	}

	@Test
	public void testToString_2() {

		assertTrue(backgroundAbundanceRange2.toString().equals(backgroundAbundanceRange1.toString()));
	}
}
