/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.pdfbox.extensions.core;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.eclipse.chemclipse.pdfbox.extensions.settings.PageBase;
import org.eclipse.chemclipse.pdfbox.extensions.settings.PageSettings;
import org.eclipse.chemclipse.pdfbox.extensions.settings.Unit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PageUtil_2_Test {

	private PDDocument document;
	private PDRectangle paperSize = PDRectangle.A4;
	private boolean landscape = true;
	private PageUtil pageUtilPT;
	private PageUtil pageUtilMM;

	@Before
	public void setUp() throws IOException {

		document = new PDDocument();
		pageUtilPT = new PageUtil(document, new PageSettings(paperSize, PageBase.BOTTOM_LEFT, Unit.PT, landscape));
		pageUtilMM = new PageUtil(document, new PageSettings(paperSize, PageBase.TOP_LEFT, Unit.MM, landscape));
	}

	@After
	public void tearDown() throws IOException {

		document.close();
	}

	@Test
	public void test1() {

		assertEquals(0.0f, pageUtilPT.getPositionBaseX(0.0f), 0);
		assertEquals(0.0f, pageUtilMM.getPositionBaseX(0.0f), 0);
	}

	@Test
	public void test2() {

		assertEquals(0.0f, pageUtilPT.getPositionBaseY(0.0f), 0);
		assertEquals(595.27563f, pageUtilMM.getPositionBaseY(0.0f), 0);
	}

	@Test
	public void test3() {

		assertEquals(841.89105f, pageUtilPT.getPositionBaseX(841.89105f), 0);
		assertEquals(841.89105f, pageUtilMM.getPositionBaseX(297.0f), 0);
	}

	@Test
	public void test4() {

		assertEquals(595.2765f, pageUtilPT.getPositionBaseY(595.2765f), 0);
		/*
		 * DIN A4 (210x297 mm)
		 * PDRectangle.A4 (595.27563 pt -> 209.999693084 mm)
		 * That's why the value is not exactly 0.
		 */
		assertEquals(-8.544922E-4f, pageUtilMM.getPositionBaseY(210.0f), 0);
	}
}
