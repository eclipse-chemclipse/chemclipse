/*******************************************************************************
 * Copyright (c) 2018, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - helper for DPI aware font creation
 *******************************************************************************/
package org.eclipse.chemclipse.swt.ui.support;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;

public class Fonts {

	private static FontRegistry fontRegistry = new FontRegistry();

	/**
	 * Returns a cached font from the font registry.
	 * In case of an error, the system font is returned.
	 * Fonts are disposed when the display is disposed.
	 * 
	 * @param display
	 * @param name
	 * @param height
	 * @param style
	 * @return
	 */
	public static Font getCachedFont(Device display, String name, int height, int style) {

		String fontId = name + "-" + height + "-" + style;
		if(!fontRegistry.hasValueFor(fontId)) {
			fontRegistry.put(fontId, new FontData[]{new FontData(name, height, style)});
		}
		Font font = fontRegistry.get(fontId);
		return font != null ? font : display.getSystemFont();
	}

	/**
	 * Creates a font so it looks the same size on different DPIs for the given device.
	 * Please note that the caller is responsible for disposing the font.
	 * 
	 * @param device
	 * @param fontData
	 * @return
	 */
	public static Font createDPIAwareFont(Device device, FontData fontData) {

		Point dpi = device.getDPI();
		int pointHeight = fontData.getHeight() * 72 / dpi.y;
		return new Font(device, fontData.getName(), pointHeight, fontData.getStyle());
	}
}
