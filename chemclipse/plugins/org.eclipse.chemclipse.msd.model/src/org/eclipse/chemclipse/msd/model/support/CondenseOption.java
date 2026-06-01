/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.support;

import org.eclipse.chemclipse.support.text.ILabel;

public enum CondenseOption implements ILabel {

	OFF("Off", ""), //
	COARSE("Coarse", "0.0"), //
	STANDARD("Standard", "0.00"), //
	SENSITIVE("Sensitive", "0.000"); //

	private String label = "";
	private String decimalPattern = "";

	private CondenseOption(String label, String decimalPattern) {

		this.label = label;
		this.decimalPattern = decimalPattern;
	}

	@Override
	public String label() {

		return label;
	}

	public String decimalPattern() {

		return decimalPattern;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}
}