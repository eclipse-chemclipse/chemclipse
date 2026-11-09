/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.quantitation;

import org.eclipse.chemclipse.model.l10n.ModelMessages;
import org.eclipse.chemclipse.support.text.ILabel;

public enum QuantitationFlag implements ILabel {
	NONE("", ""), // //$NON-NLS-1$ //$NON-NLS-2$
	ZERO("0", "Z"), // //$NON-NLS-1$ //$NON-NLS-2$
	NEGATIVE("< 0", "N"), // //$NON-NLS-1$ //$NON-NLS-2$
	LOWER_MIN_AREA(ModelMessages.lessThanMinArea, "L"), // //$NON-NLS-1$
	HIGHER_MAX_AREA(ModelMessages.greaterThanMaxArea, "H"); //$NON-NLS-1$

	private String label = ""; //$NON-NLS-1$
	private String shortcut = ""; //$NON-NLS-1$

	private QuantitationFlag(String label, String shortcut) {

		this.label = label;
		this.shortcut = shortcut;
	}

	@Override
	public String label() {

		return label;
	}

	public String shortcut() {

		return shortcut;
	}

	public static String[][] getOptions() {

		return ILabel.getOptions(values());
	}
}