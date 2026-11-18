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
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.impl.settings;

import org.eclipse.chemclipse.support.text.ILabel;

/**
 * The span of scans over which the signal coverage of an ion channel is measured.
 */
public enum CoverageReference implements ILabel {
	/**
	 * From the first scan in which the channel has signal to the end of the range - a channel that
	 * only starts mid-chromatogram is judged on the part where it exists.
	 */
	FIRST_SIGNAL("From First Signal"), //
	/**
	 * All scans of the range.
	 */
	WHOLE_RANGE("Whole Range"); //

	private String label = "";

	private CoverageReference(String label) {

		this.label = label;
	}

	@Override
	public String label() {

		return label;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}
}
