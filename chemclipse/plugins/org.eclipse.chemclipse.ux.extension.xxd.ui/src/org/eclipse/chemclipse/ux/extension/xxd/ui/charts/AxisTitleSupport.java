/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.charts;

public class AxisTitleSupport {

	private AxisTitleSupport() {

	}

	/**
	 * Appends the unit in brackets to the axis title, e.g. "Intensity [mAU]".
	 * The title is returned unchanged if no unit is given.
	 */
	public static String getAxisTitle(String title, String unit) {

		if(unit == null || unit.isBlank()) {
			return title;
		}
		return title + " [" + unit.trim() + "]";
	}
}
