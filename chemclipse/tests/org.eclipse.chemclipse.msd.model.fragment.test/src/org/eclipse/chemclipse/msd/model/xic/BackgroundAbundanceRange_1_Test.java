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

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.support.BackgroundAbundanceRange;
import org.eclipse.chemclipse.model.support.IBackgroundAbundanceRange;
import org.junit.Test;

/**
 * Tests the class BackgroundAbundanceRange.
 */
public class BackgroundAbundanceRange_1_Test {

	private IBackgroundAbundanceRange backgroundAbundanceRange;

	@Test
	public void testSetup_1() {

		backgroundAbundanceRange = new BackgroundAbundanceRange(0, 5);
		assertEquals("startBackgroundAbundance", 0.0f, backgroundAbundanceRange.getStartBackgroundAbundance(), 0);
		assertEquals("stopBackgroundAbundance", 5.0f, backgroundAbundanceRange.getStopBackgroundAbundance(), 0);
	}

	@Test
	public void testSetup_2() {

		backgroundAbundanceRange = new BackgroundAbundanceRange(5.0f, 0.0f);
		assertEquals("startBackgroundAbundance", 5.0f, backgroundAbundanceRange.getStartBackgroundAbundance(), 0);
		assertEquals("stopBackgroundAbundance", 0.0f, backgroundAbundanceRange.getStopBackgroundAbundance(), 0);
	}

	@Test
	public void testSetup_3() {

		backgroundAbundanceRange = new BackgroundAbundanceRange(-1, 5.0f);
		assertEquals("startBackgroundAbundance", IBackgroundAbundanceRange.MIN_BACKGROUND_ABUNDANCE, backgroundAbundanceRange.getStartBackgroundAbundance(), 0);
		assertEquals("stopBackgroundAbundance", 5.0f, backgroundAbundanceRange.getStopBackgroundAbundance(), 0);
	}

	@Test
	public void testSetup_4() {

		backgroundAbundanceRange = new BackgroundAbundanceRange(1, -1);
		assertEquals("startBackgroundAbundance", 1.0f, backgroundAbundanceRange.getStartBackgroundAbundance(), 0);
		assertEquals("stopBackgroundAbundance", IBackgroundAbundanceRange.MAX_BACKGROUND_ABUNDANCE, backgroundAbundanceRange.getStopBackgroundAbundance(), 0);
	}

	@Test
	public void testSetup_5() {

		backgroundAbundanceRange = new BackgroundAbundanceRange(IBackgroundAbundanceRange.MAX_BACKGROUND_ABUNDANCE + 1, 5.0f);
		// Because max abundance can't be exceeded.
		assertEquals("startBackgroundAbundance", IBackgroundAbundanceRange.MAX_BACKGROUND_ABUNDANCE, backgroundAbundanceRange.getStartBackgroundAbundance(), 0);
		assertEquals("stopBackgroundAbundance", 5.0f, backgroundAbundanceRange.getStopBackgroundAbundance(), 0);
	}

	@Test
	public void testSetup_6() {

		backgroundAbundanceRange = new BackgroundAbundanceRange(1, IBackgroundAbundanceRange.MAX_BACKGROUND_ABUNDANCE + 1);
		assertEquals("startBackgroundAbundance", 1.0f, backgroundAbundanceRange.getStartBackgroundAbundance(), 0);
		assertEquals("stopBackgroundAbundance", IBackgroundAbundanceRange.MAX_BACKGROUND_ABUNDANCE, backgroundAbundanceRange.getStopBackgroundAbundance(), 0);
	}
}
