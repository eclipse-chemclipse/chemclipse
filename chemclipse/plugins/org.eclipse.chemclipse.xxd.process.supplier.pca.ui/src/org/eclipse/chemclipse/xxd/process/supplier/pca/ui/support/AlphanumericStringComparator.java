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
 * Lorenz Gerber- initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.support;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlphanumericStringComparator implements Comparator<String> {

	private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+|\\D+)"); //$NON-NLS-1$

	@Override
	public int compare(String s1, String s2) {

		Matcher m1 = NUMBER_PATTERN.matcher(s1);
		Matcher m2 = NUMBER_PATTERN.matcher(s2);

		while(m1.find() && m2.find()) {
			String part1 = m1.group();
			String part2 = m2.group();

			boolean isNum1 = part1.matches("\\d+"); //$NON-NLS-1$
			boolean isNum2 = part2.matches("\\d+"); //$NON-NLS-1$

			if(isNum1 && isNum2) {
				int num1 = Integer.parseInt(part1);
				int num2 = Integer.parseInt(part2);
				if(num1 != num2) {
					return Integer.compare(num1, num2);
				}
			} else {
				int result = part1.compareToIgnoreCase(part2);
				if(result != 0) {
					return result;
				}
			}
		}

		return Integer.compare(m1.groupCount(), m2.groupCount());
	}

}
