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
 * Aleksandar Kurtakov - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.ui.workbench;

/**
 * The e4 workbench model is persisted as XMI. Characters that XML is unable to
 * represent are rejected instead of escaped, which fails the workbench auto save.
 */
public class XmiSupport {

	private XmiSupport() {

	}

	/**
	 * Drops the characters that XMI is unable to represent, e.g. control characters
	 * read from a binary vendor file. Use it for values stored in the workbench
	 * model, such as a part label. A <code>null</code> value is returned unchanged.
	 */
	public static String removeInvalidCharacters(String value) {

		if(value == null || value.isEmpty() || value.codePoints().allMatch(XmiSupport::isValidCharacter)) {
			return value;
		}
		StringBuilder builder = new StringBuilder(value.length());
		value.codePoints().filter(XmiSupport::isValidCharacter).forEach(builder::appendCodePoint);
		return builder.toString();
	}

	/**
	 * Tests the code point against the <code>Char</code> production of XML 1.0.
	 * Unpaired surrogates are invalid, as {@link String#codePoints()} yields them as
	 * individual code points in the surrogate range.
	 */
	public static boolean isValidCharacter(int codePoint) {

		return codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD //
				|| (codePoint >= 0x20 && codePoint <= 0xD7FF) //
				|| (codePoint >= 0xE000 && codePoint <= 0xFFFD) //
				|| (codePoint >= 0x10000 && codePoint <= 0x10FFFF);
	}
}
