/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - add support for getting suppliers
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier.comparison;

import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.msd.identifier.comparison.exceptions.NoMassSpectrumComparatorAvailableException;

public interface IMassSpectrumComparatorSupport {

	/**
	 * Returns the id of the selected filter name.<br/>
	 * The id of the selected filter is used to determine which comparer should
	 * be used to check the similarity of the two mass spectra.<br/>
	 * Be aware of that the first index is 0. It is a 0-based index.
	 */
	String getComparatorId(int index) throws NoMassSpectrumComparatorAvailableException;

	/**
	 * Returns an IMassSpectrumComparisonSupplier object.<br/>
	 * The object stores some additional supplier information.
	 */
	IMassSpectrumComparisonSupplier getMassSpectrumComparisonSupplier(String converterId) throws NoMassSpectrumComparatorAvailableException;

	/**
	 * Returns an ArrayList with all available comparator ids.
	 */
	List<String> getAvailableComparatorIds() throws NoMassSpectrumComparatorAvailableException;

	/**
	 * Returns the list of available comparator names.
	 */
	String[] getComparatorNames() throws NoMassSpectrumComparatorAvailableException;

	Collection<IMassSpectrumComparisonSupplier> getSuppliers();
}
