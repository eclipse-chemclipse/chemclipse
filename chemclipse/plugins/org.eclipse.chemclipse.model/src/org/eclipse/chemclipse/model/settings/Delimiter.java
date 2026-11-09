/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.settings;

import org.eclipse.chemclipse.model.l10n.ModelMessages;
import org.eclipse.chemclipse.support.text.ILabel;

public enum Delimiter implements ILabel {

	COMMA(',', ModelMessages.comma), //
	SEMICOLON(';', ModelMessages.semicolon), //
	TAB('\t', ModelMessages.tab);

	private char character;
	private String label;

	private Delimiter(char character, String label) {

		this.character = character;
		this.label = label;
	}

	public char getCharacter() {

		return character;
	}

	@Override
	public String label() {

		return label;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}
}