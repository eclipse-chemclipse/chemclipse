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

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.eclipse.chemclipse.pdfbox.extensions.settings.ReferenceX;
import org.eclipse.chemclipse.pdfbox.extensions.settings.ReferenceY;
import org.eclipse.chemclipse.pdfbox.extensions.settings.TextOption;
import org.junit.Before;
import org.junit.Test;

public class TextElement_1_Test {

	private TextElement element;

	@Before
	public void setUp() {

		element = new TextElement(10.0f, 20.0f, 30.0f);
	}

	@Test
	public void test1() {

		assertEquals(10.0f, element.getX(), 0);
	}

	@Test
	public void test1a() {

		element.setX(15.0f);
		assertEquals(15.0f, element.getX(), 0);
	}

	@Test
	public void test2() {

		assertEquals(20.0f, element.getY(), 0);
	}

	@Test
	public void test2a() {

		element.setY(25.0f);
		assertEquals(25.0f, element.getY(), 0);
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

		assertEquals("", element.getText());
	}

	@Test
	public void test9a() {

		element.setText("Hello World");
		assertEquals("Hello World", element.getText());
	}

	@Test
	public void test9b() {

		element.setText("Hello\tWorld");
		assertEquals("Hello World", element.getText());
	}

	@Test
	public void test9c() {

		element.setText(null);
		assertEquals("", element.getText());
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

		assertEquals(30.0f, element.getMaxWidth(), 0);
	}

	@Test
	public void test11a() {

		element.setMaxWidth(10.0f);
		assertEquals(10.0f, element.getMaxWidth(), 0);
	}
}
