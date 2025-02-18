/*******************************************************************************
 * Copyright (c) 2022 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.csd.converter.supplier.xy.io;

import org.eclipse.chemclipse.support.text.ILabel;

public enum DelimiterFormat implements ILabel {

	TAB("Tab", "\t"), //
	COMMA("Comma", ","), //
	SEMICOLON("Semicolon", ";"), //
	WHITESPACE("Whitespace", " "); //

	private String label;
	private String value;

	private DelimiterFormat(String label, String value) {

		this.label = label;
		this.value = value;
	}

	@Override
	public String label() {

		return label;
	}

	public String value() {

		return value;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}
}