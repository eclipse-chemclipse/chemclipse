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
package org.eclipse.chemclipse.dsd.model.core;

import org.eclipse.chemclipse.support.text.ILabel;

/**
 * The nucleotide base of a DNA sequencing trace.
 */
public enum Nucleobase implements ILabel {

	ADENINE('A', "Adenine"), //
	CYTOSINE('C', "Cytosine"), //
	GUANINE('G', "Guanine"), //
	THYMINE('T', "Thymine"), //
	UNKNOWN('N', "Unknown"); //

	private final char letter;
	private final String label;

	private Nucleobase(char letter, String label) {

		this.letter = letter;
		this.label = label;
	}

	public char letter() {

		return letter;
	}

	@Override
	public String label() {

		return label;
	}

	public static Nucleobase[] channels() {

		return new Nucleobase[]{ADENINE, CYTOSINE, GUANINE, THYMINE};
	}

	public static Nucleobase of(char letter) {

		return switch(Character.toUpperCase(letter)) {
			case 'A' -> ADENINE;
			case 'C' -> CYTOSINE;
			case 'G' -> GUANINE;
			case 'T' -> THYMINE;
			default -> UNKNOWN;
		};
	}
}
