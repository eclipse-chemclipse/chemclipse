/*******************************************************************************
 * Copyright (c) 2010, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - extend classifiable
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import java.io.Serializable;
import java.util.Set;

import org.eclipse.chemclipse.model.core.IClassifier;

public interface ILibraryInformation extends IClassifier, Serializable, ILibraryInformationBiological, ILibraryInformationChemical, ILibraryInformationFlavor, ILibraryInformationChromatography {

	/**
	 * Returns the name of the library mass spectrum.
	 */
	String getName();

	/**
	 * Sets the name of the library mass spectrum.
	 */
	void setName(String name);

	/**
	 * Returns the list of synonyms or an empty list.
	 */
	Set<String> getSynonyms();

	/**
	 * Sets the synonyms.
	 * The set must be not null.
	 */
	void setSynonyms(Set<String> synonyms);

	/**
	 * Returns comments of the library mass spectrum.
	 */
	String getComments();

	/**
	 * Sets comments to the library mass spectrum.
	 */
	void setComments(String comments);

	/**
	 * Returns the reference id.
	 * This field is used to track internal references.
	 */
	String getReferenceIdentifier();

	/**
	 * Sets the reference id.
	 * This field is used to track other references.
	 */
	void setReferenceIdentifier(String referenceIdentifier);

	/**
	 * Returns miscellaneous information ...
	 */
	String getMiscellaneous();

	/**
	 * Sets miscellaneous information ...
	 */
	void setMiscellaneous(String miscellaneous);

	String getDatabase();

	void setDatabase(String database);

	int getDatabaseIndex();

	void setDatabaseIndex(int databaseIndex);

	/**
	 * Returns the contributor information.
	 */
	String getContributor();

	/**
	 * Sets the contributor information.
	 */
	void setContributor(String contributor);

	String getHit();

	void setHit(String hit);
}