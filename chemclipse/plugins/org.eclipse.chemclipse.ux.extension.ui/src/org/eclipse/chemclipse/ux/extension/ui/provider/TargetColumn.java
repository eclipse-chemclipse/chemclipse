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
package org.eclipse.chemclipse.ux.extension.ui.provider;

public enum TargetColumn {

	VERIFIED, //
	RATING, //
	NAME, //
	CAS, //
	FORMULA, //
	SMILES, //
	INCHI, //
	INCHI_KEY, //
	MOL_WEIGHT, //
	EXACT_MASS, //
	ADVICE, //
	IDENTIFIER, //
	MISCELLANEOUS, //
	COMMENTS, //
	DATABASE, //
	DATABASE_INDEX, //
	CONTRIBUTOR, //
	REFERENCE_ID, //
	RETENTION_TIME, //
	RETENTION_INDEX, //
	NCBI_TAXONOMY, //
	GENBANK_ACCESSION;

	public String getTitle() {

		return switch(this) {
			case VERIFIED -> "Verified";
			case RATING -> "Rating";
			case NAME -> "Name";
			case CAS -> "CAS";
			case FORMULA -> "Formula";
			case SMILES -> "SMILES";
			case INCHI -> "InChI";
			case INCHI_KEY -> "InChI Key";
			case MOL_WEIGHT -> "Mol Weight";
			case EXACT_MASS -> "Exact Mass";
			case ADVICE -> "Advice";
			case IDENTIFIER -> "Identifier";
			case MISCELLANEOUS -> "Miscellaneous";
			case COMMENTS -> "Comments";
			case DATABASE -> "Database";
			case DATABASE_INDEX -> "Database Index";
			case CONTRIBUTOR -> "Contributor";
			case REFERENCE_ID -> "Reference ID";
			case RETENTION_TIME -> "Retention Time";
			case RETENTION_INDEX -> "Retention Index";
			case NCBI_TAXONOMY -> "NCBI Taxonomy ID";
			case GENBANK_ACCESSION -> "GenBank Accession";
		};
	}

	public int getWidth() {

		return switch(this) {
			case VERIFIED, RATING -> 30;
			default -> 100;
		};
	}
}
