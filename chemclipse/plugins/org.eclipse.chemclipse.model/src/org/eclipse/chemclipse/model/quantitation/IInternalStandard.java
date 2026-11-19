/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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

public interface IInternalStandard {

	double STANDARD_COMPENSATION_FACTOR = 1.0d;

	String getName();

	void setName(String name);

	double getConcentration();

	String getConcentrationUnit();

	double getCompensationFactor();

	/**
	 * Handle with care.
	 * Set the compensation factor.
	 * The factor must be > 0.
	 * 
	 * @param compensationFactor
	 */
	void setCompensationFactor(double compensationFactor);

	/**
	 * The response factor is the reciprocal of the compensation factor.
	 * 
	 * @return double
	 */
	double getResponseFactor();

	String getChemicalClass();

	void setChemicalClass(String chemicalClass);
}