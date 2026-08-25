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
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;

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
			case VERIFIED -> ExtensionMessages.verified;
			case RATING -> ExtensionMessages.rating;
			case NAME -> ExtensionMessages.name;
			case CAS -> "CAS";
			case FORMULA -> ExtensionMessages.formula;
			case SMILES -> "SMILES";
			case INCHI -> "InChI";
			case INCHI_KEY -> "InChI Key";
			case MOL_WEIGHT -> ExtensionMessages.molWeight;
			case EXACT_MASS -> ExtensionMessages.excactMass;
			case ADVICE -> ExtensionMessages.advice;
			case IDENTIFIER -> ExtensionMessages.identifier;
			case MISCELLANEOUS -> ExtensionMessages.miscellaneous;
			case COMMENTS -> ExtensionMessages.comments;
			case DATABASE -> ExtensionMessages.database;
			case DATABASE_INDEX -> ExtensionMessages.databaseIndex;
			case CONTRIBUTOR -> ExtensionMessages.contributor;
			case REFERENCE_ID -> ExtensionMessages.referenceID;
			case RETENTION_TIME -> ExtensionMessages.retentionTime;
			case RETENTION_INDEX -> ExtensionMessages.retentionIndex;
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
