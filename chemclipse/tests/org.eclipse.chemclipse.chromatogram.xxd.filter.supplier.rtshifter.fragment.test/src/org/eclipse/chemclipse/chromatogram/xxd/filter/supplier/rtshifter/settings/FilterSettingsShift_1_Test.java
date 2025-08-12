/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.rtshifter.settings;

import static org.junit.Assert.assertEquals;

public class FilterSettingsShift_1_Test {

	public void test_1() {

		int millisecondsToShift = 0;
		FilterSettingsShift settings = new FilterSettingsShift(millisecondsToShift, true);
		assertEquals(millisecondsToShift, settings.getMillisecondsShift());
	}

	public void test_2() {

		int millisecondsToShift = 1;
		FilterSettingsShift settings = new FilterSettingsShift(millisecondsToShift, true);
		assertEquals(millisecondsToShift, settings.getMillisecondsShift());
	}

	public void test_3() {

		int millisecondsToShift = -1;
		FilterSettingsShift settings = new FilterSettingsShift(millisecondsToShift, true);
		assertEquals(millisecondsToShift, settings.getMillisecondsShift());
	}

	public void test_4() {

		int millisecondsToShift = 1500;
		FilterSettingsShift settings = new FilterSettingsShift(millisecondsToShift, true);
		assertEquals(millisecondsToShift, settings.getMillisecondsShift());
	}

	public void test_5() {

		int millisecondsToShift = -1500;
		FilterSettingsShift settings = new FilterSettingsShift(millisecondsToShift, true);
		assertEquals(millisecondsToShift, settings.getMillisecondsShift());
	}
}