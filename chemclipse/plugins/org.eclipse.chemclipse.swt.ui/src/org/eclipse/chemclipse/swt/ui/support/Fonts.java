/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - helper for DPI aware font creation
 *******************************************************************************/
package org.eclipse.chemclipse.swt.ui.support;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

public class Fonts {

	private static FontRegistry fontRegistry = new FontRegistry();

	/**
	 * Returns a cached font from the font registry.
	 * In case of an error, the system font is returned.
	 * Fonts are disposed when the display is disposed.
	 */
	public static Font getCachedFont(String name, int height, int style) {

		String fontId = name + "-" + height + "-" + style;
		if(!fontRegistry.hasValueFor(fontId)) {
			fontRegistry.put(fontId, new FontData[]{new FontData(name, height, style)});
		}
		return fontRegistry.get(fontId);
	}

}
