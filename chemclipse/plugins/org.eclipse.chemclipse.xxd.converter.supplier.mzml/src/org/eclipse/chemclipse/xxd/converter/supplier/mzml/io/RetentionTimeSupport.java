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
package org.eclipse.chemclipse.xxd.converter.supplier.mzml.io;

public class RetentionTimeSupport {

	private static final double MICROSECONDS = 1000.0d;

	private RetentionTimeSupport() {

	}

	/**
	 * Rounds to the nearest millisecond. Times are written as decimal numbers
	 * with limited precision, so a scan at 42993.5 ms may be stored as
	 * 0.716558333333 min = 42993.49999998 ms and a scan at 485460 ms as
	 * 8.091 min = 485459.99999999994 ms. Snapping to whole microseconds first
	 * removes that noise before rounding.
	 */
	public static int toMilliseconds(double milliseconds) {

		double snapped = Math.round(milliseconds * MICROSECONDS) / MICROSECONDS;
		return (int)Math.round(snapped);
	}
}
