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
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.eclipse.chemclipse.pdfbox.extensions.settings.PageBase;
import org.eclipse.chemclipse.pdfbox.extensions.settings.PageSettings;
import org.eclipse.chemclipse.pdfbox.extensions.settings.Unit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PageUtil_3_Test {

	private static final String TEXT = "Hallo Welt!";
	private static final PDFont FONT_REGULAR = PDType1Font.HELVETICA;
	private static final float FONT_SIZE_12 = 12.0f;

	private PDDocument document;
	private PageUtil pageUtilPT;
	private PageUtil pageUtilMM;

	@Before
	public void setUp() throws IOException {

		document = new PDDocument();
		pageUtilPT = new PageUtil(document, new PageSettings(PDRectangle.A4, PageBase.BOTTOM_LEFT, Unit.PT, false));
		pageUtilMM = new PageUtil(document, new PageSettings(PDRectangle.A4, PageBase.TOP_LEFT, Unit.MM, false));
	}

	@After
	public void tearDown() throws IOException {

		document.close();
	}

	@Test
	public void test1() throws IOException {

		assertEquals(58.008003f, pageUtilPT.calculateTextWidth(FONT_REGULAR, FONT_SIZE_12, TEXT), 0);
		assertEquals(20.463903f, pageUtilMM.calculateTextWidth(FONT_REGULAR, FONT_SIZE_12, TEXT), 0);
	}

	@Test
	public void test2() throws IOException {

		assertEquals(9.43296f, pageUtilPT.calculateTextHeight(FONT_REGULAR, FONT_SIZE_12), 0);
		assertEquals(3.3277333f, pageUtilMM.calculateTextHeight(FONT_REGULAR, FONT_SIZE_12), 0);
	}
}
