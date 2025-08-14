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

import org.eclipse.chemclipse.pdfbox.extensions.settings.ReferenceX;
import org.eclipse.chemclipse.pdfbox.extensions.settings.ReferenceY;
import org.junit.Before;
import org.junit.Test;

public class BoxElement_1_Test {

	private BoxElement element;

	@Before
	public void setUp() {

		element = new BoxElement(10.0f, 20.0f, 30.0f, 40.0f);
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

		assertEquals(40.0f, element.getHeight(), 0);
	}

	@Test
	public void test3a() {

		element.setHeight(45.0f);
		assertEquals(45.0f, element.getHeight(), 0);
	}

	@Test
	public void test4() {

		assertEquals(30.0f, element.getWidth(), 0);
	}

	@Test
	public void test4a() {

		element.setWidth(35.0f);
		assertEquals(35.0f, element.getWidth(), 0);
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
}
