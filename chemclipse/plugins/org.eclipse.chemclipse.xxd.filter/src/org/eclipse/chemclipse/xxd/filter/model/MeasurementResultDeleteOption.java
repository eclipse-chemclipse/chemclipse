/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public String label() {

		return label;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}
}