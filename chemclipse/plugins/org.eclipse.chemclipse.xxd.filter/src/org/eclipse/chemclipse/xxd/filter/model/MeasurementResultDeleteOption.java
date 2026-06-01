/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.filter.model;

import org.eclipse.chemclipse.support.text.ILabel;

public enum MeasurementResultDeleteOption implements ILabel {

	IDENTIFIER_ALL("Remove Results Completely"), //
	IDENTIFIER_SPECIFIC("Target (Specific Identifier)"), //
	IDENTIFIER_REGEX("Target (Identifier Regular Expression)"); //

	private String label = "";

	private MeasurementResultDeleteOption(String label) {

		this.label = label;
	}

	@Override
	public String label() {

		return label;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}
}