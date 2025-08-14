/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.pdfbox.extensions.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Before;
import org.junit.Test;

public class PageSettings_1_Test {

	private PageSettings settings;

	@Before
	public void setUp() {

		settings = new PageSettings(PDRectangle.A4, PageBase.BOTTOM_LEFT, Unit.MM, true);
	}

	@Test
	public void test1() {

		assertEquals(settings.getPDRectangle(), PDRectangle.A4);
	}

	@Test
	public void test2() {

		assertEquals(settings.getPageBase(), PageBase.BOTTOM_LEFT);
	}

	@Test
	public void test3() {

		assertEquals(settings.getUnit(), Unit.MM);
	}

	@Test
	public void test4() {

		assertTrue(settings.isLandscape());
	}

	@Test
	public void test5() {

		assertFalse(new PageSettings(PDRectangle.A4, PageBase.BOTTOM_LEFT, Unit.MM, false).isLandscape());
	}
}
