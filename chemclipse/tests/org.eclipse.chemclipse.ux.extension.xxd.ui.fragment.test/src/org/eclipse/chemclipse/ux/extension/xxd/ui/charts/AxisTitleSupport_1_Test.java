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
package org.eclipse.chemclipse.ux.extension.xxd.ui.charts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AxisTitleSupport_1_Test {

	@Test
	public void testAxisTitleWithUnit() {

		assertEquals("Intensity [mAU]", AxisTitleSupport.getAxisTitle("Intensity", "mAU"));
	}

	@Test
	public void testAxisTitleTrimsUnit() {

		assertEquals("Intensity [absorbance unit]", AxisTitleSupport.getAxisTitle("Intensity", " absorbance unit "));
	}

	@Test
	public void testAxisTitleWithoutUnit() {

		assertEquals("Intensity", AxisTitleSupport.getAxisTitle("Intensity", ""));
		assertEquals("Intensity", AxisTitleSupport.getAxisTitle("Intensity", "  "));
		assertEquals("Intensity", AxisTitleSupport.getAxisTitle("Intensity", null));
	}
}
