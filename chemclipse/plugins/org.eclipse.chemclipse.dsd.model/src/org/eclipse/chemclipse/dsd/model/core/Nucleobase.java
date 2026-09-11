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
	URACIL('U', "Uracil"), //
	PURINES('R', "purines"), //
	PYRIMIDINES('Y', "pyrimidines"), //
	KETONES('K', "ketones"), //
	AMINO('M', "with amino group"), //
	STRONG('S', "strong interaction"), //
	WEAK('W', "weak interaction"), //
	NOT_A('B', "not A"), //
	NOT_C('D', "not C"), //
	NOT_G('H', "not G"), //
	NOT_T_U('V', "neither T nor U"), //
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
			case 'U' -> URACIL;
			case 'R' -> PURINES;
			case 'Y' -> PYRIMIDINES;
			case 'K' -> KETONES;
			case 'M' -> AMINO;
			case 'S' -> STRONG;
			case 'W' -> WEAK;
			case 'B' -> NOT_A;
			case 'D' -> NOT_C;
			case 'H' -> NOT_G;
			case 'V' -> NOT_T_U;
			default -> UNKNOWN;
		};
	}
}
