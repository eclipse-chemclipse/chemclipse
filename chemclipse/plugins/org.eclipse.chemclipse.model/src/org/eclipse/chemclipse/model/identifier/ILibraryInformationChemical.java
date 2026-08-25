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
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import java.util.List;

public interface ILibraryInformationChemical {

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
	 * Sets the molecular weight of the library mass spectrum.
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

	String getMoleculeStructure();

	void setMoleculeStructure(String moleculeStructure);

	String getCompoundClass();

	void setCompoundClass(String compoundClass);
}