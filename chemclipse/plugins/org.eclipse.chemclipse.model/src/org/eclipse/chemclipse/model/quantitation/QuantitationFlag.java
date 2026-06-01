/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.quantitation;

import org.eclipse.chemclipse.support.text.ILabel;

public enum QuantitationFlag implements ILabel {
	NONE("", ""), //
	ZERO("0", "Z"), //
	NEGATIVE("< 0", "N"), //
	LOWER_MIN_AREA("< Min Area", "L"), //
	HIGHER_MAX_AREA("> Max Area", "H");

	private String label = "";
	private String shortcut = "";

	private QuantitationFlag(String label, String shortcut) {

		this.label = label;
		this.shortcut = shortcut;
	}

	@Override
	public String label() {

		return label;
	}

	public String shortcut() {

		return shortcut;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}
}