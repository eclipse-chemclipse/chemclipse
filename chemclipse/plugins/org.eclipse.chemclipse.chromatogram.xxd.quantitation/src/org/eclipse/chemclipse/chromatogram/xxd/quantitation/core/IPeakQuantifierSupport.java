/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.quantitation.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.quantitation.exceptions.NoPeakQuantifierAvailableException;

public interface IPeakQuantifierSupport {

	/**
	 * Returns the id of the selected quantifier name.<br/>
	 * The id of the selected quantifier is used to determine which quantifier should
	 * be used<br/>
	 * Be aware of that the first index is 0. It is a 0-based index.
	 */
	String getPeakQuantifierId(int index) throws NoPeakQuantifierAvailableException;

	/**
	 * Returns an IPeakQuantifierSupplier object.<br/>
	 * The object stores some additional supplier information.
	 */
	IPeakQuantifierSupplier getPeakQuantifierSupplier(String quantifierId) throws NoPeakQuantifierAvailableException;

	/**
	 * Returns an ArrayList with all available quantifier ids.<br/>
	 */
	List<String> getAvailablePeakQuantifierIds() throws NoPeakQuantifierAvailableException;

	/**
	 * Returns the list of available quantifier names.
	 */
	String[] getPeakQuantifierNames() throws NoPeakQuantifierAvailableException;
}
