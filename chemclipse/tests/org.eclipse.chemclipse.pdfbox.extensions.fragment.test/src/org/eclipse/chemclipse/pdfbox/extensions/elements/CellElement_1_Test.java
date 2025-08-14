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
package org.eclipse.chemclipse.pdfbox.extensions.elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.eclipse.chemclipse.pdfbox.extensions.settings.ReferenceX;
import org.eclipse.chemclipse.pdfbox.extensions.settings.ReferenceY;
import org.eclipse.chemclipse.pdfbox.extensions.settings.TextOption;
import org.junit.Before;
import org.junit.Test;

public class CellElement_1_Test {

	private CellElement element;

	@Before
	public void setUp() {

		element = new CellElement("Hello", 10.0f, CellElement.BORDER_ALL);
	}

	@Test
	public void test1() {

		assertEquals(-1.0f, element.getX(), 0);
	}

	@Test
	public void test1a() {

		element.setX(10.0f);
		assertEquals(10.0f, element.getX(), 0);
	}

	@Test
	public void test2() {

		assertEquals(-1.0f, element.getY(), 0);
	}

	@Test
	public void test2a() {

		element.setY(10.0f);
		assertEquals(10.0f, element.getY(), 0);
	}

	@Test
	public void test3() {

		assertEquals(12.0f, element.getFontSize(), 0);
	}

	@Test
	public void test3a() {

		element.setFontSize(14.0f);
		assertEquals(14.0f, element.getFontSize(), 0);
	}

	@Test
	public void test4() {

		assertEquals(PDType1Font.HELVETICA, element.getFont());
	}

	@Test
	public void test4a() {

		element.setFont(PDType1Font.COURIER);
		assertEquals(PDType1Font.COURIER, element.getFont());
	}

	@Test
	public void test5() {

		assertEquals(Color.BLACK, element.getColor());
	}

	@Test
	public void test5a() {

		element.setColor(Color.CYAN);
		assertEquals(Color.CYAN, element.getColor());
	}

	@Test
	public void test6() {

		assertEquals(ReferenceX.LEFT, element.getReferenceX());
	}

	@Test
	public void test6a() {

		element.setReferenceX(ReferenceX.RIGHT);
		assertEquals(ReferenceX.RIGHT, element.getReferenceX());
	}

	@Test
	public void test7() {

		assertEquals(ReferenceY.TOP, element.getReferenceY());
	}

	@Test
	public void test7a() {

		element.setReferenceY(ReferenceY.BOTTOM);
		assertEquals(ReferenceY.BOTTOM, element.getReferenceY());
	}

	@Test
	public void test7b() {

		element.setReferenceY(ReferenceY.CENTER);
		assertEquals(ReferenceY.CENTER, element.getReferenceY());
	}

	@Test
	public void test8() {

		assertEquals(-1.0f, element.getMinHeight(), 0);
	}

	@Test
	public void test8a() {

		element.setMinHeight(10.0f);
		assertEquals(10.0f, element.getMinHeight(), 0);
	}

	@Test
	public void test9() {

		assertEquals("Hello", element.getText());
	}

	@Test
	public void test9a() {

		element.setText("Hello World");
		assertEquals("Hello World", element.getText());
	}

	@Test
	public void test10() {

		assertEquals(TextOption.NONE, element.getTextOption());
	}

	@Test
	public void test10a() {

		element.setTextOption(TextOption.SHORTEN);
		assertEquals(TextOption.SHORTEN, element.getTextOption());
	}

	@Test
	public void test10b() {

		element.setTextOption(TextOption.MULTI_LINE);
		assertEquals(TextOption.MULTI_LINE, element.getTextOption());
	}

	@Test
	public void test11() {

		assertTrue(element.isBorderSet());
	}

	@Test
	public void test11a() {

		element.setBorder(CellElement.BORDER_NONE);
		assertFalse(element.isBorderSet());
	}

	@Test
	public void test12() {

		assertTrue(element.isBorderSet(CellElement.BORDER_LEFT));
	}

	@Test
	public void test12a() {

		element.setBorder(CellElement.BORDER_RIGHT | CellElement.BORDER_BOTTOM | CellElement.BORDER_TOP);
		assertFalse(element.isBorderSet(CellElement.BORDER_LEFT));
	}

	@Test
	public void test13() {

		assertTrue(element.isBorderSet(CellElement.BORDER_RIGHT));
	}

	@Test
	public void test13a() {

		element.setBorder(CellElement.BORDER_LEFT | CellElement.BORDER_BOTTOM | CellElement.BORDER_TOP);
		assertFalse(element.isBorderSet(CellElement.BORDER_RIGHT));
	}

	@Test
	public void test14() {

		assertTrue(element.isBorderSet(CellElement.BORDER_TOP));
	}

	@Test
	public void test14a() {

		element.setBorder(CellElement.BORDER_LEFT | CellElement.BORDER_RIGHT | CellElement.BORDER_BOTTOM);
		assertFalse(element.isBorderSet(CellElement.BORDER_TOP));
	}

	@Test
	public void test15() {

		assertTrue(element.isBorderSet(CellElement.BORDER_BOTTOM));
	}

	@Test
	public void test15a() {

		element.setBorder(CellElement.BORDER_LEFT | CellElement.BORDER_RIGHT | CellElement.BORDER_TOP);
		assertFalse(element.isBorderSet(CellElement.BORDER_BOTTOM));
	}

	@Test
	public void test16() {

		assertEquals(10.0f, element.getMaxWidth(), 0);
	}

	@Test
	public void test16a() {

		element.setMaxWidth(20.0f);
		assertEquals(20.0f, element.getMaxWidth(), 0);
	}
}
