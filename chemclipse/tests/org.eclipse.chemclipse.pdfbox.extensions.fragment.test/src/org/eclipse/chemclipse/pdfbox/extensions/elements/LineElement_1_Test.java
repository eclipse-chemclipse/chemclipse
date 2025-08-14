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

import org.junit.Before;
import org.junit.Test;

public class LineElement_1_Test {

	private LineElement element;

	@Before
	public void setUp() {

		element = new LineElement(10.0f, 20.0f, 30.0f, 40.0f);
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

		assertEquals(30.0f, element.getX1(), 0);
	}

	@Test
	public void test3a() {

		element.setX1(35.0f);
		assertEquals(35.0f, element.getX1(), 0);
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

		assertEquals(Color.BLACK, element.getColor());
	}

	@Test
	public void test5a() {

		element.setColor(Color.CYAN);
		assertEquals(Color.CYAN, element.getColor());
	}

	@Test
	public void test6() {

		assertEquals(40.0f, element.getY1(), 0);
	}

	@Test
	public void test6a() {

		element.setY1(45.0f);
		assertEquals(45.0f, element.getY1(), 0);
	}
}
