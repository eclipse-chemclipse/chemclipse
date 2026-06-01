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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.scan.core;

import org.eclipse.chemclipse.support.text.ILabel;

public enum DensityOperation implements ILabel {

	NONE("None"), //
	INCREASE_ADJUST("Increase (Adjust)"), //
	INCREASE_ADD("Increase (Add)"), //
	DECREASE_REMOVE("Decrease (Remove)"), //
	DECREASE_ADJUST("Decrease (Adjust)"); //

	private String label = "";
	private int modifications = 0;

	private DensityOperation(String label) {

		this.label = label;
	}

	@Override
	public String label() {

		return label;
	}

	public int getModifications() {

		return modifications;
	}

	public void setModifications(int modifications) {

		this.modifications = modifications;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}
}