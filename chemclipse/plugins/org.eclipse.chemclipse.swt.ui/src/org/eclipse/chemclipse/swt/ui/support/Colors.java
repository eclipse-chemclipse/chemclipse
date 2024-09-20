/*******************************************************************************
 * Copyright (c) 2008, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.swt.ui.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Display;

public class Colors {

	public static final int ALPHA_TRANSPARENT = 0;
	public static final int ALPHA_OPAQUE = 255;
	/*
	 * System colors
	 */
	public static final Color WHITE = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_WHITE);
	public static final Color BLACK = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_BLACK);
	public static final Color RED = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_RED);
	public static final Color DARK_RED = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
	public static final Color GREEN = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_GREEN);
	public static final Color DARK_GREEN = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN);
	public static final Color GRAY = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_GRAY);
	public static final Color DARK_GRAY = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
	public static final Color CYAN = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_CYAN);
	public static final Color DARK_CYAN = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_DARK_CYAN);
	public static final Color BLUE = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	public static final Color DARK_BLUE = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE);
	public static final Color MAGENTA = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_MAGENTA);
	public static final Color YELLOW = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
	public static final Color DARK_YELLOW = DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW);
	/*
	 * Map specific colors
	 */
	private static final Map<Integer, Map<RGB, Color>> COLOR_MAP_RGB = new HashMap<Integer, Map<RGB, Color>>(); // Alpha and Colors
	//
	public static final Color LIGHT_RED = Colors.getColor(new RGB(249, 154, 152));
	public static final Color LIGHT_GREEN = Colors.getColor(new RGB(166, 255, 139));
	public static final Color LIGHT_YELLOW = Colors.getColor(new RGB(255, 254, 136));
	/*
	 * Levels 01 - 10 (light to dark)
	 */
	public static final Color GREEN_LEVEL_01 = Colors.getColor(new RGB(229, 255, 213));
	public static final Color GREEN_LEVEL_02 = Colors.getColor(new RGB(204, 255, 170));
	public static final Color GREEN_LEVEL_03 = Colors.getColor(new RGB(179, 255, 128));
	public static final Color GREEN_LEVEL_04 = Colors.getColor(new RGB(153, 255, 85));
	public static final Color GREEN_LEVEL_05 = Colors.getColor(new RGB(127, 255, 42));
	public static final Color GREEN_LEVEL_06 = Colors.getColor(new RGB(102, 255, 0));
	public static final Color GREEN_LEVEL_07 = Colors.getColor(new RGB(85, 255, 0));
	public static final Color GREEN_LEVEL_08 = Colors.getColor(new RGB(68, 255, 0));
	public static final Color GREEN_LEVEL_09 = Colors.getColor(new RGB(51, 255, 0));
	public static final Color GREEN_LEVEL_10 = Colors.getColor(new RGB(34, 255, 0));
	/*
	 * These are system color ids, defined org.eclipse.swt.SWT. If you use own
	 * color, dispose them, if not needed any more.
	 * The colors are black -> dark red -> red
	 * 255, 0, 0
	 * 150, 0, 0
	 * 0, 0, 0
	 */
	public static final String COLOR_SCHEME_RED = "Red";
	private static final int[] colorIdsGradientRed = new int[]{255, 0, 45, 85, 125, 165, 205};
	private static List<Color> colorsGradientRed;
	/*
	 * Normal gradient
	 * Red, Black, Gray, Green, Cyan, Yellow, Magenta, Blue
	 */
	public static final String COLOR_SCHEME_GRADIENT = "Gradient";
	private static final int[] colorIdsGradient = new int[]{3, 2, 16, 6, 14, 8, 12, 10};
	private static List<Color> colorsGradient;
	/*
	 * Red with higher contrast
	 */
	public static final String COLOR_SCHEME_RED_CONTRAST = "Red Contrast";
	private static final int[] colorIdsGradientRedContrast = new int[]{255, 0, 85, 165};
	private static List<Color> colorsGradientRedContrast;
	/*
	 * High Contrast
	 * Blue, Cyan, Gray, Magenta, Green, Black, Dark Yellow, Red
	 */
	public static final String COLOR_SCHEME_HIGH_CONTRAST = "High Contrast";
	private static final int[] colorIdsGradientHighContrast = new int[]{ //
			SWT.COLOR_BLUE, //
			SWT.COLOR_CYAN, //
			SWT.COLOR_GRAY, //
			SWT.COLOR_MAGENTA, //
			SWT.COLOR_GREEN, //
			SWT.COLOR_BLACK, //
			SWT.COLOR_DARK_YELLOW, //
			SWT.COLOR_RED //
	};
	private static List<Color> colorsGradientHighContrast;
	/*
	 * Publication
	 * Red, Green, Blue, Dark Red, Dark Green, Dark Blue
	 */
	public static final String COLOR_SCHEME_PUBLICATION = "Publication";
	private static final RGB[] colorIdsGradientPublication = new RGB[]{ //
			new RGB(255, 0, 0), //
			new RGB(0, 255, 0), //
			new RGB(0, 0, 255), //
			new RGB(150, 0, 0), //
			new RGB(0, 150, 0), //
			new RGB(0, 0, 150) //
	};
	private static List<Color> colorsGradientPublication;
	/*
	 * Print
	 */
	public static final String COLOR_SCHEME_PRINT = "Print";
	private static final RGB[] colorIdsGradientPrint = new RGB[]{ //
			new RGB(0, 69, 134), //
			new RGB(255, 66, 14), //
			new RGB(255, 211, 32), //
			new RGB(87, 157, 28), //
			new RGB(126, 0, 33), //
			new RGB(131, 202, 255), //
			new RGB(49, 64, 4), //
			new RGB(174, 207, 0), //
			new RGB(75, 31, 111), //
			new RGB(255, 149, 14) //
	};
	private static List<Color> colorsGradientPrint;
	/*
	 * Grayscale
	 */
	public static final String COLOR_SCHEME_GRAYSCALE = "Grayscale";
	private static final RGB[] colorIdsGradientGrayscale = new RGB[]{ //
			new RGB(26, 26, 26), // 90%
			new RGB(51, 51, 51), // 80%
			new RGB(77, 77, 77), // 70%
			new RGB(102, 102, 102), // 60%
			new RGB(128, 128, 128), // 50%
			new RGB(153, 153, 153), // 40%
			new RGB(179, 179, 179), // 30%
			new RGB(204, 204, 204), // 20%
			new RGB(230, 230, 230), // 10%
			new RGB(236, 236, 236), // 7.5%
			new RGB(242, 242, 242), // 5%
			new RGB(249, 249, 249) // 2.5%
	};
	private static List<Color> colorsGradientGrayscale;
	/*
	 * Analysis
	 */
	public static final String COLOR_SCHEME_ANALYSIS = "Analysis";
	private static final RGB[] colorIdsGradientAnalysis = new RGB[]{ //
			new RGB(215, 244, 227), //
			new RGB(175, 233, 198), //
			new RGB(135, 222, 170), //
			new RGB(95, 211, 141) //
	};
	private static List<Color> colorsGradientAnalysis;
	/*
	 * Noise
	 */
	public static final String COLOR_SCHEME_NOISE = "Noise";
	private static final RGB[] colorIdsGradientNoise = new RGB[]{ //
			new RGB(246, 213, 255), //
			new RGB(238, 170, 255), //
			new RGB(229, 128, 255), //
			new RGB(170, 0, 212) //
	};
	private static List<Color> colorsGradientNoise;
	/*
	 * 
	 */
	public static final String COLOR_SCHEME_UNLIMITED = "Unlimited";
	/**
	 * Creates a color array.
	 */
	static {
		initializeColors();
	}

	public static String[][] getAvailableColorSchemes() {

		String[][] elements = new String[8][2];
		//
		elements[0][0] = COLOR_SCHEME_RED;
		elements[0][1] = COLOR_SCHEME_RED;
		//
		elements[1][0] = COLOR_SCHEME_RED_CONTRAST;
		elements[1][1] = COLOR_SCHEME_RED_CONTRAST;
		//
		elements[2][0] = COLOR_SCHEME_GRADIENT;
		elements[2][1] = COLOR_SCHEME_GRADIENT;
		//
		elements[3][0] = COLOR_SCHEME_HIGH_CONTRAST;
		elements[3][1] = COLOR_SCHEME_HIGH_CONTRAST;
		//
		elements[4][0] = COLOR_SCHEME_PUBLICATION;
		elements[4][1] = COLOR_SCHEME_PUBLICATION;
		//
		elements[5][0] = COLOR_SCHEME_PRINT;
		elements[5][1] = COLOR_SCHEME_PRINT;
		//
		elements[6][0] = COLOR_SCHEME_GRAYSCALE;
		elements[6][1] = COLOR_SCHEME_GRAYSCALE;
		//
		elements[7][0] = COLOR_SCHEME_UNLIMITED;
		elements[7][1] = COLOR_SCHEME_UNLIMITED;
		//
		return elements;
	}

	public static IColorScheme getColorScheme(String colorSchemeId) {

		IColorScheme colorScheme;
		//
		switch(colorSchemeId) {
			case COLOR_SCHEME_GRADIENT:
				colorScheme = new ColorScheme(colorsGradient);
				break;
			case COLOR_SCHEME_RED_CONTRAST:
				colorScheme = new ColorScheme(colorsGradientRedContrast);
				break;
			case COLOR_SCHEME_HIGH_CONTRAST:
				colorScheme = new ColorScheme(colorsGradientHighContrast);
				break;
			case COLOR_SCHEME_PUBLICATION:
				colorScheme = new ColorScheme(colorsGradientPublication);
				break;
			case COLOR_SCHEME_PRINT:
				colorScheme = new ColorScheme(colorsGradientPrint);
				break;
			case COLOR_SCHEME_GRAYSCALE:
				colorScheme = new ColorScheme(colorsGradientGrayscale);
				break;
			case COLOR_SCHEME_ANALYSIS:
				colorScheme = new ColorScheme(colorsGradientAnalysis);
				break;
			case COLOR_SCHEME_NOISE:
				colorScheme = new ColorScheme(colorsGradientNoise);
				break;
			case COLOR_SCHEME_UNLIMITED:
				colorScheme = new UnlimitedColorSchema();
				break;
			default:
				colorScheme = new ColorScheme(colorsGradientRed);
				break;
		}
		//
		return colorScheme;
	}

	public static Color getColor(RGB rgb) {

		return getColor(rgb, ALPHA_OPAQUE);
	}

	public static Color getColor(RGBA rgba) {

		RGB rgb = rgba.rgb;
		return getColor(rgb, rgba.alpha);
	}

	public static Color getColor(RGB rgb, int alpha) {

		/*
		 * Get the alpha color map.
		 */
		Map<RGB, Color> alphaColors = COLOR_MAP_RGB.get(alpha);
		if(alphaColors == null) {
			alphaColors = new HashMap<RGB, Color>();
			COLOR_MAP_RGB.put(alpha, alphaColors);
		}
		/*
		 * Get the color.
		 */
		Color color = alphaColors.get(rgb);
		if(color == null) {
			Display display = DisplayUtils.getDisplay();
			color = new Color(display, rgb, alpha);
			alphaColors.put(rgb, color);
		}
		//
		return color;
	}

	public static Color getColor(int red, int green, int blue) {

		return getColor(red, green, blue, ALPHA_OPAQUE);
	}

	public static Color getColor(int red, int green, int blue, int alpha) {

		RGB rgb = new RGB(red, green, blue);
		return getColor(rgb, alpha);
	}

	public static String getColor(Color color) {

		if(color != null) {
			return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
		} else {
			return "255,255,255"; // White
		}
	}

	/*
	 * rgb: 255,0,0
	 */
	public static Color getColor(String rgb) {

		return getColor(rgb, ALPHA_OPAQUE);
	}

	public static Color getColor(int color) {

		return getColor(getColorRGB(color), ALPHA_OPAQUE);
	}

	/*
	 * E.g.:
	 * 0,0,0 and alpha
	 * or
	 * 255,255,255 and alpha
	 * ...
	 * Returns WHITE on exception.
	 */
	public static Color getColor(String rgb, int alpha) {

		try {
			/*
			 * Assume that there are 3 values.
			 */
			String[] values = rgb.split(",");
			int red = Integer.parseInt(values[0].trim());
			int green = Integer.parseInt(values[1].trim());
			int blue = Integer.parseInt(values[2].trim());
			return getColor(new RGB(red, green, blue), alpha);
		} catch(Exception e) {
			return WHITE;
		}
	}

	// ----------------------------------------------private methods
	/**
	 * Creates a color array by given size.<br/>
	 * The colors will be repeated if the size is greater than the colors
	 * defined in colorIds.
	 * 
	 * @param size
	 */
	private static void initializeColors() {

		/*
		 * In this case, we use system colors. We do not need to dispose them.
		 * If you use own colors, dispose them, if not needed any more.
		 */
		colorsGradientRed = initialize(colorIdsGradientRed);
		colorsGradient = initialize(colorIdsGradient);
		colorsGradientRedContrast = initialize(colorIdsGradientRedContrast);
		colorsGradientHighContrast = initialize(colorIdsGradientHighContrast);
		colorsGradientPublication = initialize(colorIdsGradientPublication);
		colorsGradientPrint = initialize(colorIdsGradientPrint);
		colorsGradientGrayscale = initialize(colorIdsGradientGrayscale);
		colorsGradientAnalysis = initialize(colorIdsGradientAnalysis);
		colorsGradientNoise = initialize(colorIdsGradientNoise);
	}

	private static List<Color> initialize(int[] colorIds) {

		Display display = DisplayUtils.getDisplay();
		List<Color> colors = new ArrayList<Color>();
		for(int colorId : colorIds) {
			Color color = display.getSystemColor(colorId);
			colors.add(color);
		}
		//
		return colors;
	}

	private static List<Color> initialize(RGB[] rgbs) {

		List<Color> colors = new ArrayList<Color>();
		for(RGB rgb : rgbs) {
			Color color = getColor(rgb);
			colors.add(color);
		}
		//
		return colors;
	}

	public static int[] getColorRgba(int color) {

		int[] rgba = new int[4];
		int value = color;
		int r = (value >> 16) & 0xFF;
		int g = (value >> 8) & 0xFF;
		int b = (value >> 0) & 0xFF;
		int alpha = ((value >> 24) & 0xff);
		rgba[0] = r;
		rgba[1] = g;
		rgba[2] = b;
		rgba[3] = alpha;
		return rgba;
	}

	public static RGB getColorRGB(int color) {

		int[] rgba = getColorRgba(color);
		return new RGB(rgba[0], rgba[1], rgba[2]);
	}

	public static int getColorRgba(int r, int g, int b, double alpha) {

		int a = (int)(alpha * 255);
		return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
	}

	public static String getColorRgbaHtml(int color) {

		int value = color;
		int r = (value >> 16) & 0xFF;
		int g = (value >> 8) & 0xFF;
		int b = (value >> 0) & 0xFF;
		double alpha = ((value >> 24) & 0xff) / 255;
		return "rgba(" + r + " ," + g + ", " + b + ", " + alpha + ")";
	}

	public static String getColorRgbHtml(int color) {

		int value = color;
		int r = (value >> 16) & 0xFF;
		int g = (value >> 8) & 0xFF;
		int b = (value >> 0) & 0xFF;
		return "rgb(" + r + " ," + g + ", " + b + ")";
	}
}