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
package org.eclipse.chemclipse.model.identifier;

public interface ILibraryInformationBiological {

	/**
	 * Returns NCBI taxonomy ID of the library organism.
	 */
	int getTaxonomyIdentifierNCBI();

	/**
	 * Sets NCBI taxonomy ID of the library organism.
	 */
	void setTaxonomyIdentifierNCBI(int taxID);

	/**
	 * Returns GeneBank accession/locus of the library organism.
	 */
	String getGenBankAccesion();

	/**
	 * Sets GeneBank accession/locus of the library organism.
	 */
	void setGenBankAccesion(String accesion);
}