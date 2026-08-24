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
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.model.core.IClassifier;

public interface ILibraryInformation extends IClassifier, Serializable {

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
	 * Returns CAS number of the library mass spectrum.
	 */
	String getCasNumber();

	/**
	 * Sets the CAS number of the library mass spectrum.
	 */
	void setCasNumber(String casNumber);

	/**
	 * Add an additional CAS numbers.
	 */
	void addCasNumber(String casNumber);

	void deleteCasNumber(String casNumber);

	/**
	 * Returns an unmodifiable list of the CAS numbers.
	 */
	List<String> getCasNumbers();

	/**
	 * Clear CAS numbers.
	 */
	void clearCasNumbers();

	/**
	 * Returns the formula of the library mass spectrum.
	 */
	String getFormula();

	/**
	 * Sets the formula of the library mass spectrum.
	 */
	void setFormula(String formula);

	/**
	 * Returns the SMILES of the library mass spectrum.
	 */
	String getSmiles();

	/**
	 * Sets the SMILES of the library mass spectrum.
	 */
	void setSmiles(String smiles);

	/**
	 * Returns the InChI of the library mass spectrum.
	 */
	String getInChI();

	/**
	 * Sets the InChI of the library mass spectrum.
	 */
	void setInChI(String inChI);

	String getInChIKey();

	void setInChIKey(String inChIKey);

	/**
	 * Returns the mol weight of the library mass spectrum.
	 */
	double getMolWeight();

	/**
	 * Sets the mol weight of the library mass spectrum.
	 */
	void setMolWeight(double molWeight);

	/**
	 * Returns the exact mass of the library mass spectrum.
	 * 
	 * @return String
	 */
	double getExactMass();

	/**
	 * Sets the exact mass of the library mass spectrum.
	 */
	void setExactMass(double exactMass);

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

	int getRetentionTime();

	void setRetentionTime(int retentionTime);

	float getRetentionIndex();

	void setRetentionIndex(float retentionIndex);

	/**
	 * Returns an unmodifiable list of the available column
	 * index markers in the correct sort order.
	 */
	List<IColumnIndexMarker> getColumnIndexMarkers();

	void add(IColumnIndexMarker columnIndexMarker);

	void delete(IColumnIndexMarker columnIndexMarker);

	void clearFlavorMarker();

	/**
	 * Returns an unmodifiable list of the available
	 * flavor markers.
	 */
	List<IFlavorMarker> getFlavorMarkers();

	void add(IFlavorMarker flavorMarker);

	void delete(IFlavorMarker flavorMarker);

	String getMoleculeStructure();

	void setMoleculeStructure(String moleculeStructure);

	String getCompoundClass();

	void setCompoundClass(String compoundClass);
}