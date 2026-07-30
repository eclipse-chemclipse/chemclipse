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
package org.eclipse.chemclipse.support.formats;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.ParsePosition;

public class MagnitudeScaledDecimalFormat extends DecimalFormat {

	private static final long serialVersionUID = 7786246213111283025L;

	private final int exponent;

	public MagnitudeScaledDecimalFormat(String pattern, DecimalFormatSymbols symbols, int exponent) {

		super(pattern, symbols);
		this.exponent = exponent;
	}

	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {

		double scale = Math.pow(10, exponent);
		return super.format(number / scale, toAppendTo, pos);
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {

		Number result = super.parse(source, parsePosition);
		if(result != null) {
			return result.doubleValue() * Math.pow(10, exponent);
		}
		return result;
	}

	public static int orderOfMagnitude(Number number) {

		return (int)Math.floor(Math.log(number.doubleValue()) / Math.log(10));
	}

	public static String toSuperscript(String exponent) {

		return exponent //
						.replace("0", "⁰") //
						.replace("1", "¹") //
						.replace("2", "²") //
						.replace("3", "³") //
						.replace("4", "⁴") //
						.replace("5", "⁵") //
						.replace("6", "⁶") //
						.replace("7", "⁷") //
						.replace("8", "⁸") //
						.replace("9", "⁹");
	}
}