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
package org.eclipse.chemclipse.model.columns;

import org.eclipse.chemclipse.model.l10n.ModelMessages;
import org.eclipse.chemclipse.support.text.ILabel;

public enum CarrierGas implements ILabel {

	HELIUM(ModelMessages.helium, "He"), // //$NON-NLS-1$
	HYDROGEN(ModelMessages.hydrogen, "H2"), // //$NON-NLS-1$
	NITROGEN(ModelMessages.nitrogen, "N2"); //$NON-NLS-1$

	private String label = ""; //$NON-NLS-1$
	private String formula = ""; //$NON-NLS-1$

	private CarrierGas(String label, String formula) {

		this.label = label;
		this.formula = formula;
	}

	@Override
	public String label() {

		return label;
	}

	public String formula() {

		return formula;
	}
}