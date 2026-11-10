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
package org.eclipse.chemclipse.msd.model.xic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.model.support.BackgroundAbundanceRange;
import org.eclipse.chemclipse.model.support.IBackgroundAbundanceRange;
import org.junit.jupiter.api.Test;

/**
 * Tests the class BackgroundAbundanceRange without validation.
 */
public class BackgroundAbundanceRange_4_Test {

	@Test
	public void testNegative() {

		IBackgroundAbundanceRange backgroundAbundanceRange = new BackgroundAbundanceRange(-1, -2, false);
		assertEquals(-1.0f, backgroundAbundanceRange.getStartBackgroundAbundance(), 0);
		assertEquals(-2.0f, backgroundAbundanceRange.getStopBackgroundAbundance(), 0);
	}

	@Test
	public void testValidated() {

		IBackgroundAbundanceRange backgroundAbundanceRange = new BackgroundAbundanceRange(-1, -2, true);
		assertEquals(IBackgroundAbundanceRange.MIN_BACKGROUND_ABUNDANCE, backgroundAbundanceRange.getStartBackgroundAbundance(), 0);
		assertEquals(IBackgroundAbundanceRange.MAX_BACKGROUND_ABUNDANCE, backgroundAbundanceRange.getStopBackgroundAbundance(), 0);
	}
}
