/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.swt.ui.support;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.graphics.Color;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Colors_1_UITest {

	private Color color = Colors.RED;

	@Test
	public void test1() {

		int alpha = 255;
		int colorCode = Colors.getColorRgba(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		assertEquals(-65536, colorCode);
		String rgb = Colors.getColorRgbHtml(colorCode);
		assertEquals("rgb(255,0,0)", rgb);
		String rgba = Colors.getColorRgbaHtml(colorCode);
		assertEquals("rgba(255,0,0,1.0)", rgba);
	}

	@Test
	public void test2() {

		int alpha = 200;
		int colorCode = Colors.getColorRgba(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		assertEquals(-922812416, colorCode);
		String rgb = Colors.getColorRgbHtml(colorCode);
		assertEquals("rgb(255,0,0)", rgb);
		String rgba = Colors.getColorRgbaHtml(colorCode);
		assertEquals("rgba(255,0,0,0.7843137254901961)", rgba);
	}

	@Test
	public void test3() {

		int alpha = 150;
		int colorCode = Colors.getColorRgba(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		assertEquals(-1761673216, colorCode);
		String rgb = Colors.getColorRgbHtml(colorCode);
		assertEquals("rgb(255,0,0)", rgb);
		String rgba = Colors.getColorRgbaHtml(colorCode);
		assertEquals("rgba(255,0,0,0.5882352941176471)", rgba);
	}

	@Test
	public void test4() {

		int alpha = 100;
		int colorCode = Colors.getColorRgba(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		assertEquals(1694433280, colorCode);
		String rgb = Colors.getColorRgbHtml(colorCode);
		assertEquals("rgb(255,0,0)", rgb);
		String rgba = Colors.getColorRgbaHtml(colorCode);
		assertEquals("rgba(255,0,0,0.39215686274509803)", rgba);
	}

	@Test
	public void test5() {

		int alpha = 50;
		int colorCode = Colors.getColorRgba(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		assertEquals(855572480, colorCode);
		String rgb = Colors.getColorRgbHtml(colorCode);
		assertEquals("rgb(255,0,0)", rgb);
		String rgba = Colors.getColorRgbaHtml(colorCode);
		assertEquals("rgba(255,0,0,0.19607843137254902)", rgba);
	}

	@Test
	public void test6() {

		int alpha = 0;
		int colorCode = Colors.getColorRgba(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		assertEquals(16711680, colorCode);
		String rgb = Colors.getColorRgbHtml(colorCode);
		assertEquals("rgb(255,0,0)", rgb);
		String rgba = Colors.getColorRgbaHtml(colorCode);
		assertEquals("rgba(255,0,0,0.0)", rgba);
	}
}