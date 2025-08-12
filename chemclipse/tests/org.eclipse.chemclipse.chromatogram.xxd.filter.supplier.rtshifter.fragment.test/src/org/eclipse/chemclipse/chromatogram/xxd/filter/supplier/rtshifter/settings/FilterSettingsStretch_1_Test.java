/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
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

import org.junit.Test;

public class FilterSettingsStretch_1_Test {

	@Test
	public void test_1() {

		FilterSettingsStretch settings = new FilterSettingsStretch();
		settings.setScanDelay(2300);
		settings.setChromatogramLength(60000);
		assertEquals(2300, settings.getScanDelay());
		assertEquals(60000, settings.getChromatogramLength());
	}
}