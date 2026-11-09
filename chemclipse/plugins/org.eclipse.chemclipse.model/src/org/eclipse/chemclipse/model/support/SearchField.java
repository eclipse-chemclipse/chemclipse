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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.support;

import java.util.Arrays;
import java.util.List;

public enum SearchField {

	ALL(Arrays.asList("all")), // //$NON-NLS-1$
	NAME(Arrays.asList("name")), // //$NON-NLS-1$
	REFERENCE_IDENTIFIER(Arrays.asList("ref", "id")), // //$NON-NLS-1$ //$NON-NLS-2$
	FORMULA(Arrays.asList("formula")), // //$NON-NLS-1$
	SMILES(Arrays.asList("smiles")), // //$NON-NLS-1$
	INCHI(Arrays.asList("inchi")), // //$NON-NLS-1$
	INCHI_KEY(Arrays.asList("inchikey")), // //$NON-NLS-1$
	CAS(Arrays.asList("cas")), // //$NON-NLS-1$
	COMMENTS(Arrays.asList("comment", "comments")), // //$NON-NLS-1$ //$NON-NLS-2$
	SYNONYMS(Arrays.asList("synonym", "synonyms")), // //$NON-NLS-1$ //$NON-NLS-2$
	DATABASE(Arrays.asList("database", "db")), // //$NON-NLS-1$ //$NON-NLS-2$
	FLAVOR_ODOR(Arrays.asList("odor")), // //$NON-NLS-1$
	FLAVOR_MATRIX(Arrays.asList("matrix")), // //$NON-NLS-1$
	FLAVOR_SOLVENT(Arrays.asList("solvent")); //$NON-NLS-1$

	private List<String> keywords;

	private SearchField(List<String> keywords) {

		this.keywords = keywords;
	}

	public List<String> keywords() {

		return keywords;
	}
}