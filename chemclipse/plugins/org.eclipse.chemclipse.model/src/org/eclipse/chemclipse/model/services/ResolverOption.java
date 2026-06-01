/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.services;

import org.eclipse.chemclipse.support.text.ILabel;

public enum ResolverOption implements ILabel {

	SYNONYM("Synonym"), //
	CAS("CAS"), //
	SMILES("SMILES"), //
	INCHI("InChI"), //
	INCHIKEY("InChIKey"); //

	private String label = "";

	private ResolverOption(String label) {

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