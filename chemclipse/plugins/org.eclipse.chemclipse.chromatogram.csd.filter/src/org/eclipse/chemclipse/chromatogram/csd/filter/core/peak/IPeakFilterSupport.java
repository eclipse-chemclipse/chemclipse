/*******************************************************************************
 * Copyright (c) 2015, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.csd.filter.core.peak;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.csd.filter.exceptions.NoPeakFilterSupplierAvailableException;

public interface IPeakFilterSupport {

	/**
	 * Returns the id of the selected filter name.<br/>
	 * The id of the selected filter is used to determine which filter should be used.
	 * Be aware of that the first index is 0. It is a 0-based index.
	 */
	String getFilterId(int index) throws NoPeakFilterSupplierAvailableException;

	/**
	 * Returns an IPeakFilterSupplier object.<br/>
	 * The object stores some additional supplier information.
	 */
	IPeakFilterSupplier getFilterSupplier(String filterId) throws NoPeakFilterSupplierAvailableException;

	/**
	 * Returns an ArrayList with all available peak filter supplier ids.<br/>
	 */
	List<String> getAvailableFilterIds() throws NoPeakFilterSupplierAvailableException;

	/**
	 * Returns the list of available peak filter names.
	 */
	String[] getFilterNames() throws NoPeakFilterSupplierAvailableException;
}
