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
import static org.junit.Assert.assertNotNull;

import java.awt.Color;

import org.eclipse.chemclipse.pdfbox.extensions.core.PDTable;
import org.junit.Before;
import org.junit.Test;

public class TableElement_1_Test {

	private TableElement element;

	@Before
	public void setUp() {

		element = new TableElement(10.0f, 20.0f, 30.0f);
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

		assertEquals(30.0f, element.getColumnHeight(), 0);
	}

	@Test
	public void test3a() {

		element.setColumnHeight(35.0f);
		assertEquals(35.0f, element.getColumnHeight(), 0);
	}

	@Test
	public void test4() {

		assertEquals(1.0f, element.getLineWidth(), 0);
	}

	@Test
	public void test4a() {

		element.setLineWidth(0.2f);
		assertEquals(0.2f, element.getLineWidth(), 0);
	}

	@Test
	public void test5() {

		assertEquals(Color.GRAY, element.getColorTitle());
	}

	@Test
	public void test5a() {

		element.setColorTitle(Color.CYAN);
		assertEquals(Color.CYAN, element.getColorTitle());
	}

	@Test
	public void test6() {

		assertEquals(0.0f, element.getTextOffsetX(), 0);
	}

	@Test
	public void test6a() {

		element.setTextOffsetX(10.0f);
		assertEquals(10.0f, element.getTextOffsetX(), 0);
	}

	@Test
	public void test7() {

		assertEquals(0.0f, element.getTextOffsetY(), 0);
	}

	@Test
	public void test7a() {

		element.setTextOffsetY(10.0f);
		assertEquals(10.0f, element.getTextOffsetY(), 0);
	}

	@Test
	public void test8() {

		assertEquals(Color.LIGHT_GRAY, element.getColorData());
	}

	@Test
	public void test8a() {

		element.setColorData(Color.LIGHT_GRAY);
		assertEquals(Color.LIGHT_GRAY, element.getColorData());
	}

	@Test
	public void test9() {

		assertNotNull(element.getPDTable());
	}

	@Test
	public void test9a() {

		element.setPDTable(new PDTable());
		assertNotNull(element.getPDTable());
	}
}
